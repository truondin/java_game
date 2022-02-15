package cz.cvut.fel.pjv.game.model.enemies;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.cvut.fel.pjv.game.controller.Handler;
import cz.cvut.fel.pjv.game.model.Sprite;
import cz.cvut.fel.pjv.game.model.attacks.EnemyAttack;
import cz.cvut.fel.pjv.game.model.objects.Heart;
import cz.cvut.fel.pjv.game.view.Gfx;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

import java.util.Random;

/**
 * Close enemy that attack in close range to player
 */
public class CloseEnemy extends Sprite {
    public int health = 100;
    private final int speed = 4;
    @JsonIgnore
    private Rectangle2D attackRange;
    @JsonIgnore
    private Rectangle2D fieldOfView;
    @JsonIgnore
    private Sprite target;
    private int attackCounter = 0;
    private boolean hasSpotted;

    private boolean isFrozen;
    private int frozenTime;

    private boolean isBurnt;
    private int burntTime;

    private boolean isSlowed;
    private int slowedTime;


    private static final Random random = new Random();

    public CloseEnemy() {}

    /**
     * Constructor of enemy
     * @param x
     * x position of spawning
     * @param y
     * y position of spawning
     * @param gfx
     * Gfx for finding image (works also as id)
     * @param handler
     * game handler for manipulating,when enemy attacks
     */
    public CloseEnemy(double x, double y, Gfx gfx, Handler handler) {
        super(x, y, gfx, handler);
        setHitbox(getX()+16, getY()+10, width - 30, height-10);
        this.fieldOfView = new Rectangle2D(x-96, y-96,width*4,height*4);
        this.hasSpotted = false;
        this.isFrozen = false;
        this.frozenTime = 0;
        this.isBurnt = false;
        this.burntTime = 0;
        this.isSlowed = false;
        this.slowedTime = 0;

    }

    /**
     * Updates position and livecycle of close enemy
     */
    public void update(){
        if (isDead()){
            handler.removeSprite(this);
            handler.addSprite(new Heart(this.x + 32, this.y + 32, Gfx.HEART, handler));
            handler.setEnemiesCount(handler.getEnemiesCount() - 1);
        }else if(!isFrozen) {
            if (isSlowed){
                slowDown();
            }
            x += velX;
            y += velY;
            setHitbox(getX() + 16, getY() + 10, width - 30, height - 10);
            if (isBurnt){
                burning();
            }

            if (collision()) {
                // changes direction
                x -= (velX * 3);
                y -= (velY * 3);
                velX *= -1;
                velY *= -1;
            } else {
                move();
            }
        }else{
            frozen();
        }
    }

    /**
     * Renders close enemy on canvas
     * @param gf
     * Graphics context that draws enemy on canvas
     */
    public void render(GraphicsContext gf){
        gf.drawImage(image, x, y);
        if (isFrozen){
            gf.drawImage(Gfx.FROZEN_EFFECT.getImage(), x,y+40);
        }else if (isBurnt){
            gf.drawImage(Gfx.BURN_EFFECT.getImage(), x,y+56);
        }

    }

    /**
     * Checks if enemy is dead
     * @return
     * Boolean if enemy is dead
     */
    @JsonIgnore
    public boolean isDead(){
        return health <=0 ;
    }

    /**
     * Chooses random direction (value of velocity) for enemy to go
     * @return
     * returns int of velocity for velX or velY
     */
    @JsonIgnore
    public int getDirection(){
        int[] directions = {0, speed-2, -(speed-2)};
        return directions[random.nextInt(3)];
    }

    /**
     * Moves enemy on map. If enemy spots player, starts to follow player and if in range starts to attack
     */
    public void move(){
        int choice = random.nextInt(15);
        wander(choice);

        if (hasSpotted || getFieldOfView().intersects(target.getHitbox())){
            followTarget();
            if (getAttackRange().intersects(target.getHitbox())){
                velX = 0;
                velY = 0;
                attack();
            }
        }
    }

    /**
     * Moves enemy randomly around map
     * @param choose
     * param choose decides if enemy should be moving
     */
    public void wander(int choose){
        if (choose == 0) {
            velX = getDirection();
            velY = getDirection();
        }
    }

    /**
     * Updates and returns field of view of enemy
     * @return
     * returns rectangle as a field of view of enemy
     */
    public Rectangle2D getFieldOfView(){
        this.fieldOfView = new Rectangle2D(x-96, y-96,width*4,height*4);
        return fieldOfView;
    }

    /**
     * Updates and returns attack range of enemy
     * @return
     * return rectangle as a attack range
     */
    public Rectangle2D getAttackRange(){
        this.attackRange = new Rectangle2D(x-16, y-16,width*1.5,height*1.5);
        return attackRange;
    }

