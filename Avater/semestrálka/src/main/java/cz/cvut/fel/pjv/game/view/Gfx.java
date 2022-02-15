package cz.cvut.fel.pjv.game.view;

import cz.cvut.fel.pjv.game.model.Sprite;
import cz.cvut.fel.pjv.game.view.state.Game;
import javafx.scene.image.Image;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Enum for game graphics
 */
public enum Gfx {
    PLAYER("player_01.png"),
    PLAYER_02("player_02.png"),
    FIRE_ATTACK("fire.png"),
    AIR_ATTACK("air.png"),
    WATER_ATTACK("water.png"),
    EARTH_ATTACK("earth.png"),
    HEART("heart.png"),

    FIRE_AMMO("fire_ammo.png"),
    AIR_AMMO("air_ammo.png"),
    WATER_AMMO("water_ammo.png"),
    EARTH_AMMO("earth_ammo.png"),

    CLOSE_ENEMY("enemy_close.png"),
    RANGE_ENEMY("range_enemy.png"),
    ENEMY_ATTACK("enemy_attack.png"),

    BURN_EFFECT("burn.png"),
    FROZEN_EFFECT("freeze.png"),

    GROUND("ground.png"),
    BORDER("vertical_border.png"),
    WALL("brick.png"),

    FIRE_UI("fire_ui.png"),
    WATER_UI("water_ui.png"),
    AIR_UI("air_ui.png"),
    EARTH_UI("earth_ui.png"),
    BACKGROUND_MENU("menu.png"),
    CURSOR("cursor.png"),
    CROSSHAIR("crosshair.png");



    public String filename;
    private static final Logger logger = Logger.getLogger(Gfx.class.getName());

    Gfx(String filename) {
        this.filename = filename;
    }

    /**
     * Creates Image from filepath
     * @return
     * returns Image of game graphics of entity
     */
    public Image getImage(){
        try {
            Image image = new Image(filename);
            return image;
        }catch (IllegalArgumentException e){
            logger.log(Level.WARNING, "image file not found!", e);
        }
        return null;
    }


}
