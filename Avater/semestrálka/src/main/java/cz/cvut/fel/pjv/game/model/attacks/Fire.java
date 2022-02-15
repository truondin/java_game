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
 * Player's fire attack
 */
public class Fire extends Sprite {
    public final AttackType type = AttackType.FIRE;

    private int range = 0;
    private final int damage = 5;
    private final int speed  = 40;

    private static final Logger logger = Logger.getLogger(Fire.class.getName());

    public Fire() {
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
    public Fire(double x, double y, Gfx gfx, Handler h, int mouseX, int mouseY) {
        super(x, y, gfx, h);
        setHitbox(getX(), getY(), width, height);
        velX = (float) ((mouseX - x) / speed);
        velY = (float) ((mouseY - y) / speed);
    }

    /**
     * updates position and livecycle of fire attack
     */
    public void update() {
        range++;
        x += velX;
        y += velY;
        setHitbox(getX(), getY(), width, height);


        if (range == 28)  {
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
     * Deals damage to range enemy and applies special ability of fire (burns enemy)
     * @param enemy
     * Range enemy that is effected by fire
     */
    public void dealDamage(RangeEnemy enemy){
        enemy.health -= damage;
        enemy.setHasSpotted(true);
        handler.removeSprite(this);
        enemy.setBurnt(true);

        logger.info("RangeEnemy hit with fire");
    }

    /**
     * Deals damage to close enemy and applies special ability of fire (burns enemy)
     * @param enemy
     * Close enemy that is effected by fire
     */
    public void dealDamage(CloseEnemy enemy){
        enemy.health -= damage;
        enemy.setHasSpotted(true);
        handler.removeSprite(this);

        enemy.setBurnt(true);

        logger.info("CloseEnemy hit with fire");
    }

    public int getDamage() {
        return damage;
    }

    public AttackType getType() {
        return type;
    }
}
