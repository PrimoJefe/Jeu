import java.util.*;

public class Minmax {

    private Arbre tree;

    public Minmax(Noeud root){
        tree = new Arbre();
        tree.setRacine(root);
    }

    /*public void constructTree(Integer[][] initBoard) {
        tree = new Arbre();
        Noeud root = new Noeud(initBoard, true);
        tree.setRacine(root);
        //constructTree(root, 6, true);
    }

    private Noeud constructTree(Noeud parentNode, int depth, boolean joueurMax) {
        boolean isChildMaxPlayer = !parentNode.estJoueurMax();

        if(depth == 0){
            double valeur = Jeu.getValue(parentNode.getBoard());
            parentNode.setScore(valeur);
            return parentNode;
        }
        if(joueurMax){
            double maxEval = Double.NEGATIVE_INFINITY;
            Noeud maxNoeud = null;
            List<Integer[][]> listeMouvementsRouges = Jeu.generateurMouvement(parentNode.getBoard(), Jeu.getPionsRouges());
            for(int i=0; i<listeMouvementsRouges.size(); i++){
                Noeud enfant = new Noeud(listeMouvementsRouges.get(i),isChildMaxPlayer);
                Noeud valNoeud = constructTree(enfant,depth-1, isChildMaxPlayer);
                maxEval = Math.max(maxEval, valNoeud.getScore());
                if(maxEval == valNoeud.getScore()){
                    maxNoeud = valNoeud;
                }
            }
            return maxNoeud;
        } else{
            List<Integer[][]> listeMouvementsNoirs = Jeu.generateurMouvement(parentNode.getBoard(), Jeu.getPionsNoirs());
            for(int i=0; i<listeMouvementsNoirs.size(); i++){
                Noeud noeudEnfant = new Noeud(listeMouvementsNoirs.get(i), isChildMaxPlayer);
                parentNode.ajouterEnfant(noeudEnfant);
                if(depth > 0){
                    constructTree(noeudEnfant, depth-1, isChildMaxPlayer);
                }
            }
        }
        return checkWin(parentNode);
    }*/

    public Noeud minimax(Jeu jeu, Noeud noeud, int profondeur, int alpha, int beta, boolean joueurRouge) {
        ArrayList<Integer[][]> enfants = new ArrayList<Integer[][]>();
        int eval;
        int minEval;
        int maxEval;
        Integer[][] minBoard = new Integer[8][8];
        Integer[][] maxBoard = new Integer[8][8];
        Noeud noeudEnfant = new Noeud(noeud.getBoard(), noeud.getScore());

        if(joueurRouge) {
            enfants = Jeu.generateurMouvement(noeud.getBoard(), jeu.getPionsRouges());
        }
        else {
            enfants = Jeu.generateurMouvement(noeud.getBoard(), jeu.getPionsNoirs());
        }

        if (profondeur == 0) {
            /*Random r = new Random();
            int low = 0;
            int high = 100;
            int result = r.nextInt(high-low) + low;
            System.out.println("allo : " + result);
            jeu.afficherPlateau(noeud.getBoard());
            System.out.println("\n");
            noeud.setScore(result);*/
            int valeur = Jeu.getValue(noeud.getBoard());
            noeud.setScore(valeur);
            return noeud;
        }

        if (joueurRouge) {
            maxEval = -1000000000;
            for (Integer[][] enfant : enfants) {
                noeudEnfant = new Noeud(enfant, maxEval);
                Noeud noeudMax = minimax(jeu, noeudEnfant, profondeur - 1, alpha, beta, false);
                eval = noeudMax.getScore();
                if (eval > maxEval) {
                    maxBoard = noeudMax.getBoard();
                }
                maxEval = Math.max(maxEval, eval);

                alpha = Math.max(alpha, noeudMax.getScore());

                if (beta <= alpha) {
                    break;
                }
            }
            return (new Noeud(noeudEnfant.getBoard(), maxEval));
        }

        else {
            minEval = 1000000000;
            for (Integer[][] enfant : enfants) {
                noeudEnfant = new Noeud(enfant, minEval);
                Noeud noeudMin = minimax(jeu, noeudEnfant, profondeur - 1, alpha, beta, true);
                eval = noeudMin.getScore();
                if (eval < minEval) {
                    minBoard = noeudMin.getBoard();
                }
                minEval = Math.min(minEval, eval);

                alpha = Math.max(alpha, noeudMin.getScore());

                if (beta <= alpha) {
                    break;
                }
            }
            return (new Noeud(noeudEnfant.getBoard(), minEval));
        }
    }
    /*public Noeud trouverBestEnfant(Noeud parentNode, int depth, boolean joueurMax){

        List<Noeud> enfants = parentNode.getEnfants();
        double nextVal = minMax(parentNode, depth, joueurMax);
        Noeud noeudAJouer = null;

        for (Noeud enfant : enfants) {
            if(enfant.getScore() == nextVal){
                noeudAJouer = enfant;
            }
        }
        return noeudAJouer;
    }*/

