import java.awt.*;

public class Pion {

    private int couleur;
    private int direction;
    private Point position;

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

    public void setPosition(Point position) {
        this.position = position;
    }

}
