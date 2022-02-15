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
 * Air ammo class that adds air ammo to player
 */
public class AirAmmo extends Sprite {
    private int amount;
    private final static Random rnd = new Random();

    public AirAmmo() {
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
    public AirAmmo(double x, double y, Gfx gfx, Handler h) {
        super(x, y, gfx, h);
        setHitbox(x,y,16,16);
        this.amount = rnd.nextInt(50);
    }

    /**
     * Updates air ammo's lifecycle, checks if player has collected it.
     */
    public void update(){
        Player player = (Player) handler.findSprite(Gfx.PLAYER);

        if (player.intersects(this) && player.airAmmo < 500){
            Thread t = new Thread(Music.AIR_COLLECT);
            t.start();
            addAmmo(player);
//            Music.AIR_COLLECT.playMusic();
        }
    }

    /**
     * Adds random air ammo amount to player
     * @param player
     * Player that collects ammo
     */
    public void addAmmo(Player player){
        player.airAmmo += amount;
        handler.removeSprite(this);
    }



    public int getAmount() {
        return amount;
    }
}
