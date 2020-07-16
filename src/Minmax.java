import java.util.*;

public class Minmax {

    private Arbre tree;

    public Minmax(Noeud root){
        tree = new Arbre();
        tree.setRacine(root);
    }

    public Noeud minimax(Jeu jeu, Noeud noeud, int profondeur, int alpha, int beta, boolean joueurRouge) {
        ArrayList<Integer[][]> enfants = new ArrayList<Integer[][]>();
        int eval;
        int minEval;
        int maxEval;
        Noeud noeudEnfant = new Noeud(noeud.getBoard(), noeud.getScore());

        if(joueurRouge) {
            enfants = jeu.generateurMouvement(noeud.getBoard(), jeu.getPionsRouges());
        }
        else {
            enfants = jeu.generateurMouvement(noeud.getBoard(), jeu.getPionsNoirs());
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
            int valeur = jeu.getValue(noeud.getBoard());
            noeud.setScore(valeur);
            return noeud;
        }

        if (joueurRouge) {
            maxEval = -1000000000;
            for (Integer[][] enfant : enfants) {
                noeudEnfant = new Noeud(enfant, maxEval);
                Noeud noeudMax = minimax(jeu, noeudEnfant, profondeur - 1, alpha, beta, false);
                eval = noeudMax.getScore();

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

                minEval = Math.min(minEval, eval);

                alpha = Math.max(alpha, noeudMin.getScore());

                if (beta <= alpha) {
                    break;
                }
            }
            return (new Noeud(noeudEnfant.getBoard(), minEval));
        }
    }

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
        currentPosition += conversionChiffreEnLettre(oldY);
        currentPosition += conversionChiffreEnChiffre(oldX);

        newPosition += conversionChiffreEnLettre(newY);
        newPosition += conversionChiffreEnChiffre(newX);

        return currentPosition + " - " + newPosition;
    }

    public String conversionChiffreEnLettre(int chiffre) {
        String str = "";
        switch (chiffre){
            case -1:
                //No position has been chosen
                break;
            case 0:
                str += "A";
                break;
            case 1:
                str += "B";
                break;
            case 2:
                str += "C";
                break;
            case 3:
                str += "D";
                break;
            case 4:
                str += "E";
                break;
            case 5:
                str += "F";
                break;
            case 6:
                str += "G";
                break;
            case 7:
                str += "H";
                break;
        }
        return str;
    }

    public String conversionChiffreEnChiffre(int chiffre) {
        String str = "";
        switch (chiffre){
            case -1:
                //No position has been chosen
                break;
            case 0:
                 str += "8";
                break;
            case 1:
                str += "7";
                break;
            case 2:
                str += "6";
                break;
            case 3:
                str += "5";
                break;
            case 4:
                str += "4";
                break;
            case 5:
                str += "3";
                break;
            case 6:
                str += "2";
                break;
            case 7:
                str += "1";
                break;
        }
        return str;
    }

}
