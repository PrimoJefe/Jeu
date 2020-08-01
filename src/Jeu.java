import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Jeu implements Cloneable{

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


    //private HashMap<Point, Case> cases;
    //private HashMap<Point, Case> copie;
    private int [][] plateau;
    private int maCouleur;
    private int couleurAdverse;
    private Map<Point, Pion> pionsRouges;
    private Map<Point, Pion> pionsNoirs;

    public Jeu() {};
    public Jeu(int[][] plateau, boolean maCouleur) {
        this.plateau = copierTableau(plateau);

        if(maCouleur) { this.maCouleur = 4;
                        this.couleurAdverse = 2;}
        else {this.maCouleur = 2;
                this.couleurAdverse = 4;}

        this.pionsRouges = new HashMap<Point, Pion>();
        this.pionsNoirs = new HashMap<Point, Pion>();

        int direction = 1;

        for (int x = 0; x < plateau.length; x++) {
            for (int y = 0; y < plateau.length; y++) {
                Point position = new Point(x,y);
                int valeur = plateau[x][y];

                if (x > (plateau.length-1)/2) {
                    direction = -1;
                }

                if (valeur == 4) {
                    pionsRouges.put(position, new Pion(4, position, direction));
                }
                else if (valeur == 2) {
                    pionsNoirs.put(position, new Pion(2, position, direction));
                }
            }
        }
    }
    public int[][] getPlateau() {
        return this.plateau;
    }
    public int getMaCouleur() { return this.maCouleur; }
    public int getCouleurAdverse() { return this.couleurAdverse; }

    public Map<Point, Pion> getPionsRouges() {
        return pionsRouges;
    }

    public Map<Point, Pion> getPionsNoirs() {
        return pionsNoirs;
    }

    public void afficherPlateau(int[][] plateau) {
        for(int i = 0; i < plateau.length; i++){
            System.out.print("\n");
            for(int j = 0; j < plateau.length; j++) {
                System.out.print(plateau[i][j]);
            }
        }
    }

    public int getValue(int[][] board){

        boolean victoireNoir = false;
        boolean victoireRouge = false;
        int piecesNoir = 0;
        int piecesRouges = 0;
        int value = 0;
        int homeValue = 0;
        int valeurDanger = 10;


        for (int ligne= 0; ligne < 8; ligne++){
            for (int colonne = 0; colonne< 8; colonne++){
                if (board[ligne][colonne] == 0) continue;
                if (board[ligne][colonne] == 4) // ROUGE MAX
                {
                    piecesRouges++;
                    value += getPieceValue(board,ligne, colonne,4);
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

    public int getPieceValue(int[][] board, int row, int column, int team) {
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
                if(board[row+1][column-1]==0){coupPossibles=coupPossibles+1;}
                if(board[row+1][column+1]==0){coupPossibles=coupPossibles+1;}
                if(board[row+1][column]==0){coupPossibles=coupPossibles+1;}
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

    public ArrayList<Pion> cloneListe(ArrayList<Pion> pions){
        ArrayList<Pion> clone = new ArrayList<Pion>();
        for(Pion pion : pions) {
            clone.add(new Pion(pion.getCouleur(), pion.getPosition(), pion.getDirection()));
        }
        return clone;
    }

    public Map<Point, Pion> cloneMap(Map<Point, Pion> pions){
        Map<Point, Pion> clone = new HashMap<>();
        boolean trouve = false;

        for(Point point : pions.keySet()) {
            Pion pion = pions.get(point);

            clone.put(point, new Pion(pion.getCouleur(), point, pion.getDirection()));
        }
        return clone;
    }

    public ArrayList<String> generateurMouvement(int[][] plateau, Point position, int direction, int joueur) {
        Point depart = new Point(position.x, position.y);
        Point devant = new Point(position.x + direction, position.y);
        Point gauche = new Point(position.x + direction, position.y - 1);
        Point droite = new Point(position.x + direction, position.y + 1);

        ArrayList<String> mouvementsPossibles = new ArrayList<String>();

        if(!(direction == -1 && position.x + direction == 0) && !(direction == 1 && position.x + direction == 7)) {

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

    /*public boolean checkCases(Map<Point, Case> casesL){
        boolean test = true;
        for(int i = 0; i<8; i++){
            for(int j =0; j<8; j++){
                Boolean couleur1 = this.copie.get(new Point(i,j)).getPion().getCouleur();
                Boolean couleur2 = casesL.get(new Point(i,j)).getPion().getCouleur();
                int c = -1;
                if(couleur1 ==null){
                    c=0;
                }else if(couleur1 == true){
                    c=4;
                }else{
                    c=2;
                }

                int g = -1;
                if(couleur2 ==null){
                    g=0;
                }else if(couleur2 == true){
                    g=4;
                }else{
                    g=2;
                }

                if(c!=g){
                    test = false;
                }
            }
        }
        return test;
    }*/

    public void modifierPlateau(String s, int joueur){
        Point depart = new Point(conversionChiffreEnChiffre(Integer.parseInt(String.valueOf(s.charAt(1)))),
            conversionLettreEnChiffre(s.charAt(0)));

        Point arrivee = new Point(conversionChiffreEnChiffre(Integer.parseInt(String.valueOf(s.charAt(6)))),
                conversionLettreEnChiffre(s.charAt(5)));


        if(joueur == 4) {
            Pion pionRouge = this.pionsRouges.get(depart);
            Pion pionNoir = this.pionsNoirs.get(arrivee);

            this.plateau[depart.x][depart.y] = 0;
            this.plateau[arrivee.x][arrivee.y] = 4;

            this.pionsRouges.remove(depart);
            pionRouge.setPosition(arrivee);
            this.pionsRouges.put(arrivee, pionRouge);

            if(this.pionsNoirs.get(arrivee) != null) {
                this.pionsNoirs.remove(pionNoir);
            }
        }
        else {
            Pion pionNoir = this.pionsNoirs.get(depart);
            Pion pionRouge = this.pionsRouges.get(arrivee);

            this.plateau[depart.x][depart.y] = 0;
            this.plateau[arrivee.x][arrivee.y] = 2;

            this.pionsNoirs.remove(depart);
            pionNoir.setPosition(arrivee);
            this.pionsNoirs.put(arrivee, pionNoir);

            if(this.pionsRouges.get(arrivee) != null) {
                this.pionsRouges.remove(pionRouge);
            }
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
