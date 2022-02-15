package cz.cvut.fel.pjv.game.model.objects;

import cz.cvut.fel.pjv.game.controller.Handler;
import cz.cvut.fel.pjv.game.controller.Music;
import cz.cvut.fel.pjv.game.model.Player;
import cz.cvut.fel.pjv.game.model.Sprite;
import cz.cvut.fel.pjv.game.view.Gfx;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;

/**
 * Fire ammo class that player collects
 */
public class FireAmmo extends Sprite {
    private int amount;
    private final static Random rnd = new Random();

    public FireAmmo() {
    }

    /**
     * Constructor of ammo
     * @param x
     * x position of where to spawn
     * @param y
     * y position of where to spawn
     * @param gfx
     * Gfx for finding its image
     * @param h
     * game handler for manipulating with other entities
     */
    public FireAmmo(double x, double y, Gfx gfx, Handler h) {
        super(x, y, gfx, h);
        setHitbox(x,y,16,16);
        this.amount = rnd.nextInt(25);
    }

    /**
     * Updates lifecycle of fire ammo and checks if player has collected it
     */
    public void update(){
        Player player = (Player) handler.findSprite(Gfx.PLAYER);
        if (player.intersects(this) && player.fireAmmo < 500){
            Thread t = new Thread(Music.FIRE_COLLECT);
            t.start();
            addAmmo(player);
        }
    }

    /**
     * Adds fire ammo amount to player
     * @param player
     * Player that collects fire ammo
     */
    public void addAmmo(Player player){
        player.fireAmmo += amount;
        handler.removeSprite(this);
    }



    public int getAmount() {
        return amount;
    }
}
