import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Jeu {

    private Integer[][] plateau;
    private boolean maCouleur;
    private boolean couleurAdverse;
    private ArrayList<Pion> pionsRouges;
    private ArrayList<Pion> pionsNoirs;
    private ArrayList<Pion> pions;

    private int victoire = 500000;
    private int presqueGagne = 10000;
    private int valeurPiece = 1300;
    private int pieceDanger = 10;
    private int pieceGrandDanger = 100;
    private int pieceValeurAttack = 50;
    private int pieceValeurProtection = 65;
    private int pieceConnectionH = 35;
    private int pieceConnectionV = 15;
    private int pieceTrouColonne = 20;
    private int pieceMaison = 150;
    private int directionRouge = 0;
    private int directionNoir = 0;


    public Jeu() {};
    public Jeu(Integer[][] plateau, boolean maCouleur) {


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
        this.pions = new ArrayList<Pion>();

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
                    pions.add(pion);
                    this.directionRouge = direction;
                }
                else if (valeur == 2) {
                    Pion pion = new Pion(false, position, direction);
                    pionsNoirs.add(pion);
                    pions.add(pion);
                    this.directionNoir = direction;
                }
                i++;
            }
        }
    }


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

    public ArrayList<Pion> getPions() {
        return pions;
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
                        value += pieceMaison;
                    }

                    //trouver colonne vide
                    int nbColonneVide = 0;
                    for (int i= 0; i < 8; i++){
                        int nbPiontEnemiColonne = 0;
                        for (int j = 0; j< 8; j++){
                            if (board[j][i]==2){nbPiontEnemiColonne++;}
                        }
                        if (nbPiontEnemiColonne == 0){nbColonneVide++;}
                    }
                    if (nbColonneVide>0){value+=pieceTrouColonne;}
//                    if(ligne == 1 || ligne == 2){value += valeurDanger;}
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
                    if(ligne == 0){value -= pieceMaison;}
