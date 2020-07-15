import com.sun.tools.corba.se.idl.constExpr.Negative;

import java.util.ArrayList;
import java.util.List;

public class Minmax {

    Arbre arbre;

    public void construireArbre(Integer[][] board){
        arbre = new Arbre();
        Noeud racine = new Noeud(board, true);
        arbre.setRacine(racine);
        construireArbre(racine);
    }

    private void construireArbre(Noeud racine){

        //Need to get all possible moves from current board
        //Iterate through all moves and add as child in tree
            //If not leaf, continue descending tree

        //List<Integer> listPossibleMoves = Breakthrough.generateurMouvement(Client.);
    }

    public int minmax(Integer[][] board, int depth, boolean joueurMax){
        if(depth == 0){
            return Breakthrough.getValue(board);
        }
        if(joueurMax){
            double maxEval = Double.NEGATIVE_INFINITY;
            for(int i=depth; i<=0; i--){

            }
        }else{
            double minEval = Double.POSITIVE_INFINITY;
        }
        return 0;
    }

}
