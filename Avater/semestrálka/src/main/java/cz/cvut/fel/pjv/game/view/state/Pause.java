package cz.cvut.fel.pjv.game.view.state;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.fel.pjv.game.controller.Handler;
import cz.cvut.fel.pjv.game.controller.Music;
import cz.cvut.fel.pjv.game.model.Sprite;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Pause menu when game is paused
 */
public class Pause {
    private final double x,y,width,height;
    private final Font font = Font.loadFont("file:src/main/resources/fonts/font.ttf",30);

    private final GraphicsContext gf;
    private final Pane pane;
    private final Game game;
    private final Stage stage;
    private final Handler handler;

    private Button resume;
    private Button save;
    private Button quit;
    private final ArrayList<Button> buttons = new ArrayList<>();

    private static final Logger logger = Logger.getLogger(Pause.class.getName());

    /**
     * Contructor for creating Pause menu during the game
     * @param gf
     * Graphics context for drawing on to canvas
     * @param pane
     * Pane used in stage
     * @param game
     * Game class that is running - for unpause with button
     * @param stage
     * stage used in javafx
     * @param handler
     * Game handler for save function
     */
    public Pause(GraphicsContext gf, Pane pane, Game game, Stage stage, Handler handler) {
        this.gf = gf;
        this.pane = pane;
        this.game = game;
        this.stage = stage;
        this.handler = handler;
        x = 280;
        y = 75;
        width = 325;
        height = 375;
    }

    /**
     * Creates pause menu in game
     */
    public void create(){
        gf.save();
        gf.setFill(Color.GRAY);
        gf.fillRect(x, y, width,height);
        gf.setFill(Color.BLACK);
        gf.strokeRect(x,y,width,height);

        gf.setFont(font);
        gf.fillText("PAUSED", x + 85, y + 75);

        gf.restore();

        resume = button(x+80, y+130, "resume");
        resume.setOnAction(actionEvent ->
            resumeFunction()
        );

        save = button(x+80, y+195, "save");
        save.setOnAction(actionEvent ->
            saveFunction()
        );

        quit = button(x+80, y+260, "quit");
        quit.setOnAction(actionEvent ->
            quitFunction()
        );

    }

    /**
     * deletes button from canvas created for pause
     */
    public void removeButtons(){
        for (Button btn: buttons){
            pane.getChildren().remove(btn);
        }
    }

    /**
     * Creates button for pause gui menu
     * @param x
     * x coordinates of button
     * @param y
     * y coordinates of button
     * @param text
     * text in button
     * @return
     * return created button
     */
    private Button button(double x, double y, String text){

        Font btnFont = Font.loadFont("file:src/main/resources/fonts/font.ttf",20);
        Button btn = new Button(text);
        btn.setPrefSize(165,40);
        btn.setFont(btnFont);
        btn.setLayoutX(x);
        btn.setLayoutY(y);

        buttons.add(btn);
        pane.getChildren().add(btn);
        return btn;
    }

    /**
     * Pause button function
     */
    private void resumeFunction(){
        new Thread(Music.BTN_CLICK).start();
        game.unPaused(this);
        game.setPause(!game.isPause());
        logger.info("Resume game");
    }

    /**
     * quit button function
     */
    private void quitFunction(){
        new Thread(Music.BTN_CLICK).start();
        new Menu(stage).start();
        logger.info("Quit game");
    }

    /**
     * Save button function
     */
    private void saveFunction() {
        new Thread(Music.BTN_CLICK).start();
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File("src/main/resources/save/save.txt");
        ArrayList<Sprite> list = handler.getSprites();
        try {
            FileWriter myWriter = new FileWriter(file);
            for (Sprite s : list){
                myWriter.write(objectMapper.writeValueAsString(s) + "\n");
            }
            myWriter.close();
            logger.info("Game has been saved");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Saving failed!", e);
        }


    }

}
