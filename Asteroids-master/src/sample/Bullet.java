/**
 * Bullet.java
 * Bullet class -- contiene datos y construye las balas que nuestra nave espacial dispara a los asteroides
 */
package sample;



import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;


public class Bullet extends Sprite {
    private Point2D velocity;
    private double radius;
    private Image image;
    private ImageView imageView;
    private AudioClip audioClip;

    /**
     * Constructor
     */
    public Bullet(){
        this.setName("bullet");
        this.velocity = new Point2D(12, 0);
        this.radius = 10.0;
        this.image = new Image(getClass().getResourceAsStream("/sample/img/laserbeam.png"), this.radius, this.radius, true, false);
        this.imageView = new ImageView();
        imageView.setImage(image);
        this.getChildren().add(imageView);
        this.audioClip = new AudioClip(getClass().getResource("sounds/laser.mp3").toString());
    }

    @Override
    /**
     * step -- establece la posision deacuerdo con la velociudad
     */
    public void step() {
        double xPos = this.getLayoutX();
        double yPos = this.getLayoutY();
        double xVel = this.velocity.getX();
        double yVel = this.velocity.getY();
        this.setPosition(xPos+xVel, yPos+yVel);
    }

    
    /**
     * makeSound -- da el sonido del lazer
     */
    public void makeSound() {
        this.audioClip.play();
    }

}
