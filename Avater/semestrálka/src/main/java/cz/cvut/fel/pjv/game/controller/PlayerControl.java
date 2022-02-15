package cz.cvut.fel.pjv.game.controller;

import cz.cvut.fel.pjv.game.model.Player;
import cz.cvut.fel.pjv.game.model.attacks.*;
import cz.cvut.fel.pjv.game.view.Gfx;



import java.util.ArrayList;

/**
 * Class for player sprite control
 */
public class PlayerControl {
    private final Player player;

    /**
     * Constructor of PlayerControl
     * @param p
     * Player sprite that should be controlled
     */
    public PlayerControl(Player p) {
        this.player = p;
    }

    /**
     * Updates players x and y velocity for its movement
     * @param keyHandler
     * Keyhandler contains pressed keys from which is determined players movement
     */
    public void movement(ArrayList<String> keyHandler){
        if (keyHandler.contains("W")){
            player.velY = -player.getSpeed();
        }
        if (keyHandler.contains("S")){
            player.velY = player.getSpeed();
        }
        if (keyHandler.contains("A")){
            player.velX = -player.getSpeed();
            player.setImage(Gfx.PLAYER_02.getImage());
        }
        if (keyHandler.contains("D")){
            player.velX = player.getSpeed();
            player.setImage(Gfx.PLAYER.getImage());
        }
    }

    /**
     * Changes players attack type
     */
    public void toggleAttacks(){
        player.changeAttack();
    }

    /**
     * Activates players attack
     * @param mouseX
     * Mouse x coordinate for calculating X direction of attack
     * @param mouseY
     * Mouse y coordinate for calculating y direction of attack
     */
    public void fireAttack(int mouseX, int mouseY){
        player.attack(mouseX, mouseY);
    }





}

