import java.util.ArrayList;
import java.util.List;

public class Noeud {

    private Integer[][] board;
    private boolean estJoueurMax;
    private double score;
    private List<Noeud> enfants;


    public Noeud(Integer[][] position, boolean estJoueurMax){
        this.board = position;
        this.estJoueurMax = estJoueurMax;
        enfants = new ArrayList<>();
    }

    public Integer[][] getBoard(){
        return this.board;
    }

    public double getScore(){
        return this.score;
    }

    public boolean getJoueurMax(){
        return this.estJoueurMax;
    }

    public List<Noeud> getEnfants(){
        return enfants;
    }

    public void ajouterEnfant(Noeud enfant){
        this.enfants.add(enfant);
    }

}
