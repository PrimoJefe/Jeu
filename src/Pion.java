import java.awt.*;

public class Pion {

    private int couleur;
    private int direction;
    private Point position;

    /*
     * CONSTRUCTEUR
     */
    public Pion (int couleur, Point position, int direction) {
        this.couleur = couleur;
        this.position = position;
        this.direction = direction;
    }

    /*
     * GETTERS
     */
    public int getCouleur() {
        return this.couleur;
    }

    public Point getPosition() {
        return this.position;
    }

    public int getDirection() {
        return this.direction;
    }

    /*
     * SETTERS
     */
    public void setPosition(Point position) {
        this.position = position;
    }
}