    /**
     * Checks if enemies hitbox collides with other entities
     * @return
     * Returns boolean if enemy collides with other sprite
     */
    public boolean collision(){
        for (Sprite sprite: handler.getSprites()) {
            if (sprite.getGfx() == Gfx.WALL && this.intersects(sprite)) {
                hasSpotted = false;
                return true;
            } else if (sprite.getGfx() == Gfx.BORDER && this.intersects(sprite)) {
                hasSpotted = false;
                return true;
            }else if (sprite.getGfx() == Gfx.CLOSE_ENEMY){
                if (this == sprite){
                    continue;
                }else if (this.intersects(sprite)){
                    return true;
                }
            }else if (sprite.getGfx() == Gfx.RANGE_ENEMY && this.intersects(sprite)){
                return true;
            }else if(sprite.getGfx() == Gfx.PLAYER && this.intersects(sprite)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets direction (velX and velY) depending on targets position for following him
     */
    public void followTarget(){
        if (this.x < target.x){
            this.velX = speed;
        }
        if (this.x > target.x){
            this.velX = -speed;
        }
        if (this.y < target.y){
            this.velY = speed;
        }
        if (this.y > target.y){
            this.velY = -speed;
        }
    }

    /**
     * Attacks (creates enemy attack entities) if target is in range. There are 3 different attacks (diagonal, non-diagonal, both at the same time) activated randomly
     */
    public void attack(){
        int num = random.nextInt(11);
        if (attackCounter == 0 ) {
            if (num < 5) {
                diagonalAttack();
            }else if (num > 5) {
                nonDiagonalAttack();
            }else{
                diagonalAttack();
                nonDiagonalAttack();
            }
        }
        attackCounter++;
        if (attackCounter == 35){
            attackCounter = 0;
        }
    }

    /**
     * Creates 4 enemy attacks that goes diagonally
     */
    public void diagonalAttack(){
        handler.addSprite(new EnemyAttack(x + 24, y + 32, Gfx.ENEMY_ATTACK, handler, getFieldOfView().getMinX(), getFieldOfView().getMinY(),20));
        handler.addSprite(new EnemyAttack(x + 24, y + 32, Gfx.ENEMY_ATTACK, handler, getFieldOfView().getMinX(), getFieldOfView().getMaxY(), 20));
        handler.addSprite(new EnemyAttack(x + 24, y + 32, Gfx.ENEMY_ATTACK, handler, getFieldOfView().getMaxX(), getFieldOfView().getMaxY(), 20));
        handler.addSprite(new EnemyAttack(x + 24, y + 32, Gfx.ENEMY_ATTACK, handler, getFieldOfView().getMaxX(), getFieldOfView().getMinY(), 20));
    }

    /**
     * Creates 4 enemy attacks that goes non-diagonally
     */
    public void nonDiagonalAttack(){
        // vertical attacks
        handler.addSprite(new EnemyAttack(x + 24, y + 32, Gfx.ENEMY_ATTACK, handler, (getFieldOfView().getMinX() + getFieldOfView().getMaxX()) / 2, getFieldOfView().getMinY(), 20));
        handler.addSprite(new EnemyAttack(x + 24, y + 32, Gfx.ENEMY_ATTACK, handler, (getFieldOfView().getMinX() + getFieldOfView().getMaxX()) / 2, getFieldOfView().getMaxY(),20));
        // horizontal attacks
        handler.addSprite(new EnemyAttack(x + 24, y + 32, Gfx.ENEMY_ATTACK, handler, getFieldOfView().getMinX(), (getFieldOfView().getMinY() + getFieldOfView().getMaxY()) / 2,20));
        handler.addSprite(new EnemyAttack(x + 24, y + 32, Gfx.ENEMY_ATTACK, handler, getFieldOfView().getMaxX(), (getFieldOfView().getMinY() + getFieldOfView().getMaxY()) / 2,20));
    }

    /**
     * Applies water attack special abillity. Freezes enemy for certain time.
     *
     */
    private void frozen(){
        isBurnt = false;
        frozenTime++;
        if (frozenTime == 25){
            isFrozen = false;
            frozenTime = 0;
        }
    }

    /**
     * Applies fire attack special ability, Burns enemy (deals extra damage) for certain time
     */
    private void burning(){
        this.health -= 1;
        burntTime++;
        if (burntTime == 10){
            isBurnt = false;
            burntTime = 0;
        }
    }

    /**
     * Applies earth attack special ability. Slows down enemies velocity by half for certain time
     */
    private void slowDown(){
        velX = velX/2;
        velY = velY/2;
        slowedTime++;
        if (slowedTime == 30){
            isSlowed = false;
            slowedTime =0;
        }
    }

    public Sprite getTarget() {
        return target;
    }

    public void setTarget(Sprite target) {
        this.target = target;
    }

    public void setHasSpotted(boolean hasSpotted) {
        this.hasSpotted = hasSpotted;
    }

    public void setFrozen(boolean frozen) {
        isFrozen = frozen;
    }

    public void setBurnt(boolean burnt) {
        isBurnt = burnt;
    }

    public void setSlowed(boolean slowed) {
        isSlowed = slowed;
    }

    public int getSpeed() {
        return speed;
    }

    public int getAttackCounter() {
        return attackCounter;
    }

    public boolean isHasSpotted() {
        return hasSpotted;
    }

    public boolean isFrozen() {
        return isFrozen;
    }

    public int getFrozenTime() {
        return frozenTime;
    }

    public boolean isBurnt() {
        return isBurnt;
    }

    public int getBurntTime() {
        return burntTime;
    }

    public boolean isSlowed() {
        return isSlowed;
    }

    public int getSlowedTime() {
        return slowedTime;
    }

    public void setAttackRange(Rectangle2D attackRange) {
        this.attackRange = attackRange;
    }

    public void setFieldOfView(Rectangle2D fieldOfView) {
        this.fieldOfView = fieldOfView;
    }
}
