package cz.cvut.fel.pjv.game.model.objects;

import cz.cvut.fel.pjv.game.controller.Handler;
import cz.cvut.fel.pjv.game.controller.Music;
import cz.cvut.fel.pjv.game.model.Player;
import cz.cvut.fel.pjv.game.model.Sprite;
import cz.cvut.fel.pjv.game.view.Gfx;
import javafx.scene.canvas.GraphicsContext;

import java.util.Random;

/**
 * Heart class that heals player
 */
public class Heart extends Sprite {
    private int amount;
    private final static Random rnd = new Random();

    public Heart() {
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
    public Heart(double x, double y, Gfx gfx, Handler h) {
        super(x, y, gfx, h);
        setHitbox(getBoundary());
        this.amount = rnd.nextInt(25);
    }

    /**
     * Updates lifecycle of heart and checks if player has collected it
     */
    public void update(){
        Player player = (Player) handler.findSprite(Gfx.PLAYER);
        if (player.intersects(this) && player.health < 100){
            addHealth(player);
          //  Music.HEAL.playMusic();
            Thread t = new Thread(Music.HEAL);
            t.start();
            handler.removeSprite(this);

        }
    }

    /**
     * Heals player by random amount
     * @param player
     * Player that is healed
     */
    public void addHealth(Player player){
        if ((player.health + amount) >= 100){
            player.health = 100;
        }else{
            player.health += amount;
        }
    }



    public int getAmount() {
        return amount;
    }
}
