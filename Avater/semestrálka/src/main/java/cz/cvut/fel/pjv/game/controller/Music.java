package cz.cvut.fel.pjv.game.controller;

import cz.cvut.fel.pjv.game.view.state.Game;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Enum of music that is used in game for game sound
 */
public enum Music implements Runnable{
    MENU("src/main/resources/music/menu.mp3"),
    BACKGROUND("src/main/resources/music/background.mp3"),
    WIN("src/main/resources/music/win.mp3"),
    LOSE("src/main/resources/music/lose.mp3"),
    BTN_CLICK("src/main/resources/music/btnClick.mp3"),

    DAMAGE("src/main/resources/music/damage.mp3"),
    HEAL("src/main/resources/music/heal.mp3"),

    FIRE_COLLECT("src/main/resources/music/fire_ammo.mp3"),
    WATER_COLLECT("src/main/resources/music/water_ammo.mp3"),
    AIR_COLLECT("src/main/resources/music/air_ammo.mp3"),
    EARTH_COLLECT("src/main/resources/music/earth_ammo.mp3"),

    FIRE_SHOT("src/main/resources/music/fire.mp3"),
    WATER_SHOT("src/main/resources/music/water.mp3"),
    AIR_SHOT("src/main/resources/music/air.mp3"),
    EARTH_SHOT("src/main/resources/music/earth.mp3");

    public String filename;
    private static final Logger logger = Logger.getLogger(Music.class.getName());

    Music(String filename) {
        this.filename = filename;
    }

    /**
     * Creates Media from url path saved in enum
     * @return
     * Returns media for MediaPlayer
     */
    public Media getMusic(){
        try {
            return new Media(Paths.get(filename).toUri().toString());
        }catch (MediaException e){
            logger.log(Level.SEVERE, "mp3 file not found!", e);
        }return null;
    }

    /**
     * Plays music in thread
     */
    @Override
    public void run() {
        MediaPlayer music = new MediaPlayer(getMusic());
        music.setVolume(0.7);
        music.play();
    }
}
