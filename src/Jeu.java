import java.awt.*;
import java.util.ArrayList;

public class Jeu implements Cloneable{

    private final int victoire = 500000;
    private final int valeurPiece = 1300;
    private final int pieceDanger = 10;
    private final int pieceGrandDanger = 100;
    private final int pieceValeurAttack = 50;
    private final int pieceValeurProtection = 65;
    private final int pieceConnectionH = 35;
    private final int pieceConnectionV = 15;
    private final int pieceTrouColonne = 20;
    private final int pieceMaison = 150;

    private int [][] plateau;
    private int maCouleur;
    private int couleurAdverse;
    private ArrayList<Pion> pionsRouges;
    private ArrayList<Pion> pionsNoirs;

    /*
     * CONSTRUCTEURS
     */
    public Jeu() {};
    public Jeu(int[][] plateau, boolean maCouleur) {
        this.plateau = copierTableau(plateau);

        if(maCouleur) { this.maCouleur = 4;
                        this.couleurAdverse = 2;}
        else {this.maCouleur = 2;
                this.couleurAdverse = 4;}

        this.pionsRouges = new ArrayList<>();
        this.pionsNoirs = new ArrayList<>();

        int direction = 1;

        for (int x = 0; x < plateau.length; x++) {
            for (int y = 0; y < plateau.length; y++) {
                Point position = new Point(x,y);
                int valeur = plateau[x][y];

                if (x > (plateau.length-1)/2) {
                    direction = -1;
                }

                if (valeur == 4) {
                    pionsRouges.add(new Pion(4, position, direction));
                }
                else if (valeur == 2) {
                    pionsNoirs.add(new Pion(2, position, direction));
                }
            }
        }
    }

    /*
     * GETTERS
     */
    public int[][] getPlateau() { return this.plateau; }
    public int getMaCouleur() { return this.maCouleur; }
    public int getCouleurAdverse() { return this.couleurAdverse; }
    public ArrayList<Pion> getPionsRouges() { return pionsRouges; }
    public ArrayList<Pion> getPionsNoirs() { return pionsNoirs; }

    /*
     * SETTERS
     */
    public void setPlateau(int[][] plateau) { this.plateau = plateau; }
    public void setPionsRouges(ArrayList<Pion> pionsRouges) { this.pionsRouges = pionsRouges; }
    public void setPionsNoirs(ArrayList<Pion> pionsNoirs) { this.pionsNoirs = pionsNoirs; }

