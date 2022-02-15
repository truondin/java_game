package cz.cvut.fel.pjv.game.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.cvut.fel.pjv.game.controller.Handler;
import cz.cvut.fel.pjv.game.controller.Music;
import cz.cvut.fel.pjv.game.model.attacks.*;
import cz.cvut.fel.pjv.game.view.Gfx;
import javafx.scene.canvas.GraphicsContext;

/**
 * Player class that is controlled by user
 */
public class Player extends Sprite {
    public int health;
    private AttackType attack;

    private final AttackType[] attackTypes = {AttackType.AIR, AttackType.EARTH, AttackType.FIRE, AttackType.WATER};
    private int counter = 0;
    private final int speed = 3;

    public int fireAmmo;
    public int waterAmmo;
    public int airAmmo;
    public int earthAmmo;

    public Player(){}

    /**
     * Constructor of player entity
     * @param x
     * x position of where to spawn
     * @param y
     * y position of where to spawn
     * @param gfx
     * Gfx for finding its image
     * @param h
     * game handler for manipulating with other entities
     */
    public Player(double x, double y, Gfx gfx, Handler h) {
        super(x, y, gfx, h);
        this.health = 100;
        setHitbox(getX() + 16, getY() + 20, width - 30, height - 20);
        this.attack = attackTypes[counter];

        this.fireAmmo = 15;
        this.waterAmmo = 15;
        this.airAmmo = 15;
        this.earthAmmo = 15;
   }

    /**
     * Moves player - adds to x and y coordinates velX and velY
     */
    public void move() {
        x += velX;
        y += velY;
    }

    /**
     * If player hits obstacle, stops movement through it
     */
    public void dontMove() {
        x -= velX;
        y -= velY;
    }

    /**
     * Checks if player is colliding with other entities
     * @return
     * returns boolean if player is colliding with other entity
     */
    public boolean collision() {
        for (Sprite sprite : handler.getSprites()) {
            if (sprite.getGfx() == Gfx.BORDER && this.intersects(sprite)) {
                return true;
            } else if (sprite.getGfx() == Gfx.WALL && this.intersects(sprite)) {
                return true;
            } else if (sprite.getGfx() == Gfx.CLOSE_ENEMY && this.intersects(sprite)) {
                return true;
            } else if (sprite.getGfx() == Gfx.RANGE_ENEMY && this.intersects(sprite)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Updates players position
     */
    public void update() {
        move();
        setHitbox(getX() + 16, getY() + 20, width - 30, height - 20); // move hitbox with player

        if (collision()) {
            dontMove();
        }

        velY = 0; // reset velocity
        velX = 0;
    }



    /**
     * Changes activated attack type
     */
    public void changeAttack() {
        counter++;
        if (counter >= 4) {
            counter = 0;
        }
        setAttack(attackTypes[counter]);
    }

    /**
     * Shoots players attack in direction depending on mouse coordinates
     * @param mouseX
     * x coordinates defining x direction of attack
     * @param mouseY
     * y coordinates defining x direction of attack
     */
    public void attack(int mouseX, int mouseY){
        switch (attack){
            case AIR:
                if (airAmmo > 0){
                    handler.addSprite(new Air(getX() + 32, getY() + 32 , Gfx.AIR_ATTACK, handler, mouseX, mouseY));
                    Thread t = new Thread(Music.AIR_SHOT);
                    t.start();
                    airAmmo--;
                }
                break;
            case EARTH:
                if (earthAmmo > 0){
                    handler.addSprite(new Earth(getX() + 32, getY() + 16 , Gfx.EARTH_ATTACK, handler, mouseX, mouseY));
                    Thread t = new Thread(Music.EARTH_SHOT);
                    t.start();
                    earthAmmo--;
                }
                break;
            case WATER:
                if (waterAmmo > 0){
                    handler.addSprite(new Water(getX() + 32, getY() + 32 , Gfx.WATER_ATTACK, handler, mouseX, mouseY));
                    Thread t = new Thread(Music.WATER_SHOT);
                    t.start();
                    waterAmmo--;
                }
                break;
            case FIRE:
                if (fireAmmo > 0 ){
                    handler.addSprite(new Fire(getX() + 32, getY() + 32, Gfx.FIRE_ATTACK, handler, mouseX, mouseY));
                    Thread t = new Thread(Music.FIRE_SHOT);
                    t.start();
                    fireAmmo--;
                }
                break;
        }
    }

    /**
     * @return
     * returns if player is dead
     */
    @JsonIgnore
    public boolean isDead() {
        return (health <= 0);
    }

    public int getSpeed() {
        return speed;
    }

    public int getHealth() {
        return health;
    }

    public AttackType getAttack() {
        return attack;
    }

    public void setAttack(AttackType attack) {
        this.attack = attack;
    }

    public AttackType[] getAttackTypes() {
        return attackTypes;
    }
}