//                    if(ligne == 6 || ligne == 5){value -= valeurDanger;}
                    //trouver colonne vide
                    int nbColonneVide = 0;
                    for (int i= 0; i < 8; i++){
                        int nbPiontEnemiColonne = 0;
                        for (int j = 0; j< 8; j++){
                            if (board[j][i]==4){nbPiontEnemiColonne++;}
                        }
                        if (nbPiontEnemiColonne == 0){nbColonneVide++;}
                    }
                    if (nbColonneVide>0){value-=pieceTrouColonne;}
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

    public int getPieceValue(Integer[][] board, int row, int column, int team) {
        int value = valeurPiece;
        int coupPossibles = 0;
        boolean vulnerable = false;
        boolean defendu = false;
        boolean connecterH = false;
        boolean connecterV = false;
        boolean gagnant = false;


        // add connections value//

        if (team == 2){ //NOIR MIN
            int adversaire = 4;

            if (row ==7){gagnant = true;}

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

            if (row > 0){
                if(column>0){
                    if(board[row-1][column-1]==team){defendu = true;}
                }
                if(column<7){
                    if(board[row-1][column+1]==team){defendu = true;}
                }
            }
            if (row < 7){
                if(column>0){
                    if(board[row+1][column-1]==adversaire){vulnerable = true;}
                }
                if(column<7){
                    if(board[row+1][column+1]==adversaire){vulnerable = true;}
                }
            }

            // facteur mobile
            if (row > 0 && column > 0 && column < 7){
                if(board[row-1][column-1]==0){coupPossibles=coupPossibles+1;}
                if(board[row-1][column+1]==0){coupPossibles=coupPossibles+1;}
                if(board[row-1][column]==0){coupPossibles=coupPossibles+1;}
            }


            if (gagnant){
                value+= victoire;
            }
            if (connecterH){value += pieceConnectionH;}
//            if (connecterV){value += pieceConnectionV;}
            if (defendu){value += pieceValeurProtection;}
            if (vulnerable){value -= pieceValeurAttack;}

            if (vulnerable && !defendu){value -= pieceValeurAttack*2;}

            if (defendu){
                if (row == 5) {
                    value += pieceDanger;
                }
                else if (row == 6){
                    value += pieceGrandDanger;
                }
            }

            if (!vulnerable && row==6){
                value+=victoire;
            }

            value += coupPossibles*2;
            value += row * pieceDanger;
            value = value*-1;

        }

        else { //rouge MAX
            int adversaire = 2;


            if (row ==0){gagnant = true;}

            if (column > 0) {
                if (board[row][column - 1] == team) {
                    connecterH = true;
                }
            }

            if (column < 7) {
                if (board[row][column + 1] == team) {
                    connecterH = true;
                }
            }

            if (row > 0) {
                if (board[row - 1][column] == team) {
                    connecterV = true;
                }
            }

            if (row < 7) {
                if (board[row + 1][column] == team) {
                    connecterV = true;
                }
            }

            if (row < 7) {
                if (column > 0) {
                    if (board[row + 1][column - 1] == team) {
                        defendu = true;
                    }
                }
                if (column < 7) {
                    if (board[row + 1][column + 1] == team) {
                        defendu = true;
                    }
                }
            }
            if (row > 0) {
                if (column > 0) {
                    if (board[row - 1][column - 1] == adversaire) {
                        vulnerable = true;
                    }
                }
                if (column < 7) {
                    if (board[row - 1][column + 1] == adversaire) {
                        vulnerable = true;
                    }
                }
            }
            // facteur mobile
            if (row > 0 && column > 0 && column < 7){
                if(board[row-1][column-1]==0){coupPossibles=coupPossibles+1;}
                if(board[row-1][column+1]==0){coupPossibles=coupPossibles+1;}
                if(board[row-1][column]==0){coupPossibles=coupPossibles+1;}
            }
            value += coupPossibles*2;

            if (gagnant){
                value+= victoire;
            }
            if (connecterH) {
                value += pieceConnectionH;
            }
            if (connecterV) {
//                value += pieceConnectionV;
            }
            if (defendu) {
                value += pieceValeurProtection;
            }
            if (vulnerable) {
                value -= pieceValeurAttack;
            }

            if (vulnerable && !defendu) {
                value -= 2*pieceValeurAttack;
            }

            if (defendu) {
                if (row == 2) {
                    value += pieceDanger;
                } else if (row == 1) {
                    value += pieceGrandDanger;
                }
            }

            if (!vulnerable && row==1){
                value+=victoire;
            }

            value += row * pieceDanger;
        }

        return value;
    }

    public Map<Integer[][], ArrayList<Pion>> generateurMouvement(Integer[][] plateau, ArrayList<Pion> pions, Boolean joueur) {
        Integer[][] plateauPossible = copierTableau(plateau);
        Map<Integer[][], ArrayList<Pion>> mouvementsPossibles = new HashMap<Integer[][], ArrayList<Pion>>();
        //ArrayList<Pion> pionsTemp = new ArrayList<Pion>(pions);
        ArrayList<Pion> pionsTemp = new ArrayList<>();
        for(Pion test : pions) {
            pionsTemp.add(test);
        }


        for (Pion pion : pions) {
            int ligne = pion.getPosition().x;
            int colonne = pion.getPosition().y;
            int avancement = ligne + pion.getDirection();
            int couleur;
            Point depart = new Point(ligne, colonne);

            if (pion.getCouleur() == true) {
                couleur = 4;
            }
            else {
                couleur = 2;
            }
            if(pion.getCouleur() == joueur) {
                if((pion.getDirection() < 0 && avancement >= 0) ||
                        (pion.getDirection() > 0 && avancement < plateau.length)){

                    if (plateau[avancement][colonne] == 0) {
                        plateauPossible[avancement][colonne] = plateau[ligne][colonne];
                        plateauPossible[ligne][colonne] = 0;

                        for(Pion pionTemp : pionsTemp) {
                            if(pionTemp.getPosition().x == ligne && pionTemp.getPosition().y == colonne){
                                pionTemp.setPosition(avancement, colonne);
                            }
                        }

                        mouvementsPossibles.put(plateauPossible, pionsTemp);
                        plateauPossible = copierTableau(plateau);
                        //pionsTemp = new ArrayList<Pion>(pions);
                        pionsTemp = new ArrayList<>();
                        for(Pion test : pions) {
                            pionsTemp.add(test);
                        }
                    }
                    if(colonne != 0) {
                        if(plateau[avancement][colonne - 1] == 0) {
                            plateauPossible[avancement][colonne - 1] = plateau[ligne][colonne];
                            plateauPossible[ligne][colonne] = 0;

                            for(Pion pionTemp : pionsTemp) {
                                if(pionTemp.getPosition().x == ligne && pionTemp.getPosition().y == colonne){
                                    pionTemp.setPosition(avancement, colonne - 1);
                                }
                            }

                            mouvementsPossibles.put(plateauPossible, pionsTemp);
                            plateauPossible = copierTableau(plateau);
                            //pionsTemp = new ArrayList<Pion>(pions);
                            pionsTemp = new ArrayList<>();
                            for(Pion test : pions) {
                                pionsTemp.add(test);
                            }
                        }
                        else if (plateau[avancement][colonne - 1] != couleur) {
                            plateauPossible[avancement][colonne - 1] = plateau[ligne][colonne];
                            plateauPossible[ligne][colonne] = 0;

                            Pion toDelete = new Pion();
                            for(Pion pionTemp : pionsTemp) {
                                if(pionTemp.getPosition().x == avancement && pionTemp.getPosition().y == colonne - 1){
                                    toDelete = pion;
                                }
                            }
                            pionsTemp.remove(toDelete);
                            for(Pion pionTemp : pionsTemp) {
                                if(pionTemp.getPosition().x == ligne && pionTemp.getPosition().y == colonne){
                                    pionTemp.setPosition(avancement, colonne - 1);
                                }
                            }

                            mouvementsPossibles.put(plateauPossible, pionsTemp);
                            plateauPossible = copierTableau(plateau);
                            //pionsTemp = new ArrayList<Pion>(pions);
                            pionsTemp = new ArrayList<>();
                            for(Pion test : pions) {
                                pionsTemp.add(test);
                            }
                        }
                    }
                    if(colonne != plateau.length - 1) {
                        if(plateau[avancement][colonne + 1] == 0) {
                            plateauPossible[avancement][colonne + 1] = plateau[ligne][colonne];
                            plateauPossible[ligne][colonne] = 0;

                            for(Pion pionTemp : pionsTemp) {
                                if(pionTemp.getPosition().x == ligne && pionTemp.getPosition().y == colonne){
                                    pionTemp.setPosition(avancement, colonne + 1);
                                }
                            }

                            mouvementsPossibles.put(plateauPossible, pionsTemp);
                            plateauPossible = copierTableau(plateau);
                            //pionsTemp = new ArrayList<Pion>(pions);
                            pionsTemp = new ArrayList<>();
                            for(Pion test : pions) {
                                pionsTemp.add(test);
                            }
                        }
                        else if (plateau[avancement][colonne + 1] != couleur) {
                            plateauPossible[avancement][colonne + 1] = plateau[ligne][colonne];
                            plateauPossible[ligne][colonne] = 0;
                            Pion toDelete = new Pion();

                            for(Pion pionTemp : pionsTemp) {
                                if(pionTemp.getPosition().x == avancement && pionTemp.getPosition().y == colonne + 1){
                                    toDelete = pion;
                                }
                            }
                            pionsTemp.remove(toDelete);

                            for(Pion pionTemp : pionsTemp) {
                                if(pionTemp.getPosition().x == ligne && pionTemp.getPosition().y == colonne){
                                    pionTemp.setPosition(avancement, colonne + 1);
                                }
                            }

                            mouvementsPossibles.put(plateauPossible, pionsTemp);
                            plateauPossible = copierTableau(plateau);
                            //pionsTemp = new ArrayList<Pion>(pions);
                            pionsTemp = new ArrayList<>();
                            for(Pion test : pions) {
                                pionsTemp.add(test);
                            }
                        }
                    }
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
            for(Pion pion : pions){
                if(pion.getPosition().x == ancienX && pion.getPosition().y == ancienY && pion.getCouleur() == joueur) {
                    pionRouge = pion;
                    break;
                }
            }
            Pion pionNoir = new Pion();
            for(Pion pion : pions) {
                if(pion.getPosition().x == nouveauX && pion.getPosition().y == nouveauY && pion.getCouleur() != joueur) {
                    pionNoir = pion;
                    break;
                }
            }
            this.plateau[ancienX][ancienY] = 0;
            this.plateau[nouveauX][nouveauY] = 4;
            pionRouge.setPosition(nouveauX, nouveauY);
            this.pions.remove(pionNoir);

        }
        else {
            Pion pionNoir = new Pion();
            for(Pion pion : pions){
                if(pion.getPosition().x == ancienX && pion.getPosition().y == ancienY && pion.getCouleur() == joueur) {
                    pionNoir = pion;
                    break;
                }
            }
            Pion pionRouge = new Pion();
            for(Pion pion : pions) {
                if(pion.getPosition().x == nouveauX && pion.getPosition().y == nouveauY && pion.getCouleur() != joueur) {
                    pionRouge = pion;
                    break;
                }
            }
            this.plateau[ancienX][ancienY] = 0;
            this.plateau[nouveauX][nouveauY] = 2;
            pionNoir.setPosition(nouveauX, nouveauY);
            this.pions.remove(pionRouge);
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

    public int getDirectionRouge() {
        return directionRouge;
    }

    public int getDirectionNoir() {
        return directionNoir;
    }
}
