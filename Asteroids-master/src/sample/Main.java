

package sample;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Main.java
 * Clase principal de nuestro programa, que lanza el juego y clases asociadas.
 * Wilson Sanchez
 */
public class Main extends Application {

    public void start(Stage primaryStage) throws Exception{
        MenuController.setPreviousStage(primaryStage);
        MenuController.setCurrentStage(primaryStage);
        primaryStage.setTitle("Asteroids Menu");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("menu.fxml"));
        Pane myPane = loader.load();
        MenuController controller = loader.getController();
        myPane.setStyle("-fx-background-image: url('sample/img/asteroidsmenu.png')");
        controller.setCurrentStage(primaryStage);
        Scene myScene = new Scene(myPane);
        primaryStage.setScene(myScene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}

