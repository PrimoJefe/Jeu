import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Noeud {

    private int[][] plateau;
    private int score;
    private Map<Point, Pion> pionsRouges;
    private Map<Point, Pion> pionsNoirs;

    public Noeud(int[][] plateau, int score, Map<Point, Pion> pionsRouges, Map<Point, Pion> pionsNoirs) {
        this.plateau = plateau;
        this.score = score;
        this.pionsRouges = new HashMap<>(pionsRouges);
        this.pionsNoirs = new HashMap<>(pionsNoirs);
    }

    public int[][] getPlateau(){
        return this.plateau;
    }

    public int getScore(){
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Map<Point, Pion> getPionsRouges() {
        return pionsRouges;
    }

    public Map<Point, Pion> getPionsNoirs() {
        return pionsNoirs;
    }
}
