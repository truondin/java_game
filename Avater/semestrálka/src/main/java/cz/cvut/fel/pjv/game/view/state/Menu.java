package cz.cvut.fel.pjv.game.view.state;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.fel.pjv.game.controller.Handler;
import cz.cvut.fel.pjv.game.controller.Music;
import cz.cvut.fel.pjv.game.model.Player;
import cz.cvut.fel.pjv.game.model.Sprite;
import cz.cvut.fel.pjv.game.model.attacks.*;
import cz.cvut.fel.pjv.game.model.enemies.CloseEnemy;
import cz.cvut.fel.pjv.game.model.enemies.RangeEnemy;
import cz.cvut.fel.pjv.game.model.objects.*;
import cz.cvut.fel.pjv.game.view.Gfx;
import cz.cvut.fel.pjv.game.view.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manu of the game
 */
public class Menu {
    private final Stage stage;
    private Scene menu;
    private StackPane root;
    private final MediaPlayer mp = new MediaPlayer(Objects.requireNonNull(Music.MENU.getMusic()));;

    private Button play;
    private Button load;
    private Button exit;

    private static final Logger logger = Logger.getLogger(Menu.class.getName());


    public Menu(Stage stage) {
        this.stage = stage;
    }

    /**
     * prepares stage look
     */
    private void init(){
        root = new StackPane();
        menu = new Scene(root);

        stage.setTitle("Avatar");
        stage.setWidth(900);
        stage.setHeight(600);
        stage.setResizable(false);

        ImageView img = new ImageView(Gfx.BACKGROUND_MENU.getImage());
        root.getChildren().add(img);

        mp.setVolume(0.5);
        mp.setCycleCount(MediaPlayer.INDEFINITE);
        mp.play();

        Image image = Gfx.CURSOR.getImage();
        menu.setCursor(new ImageCursor(image));
    }

    /**
     * Sets gui menu
     */
    public void setMenu(){
        Label label = new Label("AVATAR");
        label.setFont(Font.loadFont("file:src/main/resources/fonts/font.ttf",35));

        play = createButton("PLAY");
        load = createButton("LOAD");
        exit = createButton("EXIT");

        VBox box = new VBox(20);
        box.getChildren().add(label);
        box.getChildren().add(play);
        box.getChildren().add(load);
        box.getChildren().add(exit);
        box.setAlignment(Pos.CENTER);
        root.getChildren().add(box);

    }

    /**
     * Starts menu gui for game preperation
     */
    public void start(){
        init();
        setMenu();

        play.setOnAction(actionEvent -> {
            new Thread(Music.BTN_CLICK).start();
            playFunction();
        });
        load.setOnAction(actionEvent -> {
            new Thread(Music.BTN_CLICK).start();
            loadFunction();
        });
        exit.setOnAction(actionEvent -> {
            new Thread(Music.BTN_CLICK).start();
            stage.close();
        });

        stage.setScene(menu);
        stage.show();
    }


