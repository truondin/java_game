package cz.cvut.fel.pjv.game.view;



import cz.cvut.fel.pjv.game.view.state.Menu;
import javafx.application.Application;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class Main extends Application {



    @Override
    public void start(Stage stage){

        Menu menu = new Menu(stage);
        menu.start();

    }

    public static void main(String[] args) {
        launch();
    }

}