package cz.cvut.fel.pjv.game.view.state;

import cz.cvut.fel.pjv.game.controller.Camera;
import cz.cvut.fel.pjv.game.controller.Handler;
import cz.cvut.fel.pjv.game.controller.Music;
import cz.cvut.fel.pjv.game.controller.PlayerControl;
import cz.cvut.fel.pjv.game.model.*;
import cz.cvut.fel.pjv.game.model.attacks.AttackType;
import cz.cvut.fel.pjv.game.model.enemies.CloseEnemy;
import cz.cvut.fel.pjv.game.model.enemies.RangeEnemy;
import cz.cvut.fel.pjv.game.model.gameWorld.TileMap;
import cz.cvut.fel.pjv.game.model.objects.*;
import cz.cvut.fel.pjv.game.view.Gfx;
import javafx.animation.AnimationTimer;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Game class for playing game
 */
public class Game {
    private final Stage stage;
    private final GraphicsContext gf;
    private final Canvas canvas;
    private Scene scene;
    private final ArrayList<String> keyHandler = new ArrayList<>();
    private final MediaPlayer mp;
    private Pane pane;
    private final Font font = Font.loadFont("file:src/main/resources/fonts/font.ttf",10);

    private PlayerControl control;
    private final Handler handler;
    private final Camera camera;
    private TileMap tileMap;
    private ArrayList<int[]> walkable;
    private Player player;
    private int[] playerSpawn;
    private final int enemiesCount;

    private AnimationTimer gameloop;

    private static final Random rnd = new Random();
    private static final Logger logger = Logger.getLogger(Game.class.getName());

    private boolean isPaused;
    private boolean isEndGame;

    /**
     * Contructor of game used when loading save file
     * @param stage
     * Stage used in JavaFx window
     * @param h
     * Handler class containing sprites created from save file
     * @param w
     * Coordinates of walkable tiles
     * @param enemiesCount
     * Number of enemies that are in handler
     */
    public Game(Stage stage, Handler h, ArrayList<int[]> w, int enemiesCount){
        this.stage = stage;
        canvas = new Canvas(1980,1060);
        gf = canvas.getGraphicsContext2D();
        camera = new Camera(0,0);
        this.enemiesCount = enemiesCount;
        handler = h;
        this.player = (Player) handler.findSprite(Gfx.PLAYER);
        walkable = w;

        mp = new MediaPlayer(Objects.requireNonNull(Music.BACKGROUND.getMusic()));

    }

    /**
     * Constructor of game when creating new game
     * @param stage
     * Stage used in JavaFx
     * @param enemiesCount
     * Number of enemies to spawn in game
     */
    public Game(Stage stage, int enemiesCount){
        this.stage = stage;
        canvas = new Canvas(1980,1060);
        gf = canvas.getGraphicsContext2D();
        camera = new Camera(0,0);
        this.enemiesCount = enemiesCount;
        handler = new Handler(enemiesCount);


        tileMap = new TileMap("src/main/resources/tilemap/map.txt");
        mp = new MediaPlayer(Objects.requireNonNull(Music.BACKGROUND.getMusic()));

    }

    /**
     * prepares stage and canvas
     */
    private void init(){
        gf.setFill(Color.rgb(85,105,115));
        gf.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        stage.setTitle("Avatar");
        stage.setWidth(900);
        stage.setHeight(600);
        pane = new Pane(canvas);


        scene = new Scene(pane);
        Image image = Gfx.CROSSHAIR.getImage();
        scene.setCursor(new ImageCursor(image));

        mp.setVolume(0.2);
        mp.setCycleCount(MediaPlayer.INDEFINITE);

        isPaused = false;
        isEndGame = false;
    }

    /**
     * loads map from tilemap class and adds it into game handler
     */
    private void loadMap(){
        ArrayList<Sprite> tiles = tileMap.createBackground(handler);
        walkable = tileMap.getWalkableTiles();
        for (Sprite tile : tiles){
            handler.addSprite(tile);
        }
    }

    /**
     * Adds close enemy into game (game handler)
     * @param x
     * x coordinate for enemy spawn
     * @param y
     * y coordinate for enemy spawn
     */
    private void addCloseEnemy(int x , int y){
        CloseEnemy enemy = new CloseEnemy(x, y, Gfx.CLOSE_ENEMY, handler);
        enemy.setTarget(player);
        handler.addSprite(enemy);
    }

    /**
     * Adds range enemy into game (game handler)
     * @param x
     * x coordinate for enemy spawn
     * @param y
     * y coordinate for enemy spawn
     */
    private void addRangeEnemy(int x, int y){
        RangeEnemy enemy = new RangeEnemy(x,y, Gfx.RANGE_ENEMY,handler);
        enemy.setTarget(player);
        handler.addSprite(enemy);
    }

