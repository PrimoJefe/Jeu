import java.awt.*;
import java.util.ArrayList;


public class Jeu {

    //private static final int NOIR = 2;
    //private static final int ROUGE = 4;
    private static final int A = 0;
    private static final int B = 1;
    private static final int C = 2;
    private static final int D = 3;
    private static final int E = 4;
    private static final int F = 5;
    private static final int G = 6;
    private static final int H = 7;

    private Integer[][] plateau;
    private boolean maCouleur;
    private boolean couleurAdverse;
    private static ArrayList<Pion> pionsRouges;
    private static ArrayList<Pion> pionsNoirs;

    public Jeu(String configuration) {
        this.plateau = new Integer[8][8];
        this.pionsNoirs = new ArrayList<Pion>();
        this.pionsRouges = new ArrayList<Pion>();

        if (Integer.parseInt(String.valueOf(configuration.charAt(0))) == 1) {
            this.maCouleur = true;
            this.couleurAdverse = false;
        }
        else {
            this.maCouleur = false;
            this.couleurAdverse = true;
        }

        int i = 1;
        int direction = 1;
        for (int x = 0; x < this.plateau.length; x++) {
            for (int y = 0; y < this.plateau.length; y++) {
                int valeur = Integer.parseInt(String.valueOf(configuration.charAt(i)));
                this.plateau[x][y] = valeur;
                Point position = new Point(x,y);

                if (x > (plateau.length-1)/2) {
                    direction = -1;
                }

                if (valeur == 4) {
                    Pion pion = new Pion(true, position, direction);
                    pionsRouges.add(pion);
                }
                else if (valeur == 2) {
                    Pion pion = new Pion(false, position, direction);
                    pionsNoirs.add(pion);
                }
                i++;
            }
        }
    }

    public Integer[][] getPlateau() {
        return this.plateau;
    }
    public boolean getMaCouleur() { return this.maCouleur; }

    public static ArrayList<Pion> getPionsRouges() {
        return pionsRouges;
    }

    public static ArrayList<Pion> getPionsNoirs() {
        return pionsNoirs;
    }

    public void setPlateau(Integer[][] nouvelEtat) {
        this.plateau = nouvelEtat;
    }

    public void afficherPlateau(Integer[][] plateau) {
        for(int i = 0; i < plateau.length; i++){
            System.out.print("\n");
            for(int j = 0; j < plateau.length; j++) {
                System.out.print(plateau[i][j]);
            }
        }
    }

    public static int getValue(Integer[][] board){
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

    public static int getPieceValue(Integer[][] board, int row, int column, int team)
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

    public static ArrayList<Integer[][]> generateurMouvement(Integer[][] plateau, ArrayList<Pion> pions) {
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
