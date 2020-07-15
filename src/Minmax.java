import java.util.List;

public class Minmax {

    public double minmax(Noeud boardInit, int depth, boolean joueurMax){

        List<Integer> listPossibleMoves = Breakthrough.generateurMouvement(boardInit.getBoard(), joueurMax);

        if(depth == 0){
            return Breakthrough.getValue(boardInit.getBoard());
        }
        if(joueurMax){
            double maxEval = Double.NEGATIVE_INFINITY;
            for(int i=0; i<listPossibleMoves.size(); i++){
                double eval = minmax(boardInit, depth-1, false);
                maxEval = Math.max(maxEval,eval);
            }
            return maxEval;
        }else{
            double minEval = Double.POSITIVE_INFINITY;
            for(int i=0; i<listPossibleMoves.size(); i++){
                double eval = minmax(boardInit, depth-1, true);
                minEval = Math.min(minEval,eval);
            }
            return minEval;
        }
    }



}
