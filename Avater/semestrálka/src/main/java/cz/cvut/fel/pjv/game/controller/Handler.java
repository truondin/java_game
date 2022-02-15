package cz.cvut.fel.pjv.game.controller;

import cz.cvut.fel.pjv.game.model.Sprite;
import cz.cvut.fel.pjv.game.view.Gfx;
import cz.cvut.fel.pjv.game.view.state.Game;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Handler class that saves every sprite which must be updated and drawn on canvas during gameloop
 */
public class Handler {
    private final ArrayList<Sprite> sprites;
    private int enemiesCount;

    public Handler() {
        sprites = new ArrayList<>();
    }

    /**
     * Constructor of game handler
     * @param enemiesCount
     * number of enemies that are in game
     */
    public Handler(int enemiesCount) {
        sprites = new ArrayList<>();
        this.enemiesCount = enemiesCount;

    }

    /**
     * Updates every sprite in sprites array
     * @param camera
     * camera for updating view determined by sprite (player)
     */
    public void update(Camera camera) {
        ArrayList<Sprite> tmp = new ArrayList<>(sprites);
        for (Sprite curr: tmp){
            curr.update();
            if (curr.getGfx() == Gfx.PLAYER) {
                camera.update(curr);
            }
        }
    }


    /**
     * Renders every sprite in sprite array into Canvas
     * @param gf
     * graphics context that draws on canvas
     */
    public synchronized void render(GraphicsContext gf){
        for (Sprite sprite : sprites){
            sprite.render(gf);
        }
    }

    /**
     * Finds sprite by Gfx
     * @param find
     * wanted gfx from sprite array
     * @return
     * returns Sprite with Gfx = find param
     */
    public Sprite findSprite(Gfx find){
        for (Sprite sprite : sprites){
            if (sprite.getGfx() == find){
                return sprite;
            }
        }
        return null;
    }

    public int getEnemiesCount() {
        return enemiesCount;
    }

    public void setEnemiesCount(int enemiesCount) {
        this.enemiesCount = enemiesCount;
    }

    public void addSprite(Sprite sprite){
        sprites.add(sprite);
    }

    public void removeSprite(Sprite sprite){
        sprites.remove(sprite);
    }

    public ArrayList<Sprite> getSprites() {
        return sprites;
    }

}
