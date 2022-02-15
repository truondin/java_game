package cz.cvut.fel.pjv.game.controller;

import cz.cvut.fel.pjv.game.model.Sprite;

/**
 * Camera class translates X and Y coordinates of graphics context to follow player
 */
public class Camera {
    private float x, y;

    /**
     * Constructor of Camera
     * @param x
     * (flaot) x coordinate
     * @param y
     * (float) y coordinate
     */
    public Camera(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Updates X and Y coordinates of camera by the param sprite
     * @param sprite
     * X and Y coordinates of sprite determines x and y of Camera
     */
    public void update(Sprite sprite){
        x += ((sprite.getX() - x) - (float) 800/2) * 0.05f;
        y += ((sprite.getY() - y) - (float) 480/2) * 0.05f;

        if (x <= 0){x = 0;}
        if (x >= 1038){x = 1038;}
        if (y <= 0){y = 0;}
        if (y >= 718){y = 718;}

    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
