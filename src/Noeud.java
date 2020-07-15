import java.util.ArrayList;
import java.util.List;

public class Noeud {

    private Integer[][] board;
    private int depth;
    private String position;
    private boolean estJoueurMax;
    private int score;
    private List<Noeud> enfants;


    public Noeud(Integer[][] board, int score) {
        this.board = board;
        this.score = score;
        enfants = new ArrayList<>();
    }

    boolean estJoueurMax() {
        return this.estJoueurMax;
    }

    public Integer[][] getBoard(){
        return this.board;
    }

    public int getScore(){
        return this.score;
    }

    public boolean getJoueurMax(){
        return this.estJoueurMax;
    }

    public List<Noeud> getEnfants(){
        return enfants;
    }

    public String getPosition(){
        return this.position;
    }

    public int getDepth(){
        return this.depth;
    }

    public void ajouterEnfant(Noeud enfant){
        this.enfants.add(enfant);
    }

    public void setScore(int score){
        this.score = score;
    }

    public void setDepth(int depth){
        this.depth = depth;
    }

    public void setPosition(String position){
        this.position = position;
    }

}
