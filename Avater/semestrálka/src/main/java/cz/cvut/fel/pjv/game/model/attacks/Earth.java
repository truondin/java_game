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
 * Player's earth attack
 */
public class Earth extends Sprite{
    public final AttackType type = AttackType.WATER;
    private int range = 0;
    private final int damage = 20;
    private final int speed = 70;

    private static final Logger logger = Logger.getLogger(Earth.class.getName());

    public Earth() {
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
    public Earth(double x, double y, Gfx gfx, Handler h, int mouseX, int mouseY) {
        super(x, y, gfx, h);
        setHitbox(getX(), getY(), 24, 24);
        velX = (float) ((mouseX - x) / speed); // slow speed == bigger divider num
        velY = (float) ((mouseY - y) / speed);
    }

    /**
     * updates position and livecycle of earth attack
     */
    public void update() {
        range++;
        x += velX;
        y += velY;
        setHitbox(getX(), getY(), 24, 24);

        if (range == 30)  {
            handler.removeSprite(this);
        }

        // checks if collides with other entities
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
     * Deals damage to range enemy and slows him down (special ability)
     * @param enemy
     * Range enemy that is effected by earth attack
     *
     */
    public void dealDamage(RangeEnemy enemy){
        enemy.health -= damage;
        handler.removeSprite(this);

        enemy.setSlowed(true);


        logger.info("RangeEnemy hit with earth");
    }

    /**
     * Deals damage to close enemy and slows him down (special ability)
     * @param enemy
     * Close enemy that is effected by earth attack
     */
    public void dealDamage(CloseEnemy enemy){
        enemy.health -= damage;
        handler.removeSprite(this);

        enemy.setSlowed(true);

        logger.info("CloseEnemy hit with earth");
    }

    public int getDamage() {
        return damage;
    }

    public AttackType getType() {
        return type;
    }
}
