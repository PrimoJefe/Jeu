import java.awt.*;

public class Pion {

    private static final int NOIR = 2;
    private static final int ROUGE = 4;

    private int couleur;
    private Point position;
    private int direction;

    public Pion (int couleur, Point position, int direction) {
        this.couleur = couleur;
        this.position = position;
        this.direction = direction;
    }

    public int getCouleur() {
        return this.couleur;
    }

    public Point getPosition() {
        return this.position;
    }

    public int getDirection() {
        return this.direction = direction;
    }

}
