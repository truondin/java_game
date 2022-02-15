package cz.cvut.fel.pjv.game.model.gameWorld;

import cz.cvut.fel.pjv.game.controller.Handler;
import cz.cvut.fel.pjv.game.model.objects.Obstacle;
import cz.cvut.fel.pjv.game.model.Sprite;
import cz.cvut.fel.pjv.game.view.Gfx;
import cz.cvut.fel.pjv.game.view.state.Game;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TileMap class that creates game map from txt file.
 * Map must be 30(width)x20(height)
 */
public class TileMap {
    private final int[][] map;
    private final int width = 30;
    private final int height = 20;
    private final ArrayList<int[]> walkableTiles;

    private static final Logger logger = Logger.getLogger(TileMap.class.getName());

    /**
     * Constructes tiles from txt file
     * @param filename
     * param filename is name of txt file
     */
    public TileMap(String filename){
        walkableTiles = new ArrayList<>();
        map = new int[height][width];
        try {
            BufferedReader read = new BufferedReader(new FileReader(filename));
            for (int row = 0; row < height; row++){
                String line = read.readLine();
                String[] values = line.split(" ");
                for (int col =0; col < width; col++){
                    map[row][col] = Integer.parseInt(values[col]);
                }
            }

        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "Map file not found!", e);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Map file does not have any more lines!", e);
        }
    }

    /**
     * Creates every single tile of map as sprite and adds them into game handler for rendering.
     * Walkable tiles are saved into walkable atribute
     * @param h
     * Handler where to save tile sprites
     * @return
     * returns ArrayList of map tiles as Sprite
     */
    public ArrayList<Sprite> createBackground(Handler h){
        ArrayList<Sprite> tiles = new ArrayList<>();
        for (int row = 0; row < height; row++){
            for (int col =0; col < width; col++){
                int value = map[row][col];
                int x = 64 * col;
                int y = 64 * row;
                int[] coords = new int[2];
                if (value == 0){
                    coords[0] = x;
                    coords[1] = y;
                    walkableTiles.add(coords);
                    tiles.add(new Sprite(x,y,Gfx.GROUND, h));
                }else if (value == 1){
                    tiles.add(new Obstacle(x,y,Gfx.BORDER, h));
                }else if(value == 2){
                    tiles.add(new Obstacle(x,y, Gfx.WALL, h));
                }
            }
        }
        return tiles;
    }

    /**
     * @return
     * returns coordinates of walkable tiles
     */
    public ArrayList<int[]> getWalkableTiles() {
        return walkableTiles;
    }
}