    /**
     * Function of play button. Creates new scene for game preperation
     */
    public void playFunction(){
        // setting up background
        Font font = Font.loadFont("file:src/main/resources/fonts/font.ttf",20);
        root = new StackPane();
        ImageView img = new ImageView(Gfx.BACKGROUND_MENU.getImage());
        root.getChildren().add(img);

        Scene playMenu = new Scene(root);
        Image image = Gfx.CURSOR.getImage();
        playMenu.setCursor(new ImageCursor(image));

        // setting up inputs
        Label label = new Label("ENEMIES COUNT");
        label.setFont(font);

        Label count = new Label("1");
        count.setFont(font);

        Slider slider = new Slider();
        slider.setMax(20);
        slider.setMin(1);

        Button play = createButton("PLay");
        Button back = createButton("Back");

        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                int enemiesCount = (int) slider.getValue();
                count.setText(Integer.toString(enemiesCount));
            }
        });

        play.setOnAction(actionEvent -> {
            new Thread(Music.BTN_CLICK).start();
            int enemiesCount = Integer.parseInt(count.getText());
            mp.stop();
            new Game(stage,enemiesCount).start();
            logger.info("New game started - enemies count: " + enemiesCount);
        });

        back.setOnAction(actionEvent -> {
            new Thread(Music.BTN_CLICK).start();
            stage.setScene(menu);
        });

        // setting up layout of inputs
        HBox hBox = new HBox(10);
        hBox.getChildren().add(slider);
        hBox.setAlignment(Pos.CENTER);

        VBox box = new VBox(15);
        box.getChildren().add(label);
        box.getChildren().add(count);
        box.getChildren().add(hBox);
        box.getChildren().add(play);
        box.getChildren().add(back);
        box.setAlignment(Pos.CENTER);
        root.getChildren().add(box);

        // start scene
        stage.setScene(playMenu);
        stage.show();
    }

    /**
     * Function of load button. Loads game from save file (txt file) and starts game from save
     */
    public void loadFunction(){
        ObjectMapper objectMapper = new ObjectMapper();
        Handler handler = new Handler();
        int enemiesCount = 0;
        ArrayList<int[]> walkable = new ArrayList<>();
        int linesCount = 0;

        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/save/save.txt"));
            while (true) {
                String spriteData = null;
                try {
                    spriteData = reader.readLine();
                } catch (IOException e) {
                    logger.log(Level.WARNING, "No data in save.txt!", e);
                }
                if (spriteData == null){
                    break;
                }
                linesCount++;
                if (spriteData.contains("PLAYER")) {
                    Player player = objectMapper.readValue(spriteData, Player.class);

                    setSprite(player,Gfx.PLAYER.getImage(),player.x + 16, player.y + 20, player.width - 30, player.height - 20 );
                    handler.addSprite(player);
                } else if (spriteData.contains("WALL")) {
                    Obstacle wall = objectMapper.readValue(spriteData, Obstacle.class);
                    Image img = Gfx.WALL.getImage();
                    setSprite(wall, img,wall.x, wall.y, img.getWidth(), img.getHeight() );
                    handler.addSprite(wall);
                } else if (spriteData.contains("BORDER")) {
                    Obstacle border = objectMapper.readValue(spriteData, Obstacle.class);
                    Image img = Gfx.BORDER.getImage();


                    setSprite(border, img, border.x, border.y, img.getWidth(), img.getHeight());
                    handler.addSprite(border);
                } else if (spriteData.contains("GROUND")) {
                    int[] coord = new int[2];
                    Sprite ground = objectMapper.readValue(spriteData, Sprite.class);
                    ground.setImage(Gfx.GROUND.getImage());
                    handler.addSprite(ground);

                    coord[0] = (int) ground.x;
                    coord[1] = (int) ground.y;
                    walkable.add(coord);
                } else if (spriteData.contains("CLOSE_ENEMY")) {
                    CloseEnemy enemy = objectMapper.readValue(spriteData, CloseEnemy.class);


                    setSprite(enemy,Gfx.CLOSE_ENEMY.getImage(),enemy.getX()+16, enemy.getY()+10, enemy.width - 30, enemy.height-10);
                    enemy.setFieldOfView(enemy.getFieldOfView());
                    enemy.setAttackRange(enemy.getAttackRange());
                    handler.addSprite(enemy);
                    enemiesCount++;
                } else if (spriteData.contains("RANGE_ENEMY")) {
                    RangeEnemy enemy = objectMapper.readValue(spriteData, RangeEnemy.class);


                    setSprite(enemy,Gfx.RANGE_ENEMY.getImage(),enemy.getX()+16, enemy.getY()+10, enemy.width - 30, enemy.height-10);
                    enemy.setFieldOfView(enemy.getFieldOfView());
                    enemy.setAttackRange(enemy.getAttackRange());
                    handler.addSprite(enemy);
                    enemiesCount++;
                } else if (spriteData.contains("HEART")) {
                    Heart heart = objectMapper.readValue(spriteData, Heart.class);
                    Image img = Gfx.HEART.getImage();

                    setSprite(heart, img, heart.x, heart.y,img.getWidth(),img.getHeight());
                    handler.addSprite(heart);
                }else if (spriteData.contains("FIRE_AMMO")) {
                    FireAmmo ammo = objectMapper.readValue(spriteData, FireAmmo.class);


                    setSprite(ammo,Gfx.FIRE_AMMO.getImage(),ammo.x, ammo.y, 16,16 );
                    handler.addSprite(ammo);
                } else if (spriteData.contains("WATER_AMMO")) {
                    WaterAmmo ammo = objectMapper.readValue(spriteData, WaterAmmo.class);


                    setSprite(ammo,Gfx.WATER_AMMO.getImage(), ammo.x, ammo.y, 16,16);
                    handler.addSprite(ammo);
                } else if (spriteData.contains("AIR_AMMO")) {
                    AirAmmo ammo = objectMapper.readValue(spriteData, AirAmmo.class);

                    setSprite(ammo, Gfx.AIR_AMMO.getImage(),ammo.x, ammo.y, 16,16 );
                    handler.addSprite(ammo);;
                } else if (spriteData.contains("EARTH_AMMO")) {
                    EarthAmmo ammo = objectMapper.readValue(spriteData, EarthAmmo.class);
                    setSprite(ammo, Gfx.EARTH_AMMO.getImage(), ammo.x, ammo.y, 16,16);
                    handler.addSprite(ammo);
                }
                else if (spriteData.contains("FIRE_ATTACK")) {
                    Fire fire = objectMapper.readValue(spriteData, Fire.class);


                    setSprite(fire,Gfx.FIRE_ATTACK.getImage(),fire.x, fire.y, 16,16 );
                    handler.addSprite(fire);
                } else if (spriteData.contains("WATER_ATTACK")) {
                    Water water = objectMapper.readValue(spriteData, Water.class);


                    setSprite(water,Gfx.WATER_ATTACK.getImage(), water.x, water.y, 16,16);
                    handler.addSprite(water);
                } else if (spriteData.contains("AIR_ATTACK")) {
                    Air air = objectMapper.readValue(spriteData, Air.class);

                    setSprite(air, Gfx.AIR_ATTACK.getImage(),air.x, air.y, 16,16 );
                    handler.addSprite(air);;
                } else if (spriteData.contains("EARTH_ATTACK")) {
                    Earth earth = objectMapper.readValue(spriteData, Earth.class);
                    setSprite(earth, Gfx.EARTH_ATTACK.getImage(), earth.x, earth.y, 16,16);
                    handler.addSprite(earth);
                }else if (spriteData.contains("ENEMY_ATTACK")) {
                    EnemyAttack enemyAttack = objectMapper.readValue(spriteData, EnemyAttack.class);
                    setSprite(enemyAttack, Gfx.ENEMY_ATTACK.getImage(), enemyAttack.x, enemyAttack.y, 16,16);
                    handler.addSprite(enemyAttack);
                }

            }
            if (linesCount > 0) {
                handler.setEnemiesCount(enemiesCount);
                Game g = new Game(stage, handler, walkable, enemiesCount);
                g.loadSave();
            }else{
                logger.log(Level.WARNING, "File save.txt id empty");
            }
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "Save file doesnt exist!", e);
        }catch (JsonProcessingException e){
            logger.log(Level.SEVERE, "JSON data in save.txt are invalid!", e);
        }

    }

    /**
     * Sets sprite image and hitbox from saved file
     * @param curr
     * Sprite to be set
     * @param img
     * Sprite image to set
     * @param x
     * x coordinate for sprites hitbox
     * @param y
     * y coordinate for sprites hitbox
     * @param width
     * width of sprites hitbox
     * @param height
     * widht of sprites hitbox
     */
    public void setSprite(Sprite curr, Image img, double x, double y , double width, double height){
        curr.setImage(img);
        curr.setHitbox(x,y,width,height);
    }

    /**
     * Creates button for gui
     * @param text
     * Text in button
     * @return
     * returns created button
     */
    public Button createButton(String text){

        Font btnFont = Font.loadFont("file:src/main/resources/fonts/font.ttf",20);
        Button btn = new Button(text);
        btn.setFont(btnFont);
        btn.setPrefSize(165,40);
        return btn;
    }
}