    /**
     * Creates new player into game (adds into handler) and spawns player on random walkable place
     * @return
     * returns newly created player
     */
    private Player createPlayer(){
        ArrayList<int[]> walkable = tileMap.getWalkableTiles();
        playerSpawn = walkable.get(rnd.nextInt(walkable.size()));
        int x = playerSpawn[0];
        int y = playerSpawn[1];

        Player player = new Player(x,y, Gfx.PLAYER, handler);
        handler.addSprite(player);
        return player;
    }

    /**
     * adds fire ammo into game
     * @param x
     * x coordinate for spawn
     * @param y
     * y coordinate for spawn
     */
    private void addFireAmmo(double x, double y){
        handler.addSprite(new FireAmmo(x, y, Gfx.FIRE_AMMO, handler));
    }

    /**
     * adds water ammo into game
     * @param x
     * x coordinate for spawn
     * @param y
     * y coordinate for spawn
     */
    private void addWaterAmmo(double x, double y){
        handler.addSprite(new WaterAmmo(x, y, Gfx.WATER_AMMO, handler));
    }
    /**
     * adds air ammo into game
     * @param x
     * x coordinate for spawn
     * @param y
     * y coordinate for spawn
     */
    private void addAirAmmo(double x, double y){
        handler.addSprite(new AirAmmo(x, y, Gfx.AIR_AMMO, handler));
    }
    /**
     * adds earth ammo into game
     * @param x
     * x coordinate for spawn
     * @param y
     * y coordinate for spawn
     */
    private void addEarthAmmo(double x, double y){
        handler.addSprite(new EarthAmmo(x, y, Gfx.EARTH_AMMO, handler));
    }

    /**
     * adds certain amount of enemies into game
     * @param count
     * amount of enemies to add
     */
    private void addEnemies(double count){
        ArrayList<int[]> walkable = this.walkable;
        walkable.remove(playerSpawn);

        for (int i = 0; i < count; i++){
            int enemyType = rnd.nextInt(2);

            int[] coord = walkable.get(rnd.nextInt(walkable.size()));

            int x = coord[0];
            int y = coord[1];

            if (enemyType == 0){
                addCloseEnemy(x, y);
            }else{
                addRangeEnemy(x, y);
            }
            walkable.remove(coord);
        }
    }

    /**
     * Randomly chooses item type that should be spawned throughout the running game
     * @return
     * returns item type
     */
    private String getItemType(){
        String[] types = {"fire", "water", "air", "earth", "heart"};
        return types[rnd.nextInt(5)];
    }

    /**
     * Adds item into game for player to collect
     */
    private void addItem(){
        ArrayList<int[]> walkable = this.walkable;
        int[] coord = walkable.get(rnd.nextInt(walkable.size()));
        String item = getItemType();

        int x = coord[0];
        int y = coord[1];

        switch (item) {
            case "fire":
                addFireAmmo(x, y);
                break;
            case "water":
                addWaterAmmo(x, y);
                break;
            case "air":
                addAirAmmo(x, y);
                break;
            case "earth":
                addEarthAmmo(x, y);
                break;
            default:
                handler.addSprite(new Heart(x, y, Gfx.HEART, handler));
                break;
        }

    }

    /**
     * randomly creates item for player
     */
    private void renderItem(){
        int num = rnd.nextInt(300);
        if (num == 0) {
            addItem();
        }
    }

    /**
     * Loads saved game to play
     */
    public void loadSave(){
        init();
        for (Sprite s : handler.getSprites()){
            s.setHandler(handler);
            if (s.getGfx() == Gfx.RANGE_ENEMY){
                RangeEnemy enemy = (RangeEnemy) s;
                enemy.setTarget(player);
            }else if(s.getGfx() == Gfx.CLOSE_ENEMY){
                CloseEnemy enemy = (CloseEnemy) s;
                enemy.setTarget(player);
            }
        }

        control = new PlayerControl(player);

        // for user's inputs
        keyboardInputListener();
        mouseInputListener();

        gameloop = setGameloop();
        gameloop.start();

        mp.play();

        stage.setScene(scene);
        stage.show();
        logger.info("Saved game started");
    }

    /**
     * Starts new game
     */
    public void start(){
        init();
        loadMap();
        player = createPlayer();
        addEnemies(enemiesCount);
        control = new PlayerControl(player);

        // for user's inputs
        keyboardInputListener();
        mouseInputListener();

        gameloop = setGameloop();
        gameloop.start();

        mp.play();

        stage.setScene(scene);
        stage.show();
    }

