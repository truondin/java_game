package cz.cvut.fel.pjv.game.model.attacks;

import cz.cvut.fel.pjv.game.controller.Handler;
import cz.cvut.fel.pjv.game.controller.Music;
import cz.cvut.fel.pjv.game.model.Player;
import cz.cvut.fel.pjv.game.model.Sprite;
import cz.cvut.fel.pjv.game.view.Gfx;
import javafx.scene.canvas.GraphicsContext;

import java.util.Random;
import java.util.logging.Logger;

/**
 * Enemy's attack
 */
public class EnemyAttack extends Sprite {
    private int damage;
    private int speed;

    private static final Logger logger = Logger.getLogger(EnemyAttack.class.getName());
    private static final Random rnd = new Random();

    public EnemyAttack() {
    }

    /**
     * Constructor for attack
     * @param x
     * X coordinate where to spawn
     * @param y
     * Y coordinate where to spawn
     * @param gfx
     * Graphics context for drawing on to canvas
     * @param h
     * Game handler
     * @param tX
     * X position of target for calculating direction
     * @param tY
     * Y position of target for calculating direction
     * @param speed
     * travel speed of attack - bigger number = slower
     */
    public EnemyAttack(double x, double y, Gfx gfx, Handler h, double tX, double tY, int speed) {
        super(x, y, gfx, h);
        this.damage = rnd.nextInt(10);
        this.speed =  speed;
        this.velX = (float) ((tX - x) / speed) * 2;
        this.velY = (float) ((tY - y) / speed) * 2;
    }

    /**
     * Updates position and livecycle of enemy attack
     */
    public void update(){
        x += velX;
        y += velY;
        setHitbox(getX(), getY(), 16, 16);
        collision();
    }


    /**
     * Deals damage to player
     * @param player
     * Player that is effected by damage of enemy attack
     */
    public void dealDamage(Player player){
        player.health -= damage;
        Thread t = new Thread(Music.DAMAGE);
        t.start();
        handler.removeSprite(this);
        logger.info("Player got damaged. Players health: " + player.health);

    }

    /**
     * Checks if enemy attack is colliding with other entities
     */
    public void collision(){
        for (Sprite sprite : handler.getSprites()) {
            if (sprite.getGfx() == Gfx.WALL && sprite.intersects(this)) {
                handler.removeSprite(this);
                break;
            } else if (sprite.getGfx() == Gfx.BORDER && sprite.intersects(this)) {
                handler.removeSprite(this);
                break;
            } else if (sprite.getGfx() == Gfx.PLAYER && sprite.intersects(this)){
                Player player = (Player) sprite;
                dealDamage(player);
                break;
            }
        }
    }

    public int getDamage() {
        return damage;
    }

    public int getSpeed() {
        return speed;
    }
}
