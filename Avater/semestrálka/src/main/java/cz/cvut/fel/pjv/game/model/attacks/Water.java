package cz.cvut.fel.pjv.game.model.attacks;

import cz.cvut.fel.pjv.game.controller.Handler;
import cz.cvut.fel.pjv.game.model.enemies.CloseEnemy;
import cz.cvut.fel.pjv.game.model.Sprite;
import cz.cvut.fel.pjv.game.model.enemies.RangeEnemy;
import cz.cvut.fel.pjv.game.model.objects.Heart;
import cz.cvut.fel.pjv.game.view.Gfx;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;
import java.util.logging.Logger;

/**
 * Player's water attack
 */
public class Water extends Sprite{
    public final AttackType type = AttackType.WATER;
    private int range = 0;
    private final int damage = 10;
    private final int speed = 50;
    private final static Random rnd = new Random();

    private static final Logger logger = Logger.getLogger(Fire.class.getName());

    public Water() {
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
     * @param mouseX
     * X position of mouse cursor for calculating direction
     * @param mouseY
     * Y position of mouse cursor for calculating direction
     */
    public Water(double x, double y, Gfx gfx, Handler h, int mouseX, int mouseY) {
        super(x, y, gfx, h);
        setHitbox(getX(), getY(), 16, 16);

        velX = (float) ((mouseX - x) / speed); // slow speed == bigger divider num
        velY = (float) ((mouseY - y) / speed);
    }

    /**
     * Updates position and livecycle of water attack
     */
    public void update() {
        range++;
        x += velX;
        y += velY;
        setHitbox(getX(), getY(), 16, 16);

        if (range == 30)  {
            handler.removeSprite(this);
        }

        for (Sprite sprite : handler.getSprites()) {
            if (sprite.getGfx() == Gfx.WALL && sprite.intersects(this)) {
                handler.removeSprite(this);
                break;
            }else if(sprite.getGfx() == Gfx.BORDER && sprite.intersects(this)){
                handler.removeSprite(this);
                break;
            }else if(sprite.getGfx() == Gfx.CLOSE_ENEMY && sprite.intersects(this)){
                CloseEnemy enemy = (CloseEnemy) sprite;
                dealDamage(enemy);
                break;
            }else if(sprite.getGfx() == Gfx.RANGE_ENEMY && sprite.intersects(this)){
                RangeEnemy enemy = (RangeEnemy) sprite;
                dealDamage(enemy);
                break;
            }
        }
    }

    /**
     * Deals damage and applies special ability (freezes enemy) on Range enemy
     * @param enemy
     * Range enemy that is effected by water
     */
    public void dealDamage(RangeEnemy enemy){
        enemy.health -= damage;
        enemy.setHasSpotted(true);
        handler.removeSprite(this);

        enemy.setFrozen(true);

        logger.info("RangeEnemy hit with water");
    }

    /**
     * Deals damge and applies special ability (freezes enemy) on Close enemy
     * @param enemy
     * Close enemy that is effected by water
     */
    public void dealDamage(CloseEnemy enemy){
        enemy.health -= damage;
        enemy.setHasSpotted(true);
        handler.removeSprite(this);

        enemy.setFrozen(true);

        logger.info("CloseEnemy hit with water");
    }


    public AttackType getType() {
        return type;
    }

    public int getDamage() {
        return damage;
    }
}
