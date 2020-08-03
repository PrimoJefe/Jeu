import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Noeud {

    private int[][] plateau;
    private int score;
    private ArrayList<Pion> pionsRouges;
    private ArrayList<Pion> pionsNoirs;

    public Noeud(int[][] plateau, int score, ArrayList<Pion> pionsRouges, ArrayList<Pion> pionsNoirs) {
        this.plateau = plateau;
        this.score = score;
        this.pionsRouges = new ArrayList<Pion>(pionsRouges);
        this.pionsNoirs = new ArrayList<Pion>(pionsNoirs);
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

    public ArrayList<Pion> getPionsRouges() {
        return pionsRouges;
    }

    public ArrayList<Pion> getPionsNoirs() {
        return pionsNoirs;
    }
}
