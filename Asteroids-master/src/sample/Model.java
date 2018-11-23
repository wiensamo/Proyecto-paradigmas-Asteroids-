

package sample;


import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Model.java
 * Administra el estado actual del juego  y proporciona información al controlador para su modificación.
 * Wilson Sanchez
 */
public class Model {

    private static ArrayList asteroids;
    private static Spaceship spaceship;
    private static ArrayList bullets;
    private static Scoreboard scoreboard;
    private static ArrayList explosions;
    private double screenWidth;
    private double screenHeight;

    /**
     * Model Constructor
     * Creará un estado inicial de juego en ausencia de uno.
     */
    public Model(double screenWidth, double screenHeight) {
        bullets = new ArrayList<Bullet>();
        asteroids = new ArrayList<Asteroid>();
        scoreboard = new Scoreboard();
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
    }

    /**
     * checkGameCollisions -- comprueba si hay colisiones.
     * tipo El tipo de colisión a verificar ("asteroide de bala" o "asteroide de nave espacial")
     */
    public ArrayList<Sprite> checkGameCollisions(String type, Spaceship instantiatedShip){

        int bulletListLength = bullets.size();
        int asteroidListLength = asteroids.size();
        ArrayList<Sprite> collidedSprites = new ArrayList<Sprite>();
        //System.out.println(bulletListLength);
        for (int i = 0; i < asteroidListLength; i++) {
            Asteroid asteroid = (Asteroid) asteroids.get(i);
            if (type.equals("bullet-asteroid")) {

                for (int j = 0; j < bulletListLength; j++) {
                    Bullet bullet = (Bullet) bullets.get(j);
                    if (collided(asteroid, bullet)) {
                        collidedSprites.add(bullet);
                        collidedSprites.add(asteroid);

                    }
                }

            } else if (type.equals("spaceship-asteroid")) {
                if (collided(asteroid, instantiatedShip)) {
                    collidedSprites.add(instantiatedShip);
                    collidedSprites.add(asteroid);

                }
            }
        }
        return collidedSprites;
    }

    /**
     * collided -- Comprueba si un particular sprites dos colisionó.
     * boolean true si los sprites chocaron, false si los sprites no chocaron
     */
    public boolean collided(Asteroid asteroid, Sprite sprite2){
        BoundingBox asteroidBounds = asteroid.getBounds();
        BoundingBox sprite2Bounds = sprite2.getBounds();
        BoundingBox asteroidSmallerBox = new BoundingBox (asteroidBounds.getMinX()*1.1,
                asteroidBounds.getMinY()*1.1, asteroidBounds.getWidth()*.9, asteroidBounds.getHeight()*.9);
        if (asteroidSmallerBox.intersects(sprite2Bounds)){
            return true;
        }

        return false;



    }

    /**
     * generateAsteroid -- crea una nueva instancia de asteroide y la agrega a la asteroidList actual.
     */
    public Asteroid generateAsteroid(){
       Asteroid newAsteroid = new Asteroid();
       asteroids.add(newAsteroid);
       return newAsteroid;
    }

    /**
     * generateBullet -- crea una nueva instancia de Bullet y la agrega a la bulletList actual.
     */
    public Bullet generateBullet(){
        Bullet newBullet = new Bullet();
        bullets.add(newBullet);
        return newBullet;
    }


    /**
     * createNewShip -- crea una nueva instancia de Spaceship en caso de destrucción debido a colisión o inicialización del juego.
     */
    public void createNewShip(){
        spaceship = new Spaceship();
    }



    /**
     * getSpaceShip -- Obtiene la instancia actual de Spaceship en el modelo.
     */
    public Spaceship getSpaceship(){
        return new Spaceship();
    }



    /**
     * getAsteroidList -- Obtiene la lista actual de asteroides en el modelo.
     */
    public List getAsteroidList(){
        return asteroids;
    }

    /**
     * getBulletList -- Obtine la lista de BulletList actual
     */
    public List getBulletList(){
        return bullets;
    }

    /**
     * getScoreboard -- Establece el marcador actual en el modelo
     */
    public Scoreboard getScoreboard(){
        return scoreboard;
    }

    /**
     * createScoreboard --  crea el  Scoreboard.
     */
    public void createScoreboard(){
        scoreboard = new Scoreboard();
    }


    /**
     * updateLives --actualiza las vidas actuales en función de otros activadores de métodos, por ejemplo, colisión.
     */
    public void updateLives(int lives){
        int currentLives = scoreboard.getLives();
        scoreboard.setLives(currentLives + lives);
    }

    /**
     * getLives -- lleva el numero  de vidas cvon las que aun cuenta el  jugador
     * @return number of lives
     */
    public int getLives(){
        return scoreboard.getLives();
    }

    /**
     * updateScore -- actualiza el puntaje actual en función de los factores desencadenantes (tiempo de supervivencia, colisión con asteroides)
     */
    public void updateScore(int points){
        int currentScore = scoreboard.getScore();
        if (currentScore + points < 0){
            scoreboard.setScore(0);
        }else{
            scoreboard.setScore(currentScore + points);
        }

    }
    /**
     * quita los astoroides del  juego
     */
    public void removeAsteroid(Asteroid asteroid){
        asteroids.remove(asteroid);

    }

    /**
     * quita las balas del  juego
     */
    public void removeBullet(Bullet bullet){
        bullets.remove(bullet);

    }



}
