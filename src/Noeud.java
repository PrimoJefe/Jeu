import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Noeud {

    private HashMap<Point, Case> cases;
    private int score;
    private ArrayList<Pion> pionsRouges;
    private ArrayList<Pion> pionsNoirs;

    public Noeud(HashMap<Point, Case> cases, int score, ArrayList<Pion> pionsRouges, ArrayList<Pion> pionsNoirs) {
        this.cases = cases;
        this.score = score;
        this.pionsRouges = new ArrayList<Pion>(pionsRouges);
        this.pionsNoirs = new ArrayList<Pion>(pionsNoirs);
    }

    public HashMap<Point, Case> getCases(){
        return this.cases;
    }

    public int getScore(){
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public ArrayList<Pion> getpionsRouges() {
        return pionsRouges;
    }

    public ArrayList<Pion> getpionsNoirs() {
        return pionsNoirs;
    }
}