    public String findPositionChange(Jeu jeu, Noeud parentNode, int depth, boolean joueurMax){
        String currentPosition = "";
        String newPosition = "";
        int oldX=-1,oldY=-1, newX=-1, newY=-1;

        Noeud noeudNextMove = minimax(jeu, parentNode, depth, -1000000000, 10000000, joueurMax);
        Integer[][] currentMoveBoard = parentNode.getBoard();
        Integer[][] nextMoveBoard = noeudNextMove.getBoard();

        for(int i=0; i<currentMoveBoard.length; i++){
            for(int j=0; j<currentMoveBoard[i].length; j++){
                if(!currentMoveBoard[i][j].equals(nextMoveBoard[i][j]) && nextMoveBoard[i][j].equals(2)
                        || !currentMoveBoard[i][j].equals(nextMoveBoard[i][j]) && nextMoveBoard[i][j].equals(4)){
                    newX = i;
                    newY = j;
                }if(!currentMoveBoard[i][j].equals(nextMoveBoard[i][j]) && nextMoveBoard[i][j].equals(0)){
                    oldX = i;
                    oldY = j;
                }
            }
        }

        switch (oldY){
            case -1:
                //No position has been chosen
                break;
            case 0:
                currentPosition += "A";
                break;
            case 1:
                currentPosition += "B";
                break;
            case 2:
                currentPosition += "C";
                break;
            case 3:
                currentPosition += "D";
                break;
            case 4:
                currentPosition += "E";
                break;
            case 5:
                currentPosition += "F";
                break;
            case 6:
                currentPosition += "G";
                break;
            case 7:
                currentPosition += "H";
                break;
        }

        switch (oldX){
            case -1:
                //No position has been chosen
                break;
            case 0:
                currentPosition += "8";
                break;
            case 1:
                currentPosition += "7";
                break;
            case 2:
                currentPosition += "6";
                break;
            case 3:
                currentPosition += "5";
                break;
            case 4:
                currentPosition += "4";
                break;
            case 5:
                currentPosition += "3";
                break;
            case 6:
                currentPosition += "2";
                break;
            case 7:
                currentPosition += "1";
                break;
        }

        switch (newY){
            case -1:
                //No position has been chosen
                break;
            case 0:
                newPosition += "A";
                break;
            case 1:
                newPosition += "B";
                break;
            case 2:
                newPosition += "C";
                break;
            case 3:
                newPosition += "D";
                break;
            case 4:
                newPosition += "E";
                break;
            case 5:
                newPosition += "F";
                break;
            case 6:
                newPosition += "G";
                break;
            case 7:
                newPosition += "H";
                break;
        }

        switch (newX){
            case -1:
                //No position has been chosen
                break;
            case 0:
                newPosition += "8";
                break;
            case 1:
                newPosition += "7";
                break;
            case 2:
                newPosition += "6";
                break;
            case 3:
                newPosition += "5";
                break;
            case 4:
                newPosition += "4";
                break;
            case 5:
                newPosition += "3";
                break;
            case 6:
                newPosition += "2";
                break;
            case 7:
                newPosition += "1";
                break;
        }

        return currentPosition + " - " + newPosition;
    }


}
