package cz.cvut.fel.pjv.game.model;



import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.cvut.fel.pjv.game.controller.Handler;
import cz.cvut.fel.pjv.game.view.Gfx;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Parent class for every models in game
 */
public class Sprite {
    public Gfx gfx;
    public double x, y;
    public double width;
    public double height;
    public float velX = 0, velY = 0;

    @JsonIgnore
    public Image image;
    @JsonIgnore
    public Rectangle2D boundary;
    @JsonIgnore
    public Rectangle2D hitbox;
    @JsonIgnore
    public Handler handler;


    public Sprite(){}

    /**
     * Sprite constructor for basic sprites (ground tiles)
     * @param x
     * x position of where to spawn
     * @param y
     * y position of where to spawn
     * @param gfx
     * gfx for finding its image
     * @param h
     * game handler for manipulating with other entities
     */
    public Sprite(double x, double y, Gfx gfx, Handler h) {
        this.x = x;
        this.y = y;
        this.gfx = gfx;
        this.image = gfx.getImage();
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.boundary = new Rectangle2D(x,y,width,height);
        this.handler = h;
        this.hitbox = null;
    }

    public void update(){
        x += velX;
        y += velY;
    }

    /**
     * Renders image into canvas
     * @param gf
     * graphics context for drawing on canvas
     */
    public void render(GraphicsContext gf){
        gf.drawImage(image, x, y);
    }

    /**
     * Checks if this hitbox collides with other sprite
     * @param other
     * Sprite that is tested for collision with this sprite
     * @return
     * returns boolean if this sprite's hitbox intersects with other's hitbox
     */
    public boolean intersects(Sprite other){
        return this.getHitbox().intersects(other.getHitbox());
    }


    public Gfx getGfx() {
        return gfx;
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }

    public float getVelX() {
        return velX;
    }
    public float getVelY() {
        return velY;
    }

    public double getWidth(){ return width;}
    public double getHeight(){ return height;}

    public Rectangle2D getHitbox() {
        return hitbox;
    }
    public Rectangle2D getBoundary() {
        return boundary;
    }

    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }

    public void setVelX(float velX) {
        this.velX = velX;
    }
    public void setVelY(float velY) {
        this.velY = velY;
    }

    public void setHitbox(double x , double y, double width, double height) {this.hitbox = new Rectangle2D(x,y,width,height);}
    public void setHitbox(Rectangle2D hitbox) {this.hitbox = hitbox;}

    public void setHandler(Handler h){
        this.handler = h;
    }

    public void setImage(Image image) {
        this.image = image;
    }



}
