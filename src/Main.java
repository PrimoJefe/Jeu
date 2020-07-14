import java.util.ArrayList;

public class Main {


    public static void main(String[] args) {
        Jeu jeu = new Jeu("12222222222222222000000000000000000000000000000004444444444444444");
        System.out.println("Ma couleur : " + jeu.getMaCouleur());
        jeu.afficherPlateau(jeu.getPlateau());
        System.out.print("\n");

        ArrayList<Integer[][]> liste = new ArrayList<Integer[][]>();

        for(Pion pion : jeu.getMesPions()) {
            if(pion.getPosition().x == 6 && pion.getPosition().y == 0) {
                liste = generateurMouvement(jeu.getPlateau(), pion.getPosition().x, pion.getPosition().y, pion);
            }
        }

        for(int i = 0; i < liste.size(); i ++) {
            jeu.afficherPlateau(liste.get(i));
            System.out.print("\n");
        }

        GetValue(jeu.getPlateau(),"Black");

    }

    // 2 = noir (MIN)  4 = rouge (MAX)
    // [ligne][column]
    // [0][0] = [8][A]

    private static int GetValue(Integer[][]board, String joueur){
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
                    value += GetPieceValue(board,x, y,4);
                    if(y ==0){victoireRouge = true;}
                    //if(y == 0){Value += HomeGroundValue;}
//                    if {(column > 0) ThreatA = (board[GetPosition(y - 1, 7).NoPieceOnSquare);}
//                    if (column < 7) ThreatB = (board.GetPosition(y + 1, 7).NoPieceOnSquare);
//                    if (ThreatA && ThreatB) // almost win
//                        board.Value += PieceAlmostWinValue;
                } else{ // NOIR MIN
                    // comme rouge
                    piecesNoir++;
                    value += GetPieceValue(board,x, y,2);
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

    private static int GetPieceValue(Integer[][]board, int row,int column,int team)
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

    public static ArrayList<Integer[][]> generateurMouvement(Integer[][] plateau, int ligne, int colonne, Pion pion) {
        Integer[][] plateauPossible = copierTableau(plateau);

        int avancement = ligne + pion.getDirection();

        ArrayList<Integer[][]> mouvementsPossibles = new ArrayList<Integer[][]>();

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
            else if (plateau[avancement][colonne - 1] != pion.getCouleur()) {
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
            else if (plateau[avancement][colonne + 1] != pion.getCouleur()) {
                plateauPossible[avancement][colonne + 1] = plateau[ligne][colonne];
                plateauPossible[ligne][colonne] = 0;
                mouvementsPossibles.add(plateauPossible);
                plateauPossible = copierTableau(plateau);
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
