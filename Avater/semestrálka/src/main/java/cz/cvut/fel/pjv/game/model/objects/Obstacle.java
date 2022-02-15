package cz.cvut.fel.pjv.game.model.objects;

import cz.cvut.fel.pjv.game.controller.Handler;
import cz.cvut.fel.pjv.game.model.Sprite;
import cz.cvut.fel.pjv.game.view.Gfx;

/**
 * Obstacle class that cannot be walked through
 */
public class Obstacle extends Sprite {
    public Obstacle() {
    }

    public Obstacle(double x, double y, Gfx gfx, Handler handler) {
        super(x, y, gfx, handler);
        setHitbox(x,y,width, height);
    }
}
