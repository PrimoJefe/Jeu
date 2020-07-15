import java.util.ArrayList;
import java.util.List;

public class Noeud {

    private Integer[][] board;
    private boolean estJoueurMax;
    private int score;
    private List<Noeud> enfants;

    public Noeud(Integer[][] board, int score) {
        this.board = board;
        this.score = score;
    }
    /*public Noeud(Integer[][] position, boolean estJoueurMax){
        this.board = position;
        this.estJoueurMax = estJoueurMax;
        enfants = new ArrayList<>();
    }*/

    public Integer[][] getBoard(){
        return this.board;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean getJoeurMax(){
        return this.estJoueurMax;
    }

    public List<Noeud> getEnfants(){
        return enfants;
    }

    public void ajouterEnfant(Noeud enfant){
        this.enfants.add(enfant);
    }

}
