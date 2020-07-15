import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

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

    public double minMax(Noeud parentNode, int depth, boolean joueurMax) {

        boolean isChildMaxPlayer = !parentNode.estJoueurMax();

        if(depth == 0){
            double valeur = Jeu.getValue(parentNode.getBoard());
            parentNode.setScore(valeur);
            return valeur;
        }
        if(joueurMax){
            double maxEval = Double.NEGATIVE_INFINITY;
            List<Integer[][]> listeMouvementsRouges = Jeu.generateurMouvement(parentNode.getBoard(), Jeu.getPionsRouges());
            for(int i=0; i<listeMouvementsRouges.size(); i++){
                Noeud enfant = new Noeud(listeMouvementsRouges.get(i),isChildMaxPlayer);
                parentNode.ajouterEnfant(enfant);
                double valMax = minMax(enfant,depth-1, isChildMaxPlayer);
                enfant.setScore(valMax);
                maxEval = Math.max(maxEval, valMax);
            }
            return maxEval;
        } else{
            double minEval = Double.POSITIVE_INFINITY;
            List<Integer[][]> listeMouvementsRouges = Jeu.generateurMouvement(parentNode.getBoard(), Jeu.getPionsRouges());
            for(int i=0; i<listeMouvementsRouges.size(); i++){
                Noeud enfant = new Noeud(listeMouvementsRouges.get(i),isChildMaxPlayer);
                parentNode.ajouterEnfant(enfant);
                double valMin = minMax(enfant,depth-1, isChildMaxPlayer);
                enfant.setScore(valMin);
                minEval = Math.min(minEval, valMin);
            }
            return minEval;
        }
    }

    public Noeud trouverBestEnfant(Noeud parentNode, int depth, boolean joueurMax){

        List<Noeud> enfants = parentNode.getEnfants();
        double nextVal = minMax(parentNode, depth, joueurMax);
        Noeud noeudAJouer = null;

        for (Noeud enfant : enfants) {
            if(enfant.getScore() == nextVal){
                noeudAJouer = enfant;
            }
        }
        return noeudAJouer;
    }

    public String findPositionChange(Noeud parentNode, int depth, boolean joueurMax){
        String currentPosition = "";
        String newPosition = "";
        int oldX=-1,oldY=-1, newX=-1, newY=-1;

        Noeud noeudNextMove = trouverBestEnfant(parentNode, depth, joueurMax);
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
