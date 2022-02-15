package cz.cvut.fel.pjv.game.view.state;

import cz.cvut.fel.pjv.game.controller.Music;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class End {
    private final double x,y,width,height;
    private final Font font = Font.loadFont("file:src/main/resources/fonts/font.ttf",30);

    private final GraphicsContext gf;
    private final Pane pane;
    private final Stage stage;

    /**
     * Constructor for Endgame notification
     * @param gf
     * graphics context for drawing the notification
     * @param pane
     * Pane used in game, for adding buttons
     * @param stage
     * stage used by javafx
     */
    public End(GraphicsContext gf, Pane pane, Stage stage) {
        this.gf = gf;
        this.pane = pane;
        this.stage = stage;
        x = 280;
        y = 175;
        width = 325;
        height = 200;
    }

    /**
     * Creates endgame alert menu
     * @param endText
     * Text that is rendered in alert
     */
    public void create(String endText){

        Label label = null;
        if (endText.equals("VICTORY")){
            label = createLabelWin(endText);
        }else{
            label = createLabelLost(endText);
        }

        createBackground();
        Button menu = createMenuButton();
        menu.setOnAction(actionEvent -> {
            new Thread(Music.BTN_CLICK).start();
            new Menu(stage).start();
        });

        pane.getChildren().add(label);
        pane.getChildren().add(menu);
    }

    /**
     * Creates menu button
     * @return
     * Returns menu button
     */
    public Button createMenuButton(){
        Button menu = new Button("Menu");
        menu.setLayoutX(x + 55);
        menu.setLayoutY(y + 95);
        menu.setFont(font);
        menu.setPrefSize(225,35);
        gf.strokeRect(x+54,y+94, 227, 65);
        return menu;
    }

    /**
     * Creates background of alert
     */
    public void createBackground(){
        gf.setFill(Color.rgb(224,224,224));
        gf.fillRect(x, y, width,height);
        gf.strokeRect(x,y,width,height);
    }

    /**
     * Creates lose game label for notification
     * @param text
     * Text that will be written in label
     * @return
     * returns lose label
     */
    public Label createLabelLost(String text){
        Label label = new Label(text);
        label.setLayoutX(x+55);
        label.setLayoutY(y+40);
        label.setFont(font);
        return label;
    }

    /**
     * Creates win game
     * label for notification
     * @param text
     * Text that will be written in label
     * @return
     * returns win label
     */
    public Label createLabelWin(String text){
        Label label = new Label(text);
        label.setLayoutX(x+80);
        label.setLayoutY(y+40);
        label.setFont(font);
        return label;
    }
}
