import java.awt.*;
import java.util.ArrayList;


public class Jeu {

    private Integer[][] plateau;
    private boolean maCouleur;
    private boolean couleurAdverse;
    private ArrayList<Pion> pionsRouges;
    private ArrayList<Pion> pionsNoirs;

    private int victoire = 500000;
    private int presqueGagne = 10000;
    private int valeurPiece = 1300;
    private int pieceDanger = 10;
    private int pieceGrandDanger = 100;
    private int pieceValeurAttack = 50;
    private int pieceValeurProtection = 65;
    private int pieceConnectionH = 35;
    private int pieceConncectionV = 15;
    private int pieceTrouColonne = 20;
    private int pieceMaison = 10;

    public Jeu() {};
    public Jeu(Integer[][] plateau, boolean maCouleur) {
        /*for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                this.plateau[j][i] = plateau[i][j];
            }
        }*/
        this.plateau = plateau;
        this.maCouleur = maCouleur;
        if(maCouleur) {
            this.couleurAdverse = false;
        }
        else {
            this.couleurAdverse = true;
        }

        this.pionsNoirs = new ArrayList<Pion>();
        this.pionsRouges = new ArrayList<Pion>();

       /* if (Integer.parseInt(String.valueOf(configuration.charAt(0))) == 1) {
            this.maCouleur = true;
            this.couleurAdverse = false;
        }
        else {
            this.maCouleur = false;
            this.couleurAdverse = true;
        }*/

        int i = 1;
        int direction = 1;
        for (int x = 0; x < this.plateau.length; x++) {
            for (int y = 0; y < this.plateau.length; y++) {
                Point position = new Point(x,y);
                int valeur = plateau[x][y];
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
//    public Jeu(String configuration) {
//        /*for(int i = 0; i < 8; i++) {
//            for(int j = 0; j < 8; j++) {
//                this.plateau[j][i] = plateau[i][j];
//            }
//        }*/
//        this.plateau = new Integer[8][8];
//        if(maCouleur) {
//            this.couleurAdverse = false;
//        }
//        else {
//            this.couleurAdverse = true;
//        }
//
//        this.pionsNoirs = new ArrayList<Pion>();
//        this.pionsRouges = new ArrayList<Pion>();
//
//       if (Integer.parseInt(String.valueOf(configuration.charAt(0))) == 1) {
//            this.maCouleur = true;
//            this.couleurAdverse = false;
//        }
//       else {
//            this.maCouleur = false;
//            this.couleurAdverse = true;
//       }
//
//        int i = 1;
//        int direction = 1;
//        for (int x = 0; x < this.plateau.length; x++) {
//            for (int y = 0; y < this.plateau.length; y++) {
//                int valeur = Integer.parseInt(String.valueOf(configuration.charAt(i)));
//                this.plateau[x][y] = valeur;
//                Point position = new Point(x,y);
//                if (x > (plateau.length-1)/2) {
//                    direction = -1;
//                }
//
//                if (valeur == 4) {
//                    Pion pion = new Pion(true, position, direction);
//                    pionsRouges.add(pion);
//                }
//                else if (valeur == 2) {
//                    Pion pion = new Pion(false, position, direction);
//                    pionsNoirs.add(pion);
//                }
//                i++;
//            }
//        }
//    }

    public Integer[][] getPlateau() {
        return this.plateau;
    }
    public boolean getMaCouleur() { return this.maCouleur; }
    public boolean getCouleurAdverse() { return this.couleurAdverse; }

    public ArrayList<Pion> getPionsRouges() {
        return pionsRouges;
    }

    public ArrayList<Pion> getPionsNoirs() {
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

    public int getValue(Integer[][] board){


        boolean victoireNoir = false;
        boolean victoireRouge = false;
        int piecesNoir = 0;
        int piecesRouges = 0;
        int value = 0;
        int homeValue = 0;
        int valeurDanger = 10;


        for (int ligne= 0; ligne < 8; ligne++){
            for (int colonne = 0; colonne< 8; colonne++){
                if (board[ligne][colonne]==0) continue;
                if (board[ligne][colonne] ==4) // ROUGE MAX
                {
                    piecesRouges++;
                    value += getPieceValue(board,ligne, colonne,4);
                    if(ligne ==0){victoireRouge = true;}
                    if(ligne == 7){
                        value += homeValue;
                    }
                    if(ligne == 1 || ligne == 2){value += valeurDanger;}
                    //if(y == 0){Value += HomeGroundValue;}
//                    if {(column > 0) ThreatA = (board[GetPosition(y - 1, 7).NoPieceOnSquare);}
//                    if (column < 7) ThreatB = (board.GetPosition(y + 1, 7).NoPieceOnSquare);
//                    if (ThreatA && ThreatB) // almost win
//                        board.Value += PieceAlmostWinValue;
                } else{ // NOIR MIN
                    // comme rouge
                    piecesNoir++;
                    value += getPieceValue(board,ligne, colonne,2);
                    if(ligne ==7){victoireNoir = true;}
                    if(ligne == 0){value -= homeValue;}
                    if(ligne == 6 || ligne == 5){value -= valeurDanger;}
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
        int value = valeurPiece;
        int coupPossibles = 0;
        boolean vulnerable = false;
        boolean defendu = false;
        boolean connecterH = false;
        boolean connecterV = false;


        // add connections value//

        if (team == 2){ //NOIR MIN
            value = value*-1;
            int adversaire = 4;

            if (column > 0) {
                if (board[row][column - 1] == team) {
                    connecterH = true;}
            }

            if (column < 7) {
                if (board[row][column + 1] == team) {
                    connecterH = true;}
            }

            if (row > 0) {
                if (board[row-1][column] == team) {
                    connecterV = true;}
            }

            if (row < 7) {
                if (board[row+1][column] == team) {
                    connecterV = true;}
            }




            // add to the value the protected value
            if (row > 0 && column > 0 && column < 7){
                if(board[row-1][column-1]==team||board[row-1][column+1]==team){defendu = true;}

                if (defendu){
                    value+=2;
                }
//                if(board[row-1][column-1]==team||board[row-1][column+1]==team){value -= 0;}

//                if(board[row-1][column+1]==team){value -= 5;}
            }
            // evaluate attack
            if (row < 7 && column > 0 && column < 7){
                if(board[row+1][column-1]==adversaire||board[row+1][column+1]==adversaire){vulnerable = true;}
//                if(board[row+1][column-1]!=team||board[row+1][column+1]!=team){value += 1;}
                //if(board[row+1][column+1]!=team){value += 5;}

                if (vulnerable){value +=1;}
            }

            // test
            if (vulnerable && defendu){
                value +=2;
            }

            // evaluate mobility
            if (row < 7 && column > 0 && column < 7){
                if(board[row+1][column-1]==0){coupPossibles=coupPossibles+1;}
                if(board[row+1][column+1]==0){coupPossibles=coupPossibles+1;}
                if(board[row+1][column]==0){coupPossibles=coupPossibles+1;}
            }

            // evaluate block
            if(row <= 5 && column > 0 && column < 7){
                if(board[row+2][column-1]==adversaire || board[row+2][column]==adversaire || board[row+2][column-1]==adversaire){
                    value -= 0;}
//                if(board[row+2][column]==adversaire){value -= 7;}
//                if(board[row+2][column-1]==adversaire){value -= 7;}
            }

//            value += coupPossibles;

            // evalate distance to goal

//            value = value*(row+1);

        }

        else{ //rouge MAX
            int adversaire = 2;
            // add to the value the protected value
            if (row < 7 && column > 0 && column < 7){
                if(board[row+1][column-1]==team||board[row+1][column+1]==team){defendu = true;}

                if (defendu){
                    value+=2;
                }
//                if(board[row+1][column+1]==team){value += 5;}
            }
            // evaluate attack
            if (row > 0 && column > 0 && column < 7){
                if(board[row-1][column-1]==adversaire||board[row-1][column+1]==adversaire){vulnerable = true;}
                if (vulnerable){value +=1;}
//                if(board[row-1][column+1]!=team){value -= 5;}
            }

            // test
            if (vulnerable && defendu){
                value +=2;
            }

            // evaluate mobility
            if (row > 0 && column > 0 && column < 7){
                if(board[row-1][column-1]==0){coupPossibles=coupPossibles+1;}
                if(board[row-1][column+1]==0){coupPossibles=coupPossibles+1;}
                if(board[row-1][column]==0){coupPossibles=coupPossibles+1;}
            }
            // evaluate block
            if(row >= 2 && column > 0 && column < 7){
                if(board[row-2][column-1]==adversaire||board[row-2][column]==adversaire||board[row-2][column-1]==adversaire){value += 0;}
//                if(board[row-2][column]!=team){value += 7;}
//                if(board[row-2][column-1]!=team){value += 7;}
            }
            // mobility feature
            value += coupPossibles;

            // evaluante distance to goal
//            value = value*(8-row);
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
        /*for(int i = 0; i < mouvementsPossibles.size(); i++){
            afficherPlateau(mouvementsPossibles.get(i));
            System.out.print(" ");

        }*/
        return mouvementsPossibles;
    }

    public Integer[][] copierTableau(Integer[][] plateau) {
        Integer[][] copie = new Integer[plateau.length][plateau.length];
        for (int i = 0; i < plateau.length; i++) {
            for(int j = 0; j < plateau.length; j++) {
                copie[i][j] = plateau[i][j];
            }
        }
        return copie;
    }

    public void modifierPlateau(String s, boolean joueur){
        int ancienY = conversionLettreEnChiffre(s.charAt(0));
        int ancienX = conversionChiffreEnChiffre(Integer.parseInt(String.valueOf(s.charAt(1))));
        int nouveauY = conversionLettreEnChiffre(s.charAt(5));
        int nouveauX = conversionChiffreEnChiffre(Integer.parseInt(String.valueOf(s.charAt(6))));

        if(joueur) {
            Pion pionRouge = new Pion();
            for(Pion pion : pionsRouges){
                if(pion.getPosition().x == ancienX && pion.getPosition().y == ancienY) {
                    pionRouge = pion;
                    break;
                }
            }
            Pion pionNoir = new Pion();
            for(Pion pion : pionsNoirs) {
                if(pion.getPosition().x == nouveauX && pion.getPosition().y == nouveauY) {
                    pionNoir = pion;
                    break;
                }
            }
            this.plateau[ancienX][ancienY] = 0;
            this.plateau[nouveauX][nouveauY] = 4;
            pionRouge.setPosition(nouveauX, nouveauY);
            this.pionsNoirs.remove(pionNoir);

        }
        else {
            Pion pionNoir = new Pion();
            for(Pion pion : pionsNoirs){
                if(pion.getPosition().x == ancienX && pion.getPosition().y == ancienY) {
                    pionNoir = pion;
                    break;
                }
            }
            Pion pionRouge = new Pion();
            for(Pion pion : pionsRouges) {
                if(pion.getPosition().x == nouveauX && pion.getPosition().y == nouveauY) {
                    pionRouge = pion;
                    break;
                }
            }
            this.plateau[ancienX][ancienY] = 0;
            this.plateau[nouveauX][nouveauY] = 2;
            pionNoir.setPosition(nouveauX, nouveauY);
            this.pionsRouges.remove(pionRouge);
//            afficherPlateau(this.plateau);
//            System.out.println("\n");
        }

    }

    public int conversionLettreEnChiffre(char lettre) {
        int chiffre = -1;

        switch (lettre){
            case 'A':
                chiffre = 0;
                break;
            case 'B':
                chiffre = 1;
                break;
            case 'C':
                chiffre = 2;
                break;
            case 'D':
                chiffre = 3;
                break;
            case 'E':
                chiffre = 4;
                break;
            case 'F':
                chiffre = 5;
                break;
            case 'G':
                chiffre = 6;
                break;
            case 'H':
                chiffre = 7;
                break;
        }

        return chiffre;
    }



    public int conversionChiffreEnChiffre(int chiffreInitial) {
        int chiffre = -1;

        switch(chiffreInitial) {
            case 1 :
                chiffre = 7;
                break;
            case 2 :
                chiffre = 6;
                break;
            case 3 :
                chiffre = 5;
                break;
            case 4 :
                chiffre = 4;
                break;
            case 5 :
                chiffre = 3;
                break;
            case 6 :
                chiffre = 2;
                break;
            case 7 :
                chiffre = 1;
                break;
            case 8 :
                chiffre = 0;
                break;
        }
        return chiffre;
    }

}