    /*
     * METHODES
     */
    public int evaluerPlateau(int[][] board){

        boolean victoireNoir = false;
        boolean victoireRouge = false;
        int piecesNoir = 0;
        int piecesRouges = 0;
        int value = 0;


        for (int ligne= 0; ligne < 8; ligne++){
            for (int colonne = 0; colonne< 8; colonne++){
                if (board[ligne][colonne] == 0) continue;
                if (board[ligne][colonne] == 4) // ROUGE MAX
                {
                    piecesRouges++;
                    value += evaluerPion(board,ligne, colonne,4);
                    if(ligne == 0){victoireRouge = true;}
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
//
                } else{ // NOIR MIN
                    // comme rouge
                    piecesNoir++;
                    value += evaluerPion(board,ligne, colonne,2);
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
        // Si tous les pions d'une couleur ont ete manges
        if (piecesRouges == 0) victoireNoir = true;
        if (piecesNoir == 0) victoireRouge = true;

        // position gagnante
        if (victoireNoir){
            value -= victoire;
        }
        if (victoireRouge){
            value += victoire;
        }

        return value;
    }

    // L'attribution des points pour les fonctions evaluerPlateau et evaluerPion est inspiree du code trouve au site
    // suvant : https://www.codeproject.com/Articles/37024/Simple-AI-for-the-Game-of-Breakthrough
    public int evaluerPion(int[][] board, int row, int column, int team) {
        int value = valeurPiece;
        int coupPossibles = 0;
        boolean vulnerable = false;
        boolean defendu = false;
        boolean connecterH = false;
        boolean connecterV = false;
        boolean gagnant = false;

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
            if (row < 7 && column > 0){
                if(board[row+1][column-1]!=team){coupPossibles=coupPossibles+1;}
                if(board[row+1][column]==0){coupPossibles=coupPossibles+1;}
            }
            if (row < 7 && column < 7){
                if(board[row+1][column+1]!=team){coupPossibles=coupPossibles+1;}
            }


            if (gagnant){
                value+= victoire;
            }
            if (connecterH){value += pieceConnectionH;}
            if (connecterV){value += pieceConnectionV;}
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
            value += row * 2;
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
            if (row > 0 && column > 0){
                if(board[row-1][column-1]!=team){coupPossibles=coupPossibles+1;}
                if(board[row-1][column]==0){coupPossibles=coupPossibles+1;}
            }
            if (row > 0 && column < 7){
                if(board[row-1][column+1]!=team){coupPossibles=coupPossibles+1;}
            }


            if (gagnant){
                value+= victoire;
            }
            if (connecterH) {
                value += pieceConnectionH;
            }
            if (connecterV) {
                value += pieceConnectionV;
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

            value += (7-row) * 2;
            value += coupPossibles*2;
        }

        return value;
    }

    public ArrayList<String> generateurMouvement(int[][] plateau, Point position, int direction, int joueur) {
        Point depart = new Point(position.x, position.y);
        Point devant = new Point(position.x + direction, position.y);
        Point gauche = new Point(position.x + direction, position.y - 1);
        Point droite = new Point(position.x + direction, position.y + 1);

        ArrayList<String> mouvementsPossibles = new ArrayList<String>();

        if(!(direction == -1 && position.x == 0) && !(direction == 1 && position.x == 7)) {

            if (plateau[devant.x][devant.y] == 0) {
                mouvementsPossibles.add("" + depart.x + depart.y + devant.x + devant.y);
            }
            if (depart.y > 0) {
                if (plateau[gauche.x][gauche.y] != joueur) {
                    mouvementsPossibles.add("" + depart.x + depart.y + gauche.x + gauche.y);
                }
            }
            if (depart.y < 7) {
                if (plateau[droite.x][droite.y] != joueur) {
                    mouvementsPossibles.add("" + depart.x + depart.y + droite.x + droite.y);
                }
            }

        }
        return mouvementsPossibles;
    }

    public int[][] copierTableau(int[][] plateau) {
        int[][] clone = new int[plateau.length][plateau.length];
        for (int i = 0; i < plateau.length; i++) {
            for(int j = 0; j < plateau.length; j++) {
                clone[i][j] = plateau[i][j];
            }
        }
        return clone;
    }

    public ArrayList<Pion> clonerListe(ArrayList<Pion> pions){
        ArrayList<Pion> clone = new ArrayList<>();

        for(Pion pion : pions) {
            clone.add(new Pion(pion.getCouleur(), new Point(pion.getPosition().x, pion.getPosition().y),
                    pion.getDirection()));
        }
        return clone;
    }

    public void modifierPlateau(String s, int joueur){
        Point depart = new Point(conversionChiffreEnChiffre(Integer.parseInt(String.valueOf(s.charAt(1)))),
            conversionLettreEnChiffre(s.charAt(0)));

        Point arrivee = new Point(conversionChiffreEnChiffre(Integer.parseInt(String.valueOf(s.charAt(6)))),
                conversionLettreEnChiffre(s.charAt(5)));


        if(joueur == 4) {
            Pion pionRouge = null;
            for(Pion pion : this.pionsRouges) {
                if(pion.getPosition().equals(depart)) {
                    pionRouge = pion;
                    break;
                }
            }
            if(this.plateau[arrivee.x][arrivee.y] == 2){
                Pion pionNoir = null;
                for(Pion pion : this.pionsNoirs) {
                    if(pion.getPosition().equals(arrivee)) {
                        pionNoir = pion;
                        break;
                    }
                }
                this.pionsNoirs.remove(pionNoir);
            }

            this.plateau[depart.x][depart.y] = 0;
            this.plateau[arrivee.x][arrivee.y] = 4;

            pionRouge.setPosition(arrivee);
        }
        else {
            Pion pionNoir = null;
            for(Pion pion : this.pionsNoirs) {
                if(pion.getPosition().equals(depart)) {
                    pionNoir = pion;
                    break;
                }
            }
            if(this.plateau[arrivee.x][arrivee.y] == 4){
                Pion pionRouge = null;
                for(Pion pion : this.pionsRouges) {
                    if(pion.getPosition().equals(arrivee)) {
                        pionRouge = pion;
                        break;
                    }
                }
                this.pionsRouges.remove(pionRouge);
            }

            this.plateau[depart.x][depart.y] = 0;
            this.plateau[arrivee.x][arrivee.y] = 2;

            pionNoir.setPosition(arrivee);
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
