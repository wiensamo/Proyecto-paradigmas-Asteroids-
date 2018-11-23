
package sample;


import javafx.scene.Group;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


 /** Scoreboard.java
 * Scoreboard: construye un marcador para nuestro juego, que incluye la puntuación actual y la vida del jugador.
 * Wilson Sanchez
 */
public class Scoreboard extends Group {

    private int score;
    private int lives;
    private Text scoreLabel;
    private Text instructions;




    /**
     * Constructor
     */
    public Scoreboard(){
        this.score = 0;
        this.lives = 3;

        //Formato para la partitura y vidas en la vista del juego.
        this.scoreLabel=new Text();
        this.scoreLabel.setText(String.format("Score: %d\nLives: %d", this.score, this.lives));
        Font myFont = new Font("Lucida Console", 18);
        this.scoreLabel.setFont(myFont);
        this.scoreLabel.setLayoutX(10);
        this.scoreLabel.setLayoutY(720);
        this.scoreLabel.setLineSpacing(10);
        this.scoreLabel.setFill(Color.LIGHTGREEN);
        this.getChildren().add(this.scoreLabel);


        //Formato  para ver los comandos con ls que se juego  el  juego
        this.instructions=new Text();

        /*  Displayed as:
        Se mueve con las flechas o:
                K               SPACE: Disparaa!
        H               L
                J               
         */
        this.instructions.setText(String.format(
                "Move: Arrow Keys or\n" +
                        "\tK\t\tSPACE: Dispara!\n"+
                        "H\t\tL\n" +
                        "\tJ\t\t"));

        this.instructions.setFont(myFont);
        this.instructions.setLayoutX(720);
        this.instructions.setLayoutY(690);
        this.instructions.setLineSpacing(5);
        this.instructions.setFill(Color.WHITESMOKE);

        this.getChildren().add(this.instructions);

    }


    /**
     * getScore -- Obtiene la puntiacion en el juego
     */
    public int getScore(){
        return this.score;
    }

    /**
     * setScore -- Establece la puntuacion del jugador en el juego
     */
    public void setScore(int score){
        this.score = score;
    }

    /**
     * getLives -- obtiene el número actual de vidas del jugador en el juego.
     */
    public int getLives(){
       return this.lives;
    }

    /**
     * setLives -- establece el número de vidas para el jugador en el juego
     */
    public void setLives(int lives){
        this.lives = lives;
    }

    /**
     * getScoreLabel -- vuelve la etiqueta que proporciona puntuaciones y vidas en la vista.
     */
    public Text getScoreLabel(){ return this.scoreLabel; }

    /**
     * setScoreLabel -- establece la etiqueta en esta clase a uno asignado.
     */
    public void setScoreLabel(Text scoreLabel){ this.scoreLabel = scoreLabel;}

    /**
     * setInstructions -- Establecer las instrucciones para el juego.
     */
    public void setInstructions(Text instructions){ this.instructions = instructions;}

    /**
     * getInstructions -- Obtén las instrucciones del juego para usar en el controlador.
     */
    public Text getInstructions(){
        return this.instructions;
    }
}