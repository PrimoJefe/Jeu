import java.util.ArrayList;

public class Noeud {

    private int[][] plateau;
    private int score;
    private ArrayList<Pion> pionsRouges;
    private ArrayList<Pion> pionsNoirs;

    /*
     * CONSTRUCTEUR
     */
    public Noeud(int[][] plateau, int score, ArrayList<Pion> pionsRouges, ArrayList<Pion> pionsNoirs) {
        this.plateau = plateau;
        this.score = score;
        this.pionsRouges = new ArrayList<Pion>(pionsRouges);
        this.pionsNoirs = new ArrayList<Pion>(pionsNoirs);
    }

    /*
     * GETTERS
     */
    public int[][] getPlateau(){
        return this.plateau;
    }

    public int getScore(){
        return this.score;
    }

    public ArrayList<Pion> getPionsRouges() {
        return pionsRouges;
    }

    public ArrayList<Pion> getPionsNoirs() {
        return pionsNoirs;
    }

    /*
     * SETTERS
     */
    public void setScore(int score) {
        this.score = score;
    }
}
