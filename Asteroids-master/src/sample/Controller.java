

package sample;

import javafx.application.Platform;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Controller.java
 * Manipula y crea elementos del juego en el juego Asteroids.
 * Wilson Sanchez
 */
public class Controller implements EventHandler<KeyEvent> {
    @FXML public Group spaceshipGroup;
    @FXML public Group asteroidGroup;
    @FXML public Group bulletGroup;
    public Group scoreboardGroup;
    private static Model spaceModel;
    final private double screenWidth = 1200;
    final private double screenHeight= 700;
    @FXML private Spaceship spaceship;
    private static AudioClip gameMusic;
    private static Stage previousStage;
    private static Stage currentStage;

    //Variable para la musica
    private boolean wantMusicOn;

    //Variables para controlar el tiempo
    private Timer timer;
    private Timer cleanupTimer;

    //Variables para controlar las muertes
    private boolean invincible;
    private int invincibleCount;

    final private double framesPerSecond = 20.0;

    public Controller(){

    }

    /**
     * initialize -- Inicializa el juego los elementos iniciales del juego, incluida la música, e inicia el temporizador de animación.
     */
    public void initialize() {
        spaceModel = new Model(this.screenWidth, this.screenHeight);
        this.gameMusic = new AudioClip(getClass().getResource("sounds/music.mp3").toString());
        this.gameMusic.play();
        this.wantMusicOn=true;
        this.invincible=false;

        initScore();
        this.setUpAnimationTimer();
    }

    /**
     * setPreviousStage -- Establece la etapa anterior del programa para cerrar las siguientes ventanas
     */
    public static void setPreviousStage(Stage stage){
        previousStage = stage;
    }

    /**
     * setCurrentStage -- Establece la etapa actual del juego para su posterior cierre.
     */
    public static void setCurrentStage(Stage stage){
        currentStage = stage;
    }

    /**
     * gotoMenu -- va al menú de inicio del juego cuando ocurren ciertos disparadores (muerte, posiblemente otras funciones eventualmente)
     */
    public void gotoMenu() throws IOException {
        setPreviousStage(currentStage);
        Stage primaryStage = new Stage();
        MenuController.setCurrentStage(primaryStage);
        primaryStage.setTitle("Asteroids Menu");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("menu.fxml"));

        Pane myPane = loader.load();
        MenuController controller = loader.getController();

        myPane.setStyle("-fx-background-image: url('sample/img/asteroidsmenu.png')");

        Scene myScene = new Scene(myPane);
        primaryStage.setScene(myScene);

        previousStage.close();

