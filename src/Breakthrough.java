import java.util.ArrayList;
import java.util.Random;

public class Breakthrough {

    public Breakthrough() {};

    public Noeud minimax(Jeu jeu, Noeud noeud, int profondeur, int alpha, int beta, boolean joueurRouge) {
        ArrayList<Integer[][]> enfants = new ArrayList<Integer[][]>();
        int eval;
        int minEval;
        int maxEval;
        Integer[][] minBoard = new Integer[8][8];
        Integer[][] maxBoard = new Integer[8][8];
        Noeud noeudEnfant = new Noeud(noeud.getBoard(), noeud.getScore());

        if(joueurRouge) {
            enfants = generateurMouvement(noeud.getBoard(), jeu.getPionsRouges());
        }
        else {
            enfants = generateurMouvement(noeud.getBoard(), jeu.getPionsNoirs());
        }

        if (profondeur == 0) {
            Random r = new Random();
            int low = 0;
            int high = 100;
            int result = r.nextInt(high-low) + low;
            System.out.println("allo : " + result);
            jeu.afficherPlateau(noeud.getBoard());
            System.out.println("\n");
            noeud.setScore(result);
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

//    public int minimax(Jeu jeu, Integer[][] plateau, int profondeur, int alpha, int beta, boolean joueurRouge) {
//        ArrayList<Integer[][]> enfants = new ArrayList<Integer[][]>();
//        int eval;
//        int minEval;
//        int maxEval;
//        if(joueurRouge) {
//            enfants = generateurMouvement(plateau, jeu.getPionsRouges());
//        }
//        else {
//            enfants = generateurMouvement(plateau, jeu.getPionsNoirs());
//        }
//
//        if (profondeur == 0) {
//            Random r = new Random();
//            int low = 0;
//            int high = 100;
//            int result = r.nextInt(high-low) + low;
//            System.out.println("allo : " + result);
//            return result;
//        }
//
//        if (joueurRouge) {
//            maxEval = -1000000000;
//            for (Integer[][] enfant : enfants) {
//                eval = minimax(jeu, enfant, profondeur - 1, alpha, beta, false);
//                maxEval = Math.max(maxEval, eval);
//
//                alpha = Math.max(alpha, eval);
//
//                if (beta <= alpha) {
//                    break;
//                }
//            }
//            return maxEval;
//        }
//
//        else {
//            minEval = 1000000000;
//            for (Integer[][] enfant : enfants) {
//                eval = minimax(jeu, enfant, profondeur - 1, alpha, beta, true);
//                minEval = Math.min(minEval, eval);
//
//                alpha = Math.min(alpha, eval);
//
//                if (beta <= alpha) {
//                    break;
//                }
//            }
//            return minEval;
//        }
//    }

    public int getValue(Integer[][] board){
        int victoire = 100;
        boolean victoireNoir = false;
        boolean victoireRouge = false;
        int piecesNoir = 0;
        int piecesRouges = 0;
        int value = 0;
        for (int x= 0; x < 8; x++){
            for (int y = 0; y< 8; y++){
                if (board[x][y]==0) continue;
                if (board[x][y] ==4) // ROUGE MAX
                {
                    piecesRouges++;
                    value += getPieceValue(board,x, y,4);
                    if(y ==0){victoireRouge = true;}
                    //if(y == 0){Value += HomeGroundValue;}
//                    if {(column > 0) ThreatA = (board[GetPosition(y - 1, 7).NoPieceOnSquare);}
//                    if (column < 7) ThreatB = (board.GetPosition(y + 1, 7).NoPieceOnSquare);
//                    if (ThreatA && ThreatB) // almost win
//                        board.Value += PieceAlmostWinValue;
                } else{ // NOIR MIN
                    // comme rouge
                    piecesNoir++;
                    value += getPieceValue(board,x, y,2);
                    if(y ==7){victoireNoir = true;}
                }
            }
        }
        // if no more material available
        if (piecesRouges == 0) victoireNoir = true;
        if (piecesNoir == 0) victoireRouge = true;

        // winning positions
        if (victoireNoir){value -= victoire;}
        if (victoireRouge){value += victoire;}

        return value;
    }

    public int getPieceValue(Integer[][] board, int row, int column, int team)
    {
        int value = 0;

        // add connections value//

        if (team == 2){ //NOIR MIN
            // add to the value the protected value
            if (row > 0 && column > 0 && column < 7){
                if(board[row-1][column-1]==team){value -= 5;}
                if(board[row-1][column+1]==team){value -= 5;}
            }
            // evaluate attack
            if (row < 7 && column > 0 && column < 7){
                if(board[row+1][column-1]!=team){value += 5;}
                if(board[row+1][column+1]!=team){value += 5;}
            }

            // evaluate block
            if(row <= 5 && column > 0 && column < 7){
                if(board[row+2][column-1]!=team){value -= 5;}
                if(board[row+2][column]!=team){value -= 5;}
                if(board[row+2][column-1]!=team){value -= 5;}
            }

            // mobility feature
            // Value += possibleMoves;

            // evalate distance to goal
            value = value*(row+1);

        }

        else{ //rouge MAX
            // add to the value the protected value
            if (row < 7 && column > 0 && column < 7){
                if(board[row+1][column-1]==team){value += 5;}
                if(board[row+1][column+1]==team){value += 5;}
            }
            // evaluate attack
            if (row > 0 && column > 0 && column < 7){
                if(board[row-1][column-1]!=team){value -= 5;}
                if(board[row-1][column+1]!=team){value -= 5;}
            }


            // evaluate block
            if(row >= 2 && column > 0 && column < 7){
                if(board[row-2][column-1]!=team){value += 5;}
                if(board[row-2][column]!=team){value += 5;}
                if(board[row-2][column-1]!=team){value += 5;}
            }
            // mobility feature
            // Value += possibleMoves;

            // evaluante distance to goal
            value = value*(8-row);
        }
        return value;
    }

    public ArrayList<Integer[][]> generateurMouvement(Integer[][] plateau, ArrayList<Pion> pions) {
        Integer[][] plateauPossible = copierTableau(plateau);
        ArrayList<Integer[][]> mouvementsPossibles = new ArrayList<Integer[][]>();

        for (Pion pion : pions) {
            int ligne = pion.getPosition().x;
            int colonne = pion.getPosition().y;
            int avancement = ligne + pion.getDirection();
            int couleur;

            if (pion.getCouleur() == true) {
                couleur = 4;
            }
            else {
                couleur = 2;
            }

            if (plateau[avancement][colonne] == 0) {
                plateauPossible[avancement][colonne] = plateau[ligne][colonne];
                plateauPossible[ligne][colonne] = 0;
                mouvementsPossibles.add(plateauPossible);
                plateauPossible = copierTableau(plateau);
            }
            if(colonne != 0) {
                if(plateau[avancement][colonne - 1] == 0) {
                    plateauPossible[avancement][colonne - 1] = plateau[ligne][colonne];
                    plateauPossible[ligne][colonne] = 0;
                    mouvementsPossibles.add(plateauPossible);
                    plateauPossible = copierTableau(plateau);
                }
                else if (plateau[avancement][colonne - 1] != couleur) {
                    plateauPossible[avancement][colonne - 1] = plateau[ligne][colonne];
                    plateauPossible[ligne][colonne] = 0;
                    mouvementsPossibles.add(plateauPossible);
                    plateauPossible = copierTableau(plateau);
                }
            }
            if(colonne != plateau.length - 1) {
                if(plateau[avancement][colonne + 1] == 0) {
                    plateauPossible[avancement][colonne + 1] = plateau[ligne][colonne];
                    plateauPossible[ligne][colonne] = 0;
                    mouvementsPossibles.add(plateauPossible);
                    plateauPossible = copierTableau(plateau);
                }
                else if (plateau[avancement][colonne + 1] != couleur) {
                    plateauPossible[avancement][colonne + 1] = plateau[ligne][colonne];
                    plateauPossible[ligne][colonne] = 0;
                    mouvementsPossibles.add(plateauPossible);
                    plateauPossible = copierTableau(plateau);
                }
            }
        }
        return mouvementsPossibles;
    }

    public static Integer[][] copierTableau(Integer[][] plateau) {
        Integer[][] copie = new Integer[plateau.length][plateau.length];
        for (int i = 0; i < plateau.length; i++) {
            for(int j = 0; j < plateau.length; j++) {
                copie[i][j] = plateau[i][j];
            }
        }
        return copie;
    }
}