    /**
     * Creates gameloop for game
     * @return
     * returns AnimationTimer as gameloop
     */
    private AnimationTimer setGameloop(){
        return new AnimationTimer() {
            @Override
            public void handle(long now) {
                handler.update(camera);
                renderItem();

                gf.translate(-camera.getX(), -camera.getY());
                handler.render(gf);
                gf.translate(camera.getX(), camera.getY());

                control.movement(keyHandler);
                renderUI();
                if (handler.getEnemiesCount() == 0) {
                    new Thread(Music.WIN).start();
                    endGame("VICTORY");
                    isEndGame = true;
                    logger.info("Victory game!");
                }else if(player.isDead()){
                    new Thread(Music.LOSE).start();
                    endGame("GAME OVER");
                    isEndGame = true;
                    logger.info("Lost game");
                }
            }
        };
    }

    /**
     * Keyboard input listener handler
     */
    private void keyboardInputListener(){

        Pause p = new Pause(gf, pane, this, stage, handler);

        scene.setOnKeyPressed(e -> {
            String keyName = e.getCode().toString();
            if (!isPaused && !keyHandler.contains(keyName)) {
                keyHandler.add(keyName);
            }
            if (!isPaused && keyName.equals("SPACE")){
                control.toggleAttacks();
            }
            if (!isEndGame && keyName.equals("ESCAPE")){
                isPaused = !isPaused;
                if (isPaused){
                    paused();
                    p.create();

                }else{
                    unPaused(p);
                }
            }
        });

        scene.setOnKeyReleased(e -> {
                    String keyName = e.getCode().toString();
                    keyHandler.remove(keyName);
                }
        );
    }

    /**
     * Mouse input listener handler
     */
    private void mouseInputListener(){
        scene.setOnMousePressed(mouseEvent -> {
            if (!isPaused && !isEndGame) {
                int mouseX = (int) (mouseEvent.getX() + camera.getX());
                int mouseY = (int) (mouseEvent.getY() + camera.getY());

                control.fireAttack(mouseX, mouseY);
            }
        });
    }

    /**
     * Renders game user interface
     */
    private void renderUI(){
        renderHealthBar();
        renderAmmoCount();
    }

    /**
     * renders health bar for game user interface
     */
    private void renderHealthBar(){
        gf.setFill(Color.GRAY);
        gf.fillRect(10,10,200,32);
        gf.setFill(Color.RED);
        gf.fillRect(10,10, player.health * 2, 32);
        gf.setFill(Color.BLACK);
        gf.strokeRect(10,10,200,32);
        gf.drawImage(Gfx.HEART.getImage(),  3,5);

    }

    /**
     * Renders ammo count user interface
     */
    private void renderAmmoCount(){
        gf.save();
        gf.setFont(font);
        AttackType curr = player.getAttack();
        switch (curr){
            case AIR:
                String airCount  = player.airAmmo + "";
                gf.drawImage(Gfx.AIR_UI.getImage(), 800, 10 );
                gf.fillText(airCount , 835, 30 );
                break;
            case EARTH:
                String earthCount  = player.earthAmmo + "";
                gf.drawImage(Gfx.EARTH_UI.getImage(),  800, 10 );
                gf.fillText(earthCount , 835, 30 );
                break;
            case WATER:
                String waterCount  = player.waterAmmo + "";
                gf.drawImage(Gfx.WATER_UI.getImage(), 800, 10 );
                gf.fillText(waterCount , 835, 30 );
                break;
            case FIRE:
                String fireCount  = player.fireAmmo + "";
                gf.drawImage(Gfx.FIRE_UI.getImage(), 800, 10 );
                gf.fillText(fireCount , 835, 30 );
                break;
        }
        gf.restore();

    }

    /**
     * pauses gameloop
     */
    private void paused(){
        gameloop.stop();
        mp.pause();

        Image image = Gfx.CURSOR.getImage();
        scene.setCursor(new ImageCursor(image));
    }

    /**
     * unpauses gameloop
     * @param pause
     * Pause UI for removing from UI
     */
    public void unPaused(Pause pause){
        gameloop.start();
        mp.play();
        pause.removeButtons();

        Image image = Gfx.CROSSHAIR.getImage();
        scene.setCursor(new ImageCursor(image));
    }

    public boolean isPause() {
        return isPaused;
    }

    public void setPause(boolean pause) {
        isPaused = pause;
    }

    /**
     * Creates endgame notification
     * @param endText
     * Text to be written in notification (win or lose)
     */
    private void endGame(String endText){
        gameloop.stop();
        mp.pause();
        Image image = Gfx.CURSOR.getImage();
        scene.setCursor(new ImageCursor(image));
        End end = new End(gf,pane,stage);
        end.create(endText);
    }
}