        primaryStage.show();
    }

    /**
     * setUpAnimationTimer -- comienza los temporizadores para animaciones
     * También establece algunas tareas de temporizador que ocurrirán intermitentemente durante el curso del juego.
     */
    private void setUpAnimationTimer() {
        TimerTask timerTask = new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                            updateAnimation();

                    }
                });
            }
        };
        //establece una tarea de temporizador para verificar si hay objetos para limpiar.
        TimerTask cleanUpTask = new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        cleanUpObjects();

                    }
                });
            }
        };
        //establece un temporizador para los asteroides
        TimerTask asteroidGeneration = new TimerTask(){
            public void run(){
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                       makeAsteroids();

                    }
                });
            }
        };
        //establece un un temporizador para verificar las coliciones
        TimerTask collisionTask = new TimerTask(){
            public void run(){
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        checkGameCollisions();

                    }
                });
            }
        };
        //establece si la música está actualmente activada.
        TimerTask musicPlayer = new TimerTask(){
            public void run(){
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        checkMusic();

                    }
                });
            }
        };


        long frameTimeInMilliseconds = (long)(1000.0 / framesPerSecond);
        this.timer = new java.util.Timer();
        this.cleanupTimer = new Timer();
        this.timer.schedule(timerTask, 0, frameTimeInMilliseconds);
        this.cleanupTimer.schedule(cleanUpTask, 100, frameTimeInMilliseconds);
        this.cleanupTimer.schedule(collisionTask, 10, 50);
        this.timer.scheduleAtFixedRate(musicPlayer, 0, 10000);
        this.timer.scheduleAtFixedRate(asteroidGeneration, 0, 750);
    }

    /**
     * updateAnimation -- Llama a funciones para manejar métodos de pasos para el movimiento de objetos.
     */
    private void updateAnimation() {

        this.spaceship.step();
        if (this.asteroidGroup.getChildren().size()>0) {
            for (Node child : this.asteroidGroup.getChildren()) {
                Asteroid asteroid = (Asteroid) child;
                asteroid.step();
            }
        }

        for (Node child : this.bulletGroup.getChildren()) {
            Bullet bullet = (Bullet) child;
            bullet.step();

        }

    }

    /**
     * makeAsteroids -- genera asteroides en el modelo y los inserta en la vista para usarlos en el juego.
     */
    public void makeAsteroids(){
        Asteroid newAsteroid = this.spaceModel.generateAsteroid();
        try{
            this.asteroidGroup.getChildren().add(newAsteroid);
        }
        catch (Exception e){

        }

    }

    /**
     * checkMusic -- comprueba que la música del juego  si  se está reproduciendo actualmente
     * se ocupa de que el audio llegue al final de la pista
     */
    private void checkMusic(){
        if (!this.gameMusic.isPlaying()&& this.wantMusicOn){
            this.gameMusic.play();
        }
    }

    /**
     * toggleMusic -- Alterna la musica del juego 
     */
    public void toggleMusic(){
        if (this.gameMusic.isPlaying()){
            this.wantMusicOn=false;
            this.gameMusic.stop();
        }
        else{
            this.wantMusicOn=true;
            this.gameMusic.play();
        }
    }

    /**
     * checkGameCollisions -- comprueba el juego para detectar colisiones entre objetos y luego llama a los métodos asociados requeridos para la limpieza.
     */
    public void checkGameCollisions(){
        try {
            ArrayList collidedBAs = spaceModel.checkGameCollisions("bullet-asteroid", this.spaceship);

            //verifica las colisiones de asteroides de bala. Si la función anterior devolvió una colisión, llama a las funciones de eliminación para limpiar.
            if (collidedBAs.size() != 0) {
                for (int i = 0; i < collidedBAs.size(); i += 2) {
                    Bullet deadBullet= (Bullet) collidedBAs.get(i);
                    bulletGroup.getChildren().remove(deadBullet);
                    spaceModel.removeBullet(deadBullet);
                    Asteroid deadAsteroid = (Asteroid) collidedBAs.get(i+1);
                    deadAsteroid.makeSound();
                    asteroidGroup.getChildren().remove(deadAsteroid);
                    spaceModel.removeAsteroid(deadAsteroid);
                    this.updateScore(50);
                }
            }
        } catch (Exception e) {

        }

        // Si no es invencible actualmente debido a reaparición reciente, mata a la nave y al asteroide.
        if (!this.invincible) {
            ArrayList collidedSAs = spaceModel.checkGameCollisions("spaceship-asteroid", this.spaceship);
            if (collidedSAs.size() != 0) {

                //only one asteroid can hit the ship at a time
                explodeTheShip(collidedSAs);
                updateScore(-500);

            }
        }
        //Si actualmente es invencible porque justo después de reaparecer, evita que los asteroides golpeen la nave espacial
        else{
            this.invincibleCount++;
            if (this.invincibleCount>50){
                this.invincible=false;
                this.invincibleCount=0;
            }

        }
    }

    /**
     * explodeTheShip -- dado que la función checkGameCollisions encuentra que hubo una colisión entre la nave espacial y el asteroide,
     * esta función actualiza las vidas, y llama a funciones para animaciones y limpieza asociadas.
     */
    private void explodeTheShip(ArrayList<Sprite> collidedSAs){
        this.invincible=true;
        spaceModel.updateLives(-1);
        Asteroid deadAsteroid = (Asteroid) collidedSAs.get(1);
        asteroidGroup.getChildren().remove(deadAsteroid);
        spaceModel.removeAsteroid(deadAsteroid);
        if (spaceModel.getLives()>0) {
            //exploding sound! BOOM
            spaceship.makeSound();
        }
        else if (spaceModel.getLives()==0){
            spaceshipGroup.getChildren().remove(collidedSAs.get(0));
            try{
                this.gameMusic.stop();
                this.timer.cancel();
                this.cleanupTimer.cancel();
                gotoMenu();
            }catch (IOException exception){}

        }

    }

    /**
     * updateScore --Actualiza el marcador en el modelo y en el juego.
     */
    private void updateScore(int value){
        spaceModel.updateScore(value);
        spaceModel.getScoreboard().getScoreLabel().setText(String.format("Score: %d\nLives: %d", spaceModel.getScoreboard().getScore(),
                                                                                                 spaceModel.getScoreboard().getLives()));
        scoreboardGroup.getChildren().remove(spaceModel.getScoreboard().getScoreLabel());
        scoreboardGroup.getChildren().add(spaceModel.getScoreboard().getScoreLabel());
    }

    /**
     * initScore -- inicializa la puntuacion
     */
    private void initScore(){

        spaceModel.getScoreboard().getScoreLabel().setText(String.format("Score: %d\nLives: %d", spaceModel.getScoreboard().getScore(),
                                                                                                 spaceModel.getScoreboard().getLives()));
        scoreboardGroup.getChildren().add(spaceModel.getScoreboard().getScoreLabel());
        scoreboardGroup.getChildren().add(spaceModel.getScoreboard().getInstructions());

    }


    /**
     * handle -- implementa el manejo de pulsaciones de teclas, incluido el movimiento de la nave espacial y el disparo de bala.
     */
    @Override
    public void handle(KeyEvent keyEvent) {
        KeyCode code = keyEvent.getCode();
        double spaceshipYPosition = this.spaceship.getLayoutY();
        double spaceshipXPosition = this.spaceship.getLayoutX();
        double stepSize = 15.0;
        if (code == KeyCode.UP || code == KeyCode.K) {
            // movimiento arriba
            if (spaceshipYPosition > stepSize) {
                this.spaceship.setLayoutY(this.spaceship.getLayoutY() - stepSize);
            } else {
                this.spaceship.setLayoutY(this.screenHeight - this.spaceship.getSize().getY());
            }

        } else if (code == KeyCode.DOWN || code == KeyCode.J) {
            // movimiento abajo
            if (spaceshipYPosition + this.spaceship.getSize().getY() + stepSize < this.screenHeight) {
                this.spaceship.setLayoutY(this.spaceship.getLayoutY() + stepSize);
            } else {
                this.spaceship.setLayoutY(this.screenHeight - this.spaceship.getSize().getY());
            }

        } else if (code == KeyCode.LEFT || code == KeyCode.H) {
            // movimiento a la izquierda
            if (spaceshipXPosition > stepSize) {
                this.spaceship.setLayoutX(this.spaceship.getLayoutX() - stepSize);
            } else {
                this.spaceship.setLayoutX(0);
            }

        } else if (code == KeyCode.RIGHT || code == KeyCode.L) {
            // movimiento a la derecha
            if (spaceshipXPosition + this.spaceship.getSize().getX() + stepSize < this.screenWidth) {
                this.spaceship.setLayoutX(this.spaceship.getLayoutX() + stepSize);
            } else {
                this.spaceship.setLayoutX(this.screenWidth - this.spaceship.getSize().getX());
            }

        }
        else if (code == KeyCode.SPACE) {
            fireBullet();
        }
        else if (code == KeyCode.M){
            toggleMusic();
        }

    }

    /**
     * fireBullet -- dispara una bala desde la nave espacial creando una nueva instancia de Bullet en la ubicación de la nave espacial.
     */
    public void fireBullet(){
        double spaceshipXOffset = spaceship.getSize().getX() - 30;
        double spaceshipYOffset = spaceship.getSize().getY()/2;
        double bulletXVal = spaceship.getPosition().getX() + spaceshipXOffset;
        double bulletYVal = spaceship.getPosition().getY() + spaceshipYOffset;
        Bullet newBullet = spaceModel.generateBullet();
        newBullet.setPosition(bulletXVal, bulletYVal);
        newBullet.makeSound();
        this.bulletGroup.getChildren().add(newBullet);


    }


    /**
     * isAsteroidInScreen -- determina si un asteroide dado sigue siendo visible.
     */
    private boolean isAsteroidInScreen(Asteroid asteroid){
        if (asteroid.getPosition().getX() + asteroid.getRadius() <=0){
            return false;
        }
        return true;
    }
    /**
     * isBulletInScreen -- determina si una bala dada sigue siendo visible.

     */
    private boolean isBulletInScreen(Bullet bullet){
        if (bullet.getPosition().getX() - bullet.getSize().getX() >= this.screenWidth){
            return false;
        }
        return true;
    }

    /**
     * cleanUpObjects -- elimina los objetos que han chocado entre sí, y crea una instancia de los nuevos si es necesario.
     */
    public void cleanUpObjects(){
        try {
            for (Node node : this.asteroidGroup.getChildren()) {
                Asteroid asteroid = (Asteroid) node;
                if (!isAsteroidInScreen(asteroid)) {
                    spaceModel.removeAsteroid(asteroid);
                    this.asteroidGroup.getChildren().remove(asteroid);

                }
            }
        }
        catch (Exception e){}
          try {
              for (Node node : this.bulletGroup.getChildren()) {
                  Bullet bullet = (Bullet) node;
                  if (!isBulletInScreen(bullet)) {
                      spaceModel.removeBullet(bullet);
                      this.bulletGroup.getChildren().remove(bullet);
                  }
              }
          }
          catch (Exception e){}

    }
}
