import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Noeud {

    private HashMap<Point, Case> cases;
    private int score;
    private ArrayList<Pion> pionsIn;
    private ArrayList<Pion> pionsOut;

    public Noeud(HashMap<Point, Case> cases, int score) {
        this.cases = cases;
        this.score = score;
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
}
