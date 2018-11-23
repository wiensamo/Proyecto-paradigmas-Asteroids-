

package sample;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;

import java.util.Random;


/**
 * Asteroid.java
 * Construye un asteroide para nuestro juego. Los métodos incluyen get / setPosition, get / setVelocity, get / setRadius
 * Wilson Sanchez
 */
public class Asteroid extends Sprite {

    private Point2D velocity;
    private double radius;
    private Image image;
    private ImageView imageView;
    private int screenHeight = 700;
    private int screenWidth = 1200;
    private float id;
    private AudioClip audioClip;


    /**
     * Constructor
     */
    public Asteroid(){
        this.setName("asteroid");
        //
        Random r = new Random();
        id = r.nextFloat();

        //Establece un radio aleatorio para que el tamaño del asteroide varíe.
        this.radius = r.nextDouble()*60+60;
        double yCoord = r.nextDouble()*(screenHeight - 2*this.radius) + this.radius;

        //Establecer una velocidad aleatoria.
        double randVelocity = id * -18;
        this.velocity = new Point2D(randVelocity, 0);
        this.setVelocity(randVelocity,0);


        this.setPosition(screenWidth, yCoord);
        this.image = new Image(getClass().getResourceAsStream("/sample/img/asteroid.png"), this.radius, this.radius, true, false);
        this.imageView = new ImageView();
        imageView.setImage(image);
        this.getChildren().add(imageView);
        this.audioClip = new AudioClip(getClass().getResource("sounds/explosion.mp3").toString());

    }


    public void setSize(double width, double height) {
        super.setSize(width, height);
        this.getChildren().remove(imageView);
        image = new Image(getClass().getResourceAsStream("/sample/img/asteroid.png"), width, height, true,false);
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        this.getChildren().add(imageView);
    }

    /**
     * getRadius -- retorna el radio del asteroides
     */
    public double getRadius(){
        return this.radius;
    }


    /**
     * setRadius -- estableceel radio del asteroide
     */
    public void setRadius(double newRadius){
        this.radius = newRadius;
        this.setSize(newRadius, newRadius);
    }

    /**
     * step -- establece la posicion de acuerdo  con la velocidad
     */
    public void step() {
        double xPos = this.getPosition().getX();
        double yPos = this.getPosition().getY();
        double xVel = this.velocity.getX();
        double yVel = this.velocity.getY();
        this.setPosition(xPos+xVel, yPos+yVel);
    }

    /**
     * makeSound -- Haz sonar una explosión cuando los objetos chocan contra este asteroide.
     */
    @Override
    public void makeSound(){
        this.audioClip.play();
    }



}
