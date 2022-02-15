package cz.cvut.fel.pjv.game.model.attacks;


import cz.cvut.fel.pjv.game.controller.Handler;
import cz.cvut.fel.pjv.game.controller.Music;
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
 * Player's air attack
 */
public class Air extends Sprite {
    public final AttackType type = AttackType.AIR;
    private final int damage = 5;
    private final int speed = 20;

    private static final Logger logger = Logger.getLogger(Air.class.getName());

    public Air() {
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
    public Air(double x, double y, Gfx gfx, Handler h, int mouseX, int mouseY) {
        super(x, y, gfx, h);
        setHitbox(getX(), getY(), 16, 16);

        velX = (float) ((mouseX - x) / speed);
        velY = (float) ((mouseY - y) / speed);
    }

    /**
     * updates position and livecycle of air attack
     */
    public void update() {
        x += velX;
        y += velY;
        setHitbox(getX(), getY(), 16, 16);


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
     * Applies special ability (pushes enemy back) of air attack on enemy
     * @param enemy
     * Enemy which is effected by air attack
     */
    private void attackAbility(Sprite enemy){
        enemy.velX += (velX * 2);
        enemy.velY += (velY * 2);
    }

    /**
     * Deals damage to certain close enemy sprite
     * @param sprite
     * Sprite (enemy) that is effected by damage of air
     */
    public void dealDamage(Sprite sprite){
        RangeEnemy enemy = (RangeEnemy) sprite;
        enemy.health -= damage;
        enemy.setHasSpotted(true);
        handler.removeSprite(this);

        attackAbility(enemy);

        logger.info("RangeEnemy hit with air");
    }

    /**
     * Deals damage to certain range enemy sprite
     * @param enemy
     * enemy that is effected by damage of air
     */
    public void dealDamage(CloseEnemy enemy){
        enemy.health -= damage;
        enemy.setHasSpotted(true);
        handler.removeSprite(this);
        attackAbility(enemy);

        logger.info("CloseEnemy hit with air");
    }


    public int getDamage() {
        return damage;
    }

    public AttackType getType() {
        return type;
    }
}
