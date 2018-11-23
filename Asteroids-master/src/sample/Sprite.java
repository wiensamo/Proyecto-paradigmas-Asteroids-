

package sample;


import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;

/**
 * Sprite.java
 * a clase Sprite proporciona métodos para recuperar y establecer propiedades de sprite en el juego
 * Wilson Sanchez
 */
public abstract class Sprite extends Group {
    // Además del nombre y la velocidad, cada Sprite también tiene una posición y el tamaño. Esos atributos forman parte de la superclase del Grupo.
    private String name;
    private Point2D velocity;
    private Model model;

    public Sprite() {
    }

    /**
     * setModel -- establece el modelo 
     */
    public void setModel(Model model){
        this.model = model;
    }

    /**
     * getName -- obtiene el nombre
     */
    public String getName() {
        return this.name;
    }

    /**
     * setName --establece el nombre
     * @param newName
     */
    public void setName(String newName) {
        this.name = newName;
    }

    /**
     * getPosition -- obtierne las posiciones 
     */
    public Point2D getPosition() {
        Point2D position = new Point2D(this.getLayoutX(), this.getLayoutY());
        return position;
    }

    /**
     * setPosition -- establece la posicion 
     * @param x new x
     * @param y new y
     */
    public void setPosition(double x, double y) {
        this.setLayoutX(x);
        this.setLayoutY(y);
    }

    /**
     * getBounds -- gets the outer bounds of this object
     * este objeto en particular (cada esquina de la caja que rodea un objeto de juego)
     */
    public BoundingBox getBounds(){
        Point2D center = this.getPosition();
        Point2D size = this.getSize();
        double minX = center.getX()-0.5*size.getX();
        double minY = center.getY()-0.5*size.getY();
        return new BoundingBox(minX, minY, size.getX(), size.getY());
    }

    /**
     * getVelocity() -- ontiene la velocidad 
     */
    public Point2D getVelocity() {
        return this.velocity;
    }

    /**
     * setVelocity -- establece la velocidad 
     */
    public void setVelocity(double vx, double vy) {
        this.velocity = new Point2D(vx, vy);
    }

    /**
     * getSize-- optiene el tamaño
     */
    public Point2D getSize() {
        Bounds bounds = this.getLayoutBounds();
        Point2D size = new Point2D(bounds.getWidth(), bounds.getHeight());
        return size;
    }

    /**
     * setSize -- establece el tamañpo

     */
    public void setSize(double width, double height) {
        this.resize(width, height);
    }

    /**
     * Mueve el Sprite un paso en la dirección y magnitud de su velocidad. Las subclases pueden anular este método y luego realice las acciones adicionales que desee
     */
    public void step() {
        Point2D position = this.getPosition();
        this.setPosition(position.getX() + this.velocity.getX(), position.getY() + this.velocity.getY());
    }


    /**
     *reprodice el sonido de colicion
     */
    abstract public void makeSound();
}

