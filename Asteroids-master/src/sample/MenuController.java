package sample;


import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;

/**
 * MenuController.java
 * Implementación de cambios entre este menú y el juego de asteroides.
 * Wilson Sanchez
 */
public class MenuController implements Initializable{
    private static double screenWidth = 1200;
    private static double screenHeight = 800;
    private static Stage previousStage;
    private static Stage currentStage;


    /**
     * setPreviousStage -- Establece la etapa anterior del programa para cerrar en ventanas.
     */
    public static void setPreviousStage(Stage stage){
        previousStage = stage;
    }

    /**
     * setCurrentStage -- Establece la etapa actual enfocada del juego para su posterior cierre.
     */
    public static void setCurrentStage(Stage stage){
        currentStage = stage;
    }

    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * gotoGame -- inicializa los asteroides del juego
     */
    public void gotoGame(ActionEvent actionEvent) throws IOException {
        previousStage = currentStage;
        Stage stage = new Stage();
        stage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });

        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        final Parent root = (Parent)loader.load();
        Controller controller = loader.getController();

        root.setOnKeyPressed(controller);
        root.setOnMouseClicked(me -> root.requestFocus());

        root.setStyle("-fx-background-image: url('sample/img/spaceBackground.png')");
        stage.setTitle("#STELLAR");
        screenWidth = 1200;
        screenHeight = 800;
        stage.setScene(new Scene(root, screenWidth, screenHeight));
        Controller.setCurrentStage(stage);
        previousStage.close();
        stage.show();
        root.requestFocus();
    }

    /**
     * close -- cierra el  juego
     */
    public void close(){
        System.exit(1);

    }
}
