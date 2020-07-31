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


    private HashMap<Point, Case> cases;
    private HashMap<Point, Case> copie;
    //private Integer[][] plateau;
    private boolean maCouleur;
    private boolean couleurAdverse;
    private ArrayList<Pion> pionsRouges;
    private ArrayList<Pion> pionsNoirs;

    public Jeu() {};
    public Jeu(Integer[][] plateau, boolean maCouleur) {
        this.cases = new HashMap<Point, Case>();
        this.copie = new HashMap<Point, Case>();

        this.maCouleur = maCouleur;
        if(maCouleur) {
            this.couleurAdverse = false;
        }
        else {
            this.couleurAdverse = true;
        }

        this.pionsNoirs = new ArrayList<Pion>();
        this.pionsRouges = new ArrayList<Pion>();

        int direction = 1;
        Case caseP;
        for (int x = 0; x < plateau.length; x++) {
            for (int y = 0; y < plateau.length; y++) {
                Point position = new Point(x,y);

                int valeur = plateau[x][y];
                if (x > (plateau.length-1)/2) {
                    direction = -1;
                }

                if (valeur == 4) {
                    Pion pion = new Pion(true, position, direction);
                    pionsRouges.add(pion);
                    caseP = new Case(position, pion);
                }
                else if (valeur == 2) {
                    Pion pion = new Pion(false, position, direction);
                    pionsNoirs.add(pion);
                    caseP = new Case(position, pion);
                }
                else {
                    caseP = new Case(position, null);
                }
                this.cases.put(position, caseP);
                this.copie.put(position, caseP);
            }
        }
    }
    public boolean getMaCouleur() { return this.maCouleur; }
    public boolean getCouleurAdverse() { return this.couleurAdverse; }

    public HashMap<Point, Case> getCases() {
        return cases;
    }

    public ArrayList<Pion> getPionsRouges() {
        return pionsRouges;
    }

    public ArrayList<Pion> getPionsNoirs() {
        return pionsNoirs;
    }

    public void afficherPlateau(Integer[][] plateau) {
        for(int i = 0; i < plateau.length; i++){
            System.out.print("\n");
            for(int j = 0; j < plateau.length; j++) {
                System.out.print(plateau[i][j]);
            }
        }
    }

    public int getValue(Map<Point, Case> cases){


        boolean victoireNoir = false;
        boolean victoireRouge = false;
        int piecesNoir = 0;
        int piecesRouges = 0;
        int value = 0;
        int homeValue = 0;
        int valeurDanger = 10;


        for (int ligne= 0; ligne < 8; ligne++){
            for (int colonne = 0; colonne< 8; colonne++){
                if (cases.get(new Point(ligne, colonne)).getPion() == null) continue;
                if (cases.get(new Point(ligne, colonne)).getPion().getCouleur() == true) // ROUGE MAX
                {
                    piecesRouges++;
                    value += getPieceValue(cases,ligne, colonne,true);
                    if(ligne == 0){victoireRouge = true;}
                    if(ligne == 7){
                        value += pieceMaison;
                    }

                    //trouver colonne vide
                    int nbColonneVide = 0;
                    for (int i= 0; i < 8; i++){
                        int nbPiontEnemiColonne = 0;
                        for (int j = 0; j< 8; j++){
                            if(cases.get(new Point(i, j)).getPion() != null){
                                if (cases.get(new Point(i, j)).getPion().getCouleur() == false){nbPiontEnemiColonne++;}
                            }
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
                    value += getPieceValue(cases,ligne, colonne,false);
                    if(ligne ==7){victoireNoir = true;}
                    if(ligne == 0){value -= pieceMaison;}
//                    if(ligne == 6 || ligne == 5){value -= valeurDanger;}
                    //trouver colonne vide
                    int nbColonneVide = 0;
                    for (int i= 0; i < 8; i++){
                        int nbPiontEnemiColonne = 0;
                        for (int j = 0; j< 8; j++){
                            if(cases.get(new Point(i, j)).getPion() != null){
                                if (cases.get(new Point(i, j)).getPion().getCouleur() == true){nbPiontEnemiColonne++;}
                            }
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

    public int getPieceValue(Map<Point, Case> cases, int row, int column, boolean team) {
        int value = valeurPiece;
        int coupPossibles = 0;
        boolean vulnerable = false;
        boolean defendu = false;
        boolean connecterH = false;
        boolean connecterV = false;
        boolean gagnant = false;


        // add connections value//

        if (!team){ //NOIR MIN
            boolean adversaire = true;

            if (row == 7){gagnant = true;}

            if (column > 0 && cases.get(new Point(row, column - 1)).getPion() != null) {
                if (cases.get(new Point(row, column - 1)).getPion().getCouleur() == team) {
                    connecterH = true;}
            }

            if (column < 7 && cases.get(new Point(row, column + 1)).getPion() != null) {
                if (cases.get(new Point(row, column + 1)).getPion().getCouleur() == team) {
                    connecterH = true;}
            }

            if (row > 0 && cases.get(new Point(row - 1, column)).getPion() != null) {
                if (cases.get(new Point(row - 1, column)).getPion().getCouleur() == team) {
                    connecterV = true;}
            }

            if (row < 7 && cases.get(new Point(row + 1, column)).getPion() != null) {
                if (cases.get(new Point(row + 1, column)).getPion().getCouleur() == team) {
                    connecterV = true;}
            }

            if (row > 0){
                if(column>0 && cases.get(new Point(row - 1, column - 1)).getPion() != null){
                    if(cases.get(new Point(row - 1, column - 1)).getPion().getCouleur()==team){defendu = true;}
                }
                if(column<7 && cases.get(new Point(row - 1, column + 1)).getPion() != null){
                    if(cases.get(new Point(row - 1, column + 1)).getPion().getCouleur()==team){defendu = true;}
                }
            }
            if (row < 7){
                if(column>0 && cases.get(new Point(row + 1, column - 1)).getPion() != null){
                    if(cases.get(new Point(row + 1, column - 1)).getPion().getCouleur()==adversaire){vulnerable = true;}
                }
                if(column<7 && cases.get(new Point(row + 1, column + 1)).getPion() != null){
                    if(cases.get(new Point(row + 1, column + 1)).getPion().getCouleur()==adversaire){vulnerable = true;}
                }
            }

            // facteur mobile
            if (row > 0 && column > 0 && column < 7){
                if(cases.get(new Point(row + 1, column - 1)).getPion() == null){coupPossibles=coupPossibles+1;}
                if(cases.get(new Point(row + 1, column + 1)).getPion() == null){coupPossibles=coupPossibles+1;}
                if(cases.get(new Point(row +1, column)).getPion() == null){coupPossibles=coupPossibles+1;}
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
            boolean adversaire = false;


            if (row ==0){gagnant = true;}

            if (column > 0 && cases.get(new Point(row, column - 1)).getPion() != null) {
                if (cases.get(new Point(row, column - 1)).getPion().getCouleur() == team) {
                    connecterH = true;
                }
            }

            if (column < 7 && cases.get(new Point(row, column + 1)).getPion() != null) {
                if (cases.get(new Point(row, column + 1)).getPion().getCouleur() == team) {
                    connecterH = true;
                }
            }

            if (row > 0 && cases.get(new Point(row - 1, column)).getPion() != null) {
                if (cases.get(new Point(row - 1, column)).getPion().getCouleur() == team) {
                    connecterV = true;
                }
            }

            if (row < 7 && cases.get(new Point(row + 1, column)).getPion() != null) {
                if (cases.get(new Point(row + 1, column)).getPion().getCouleur() == team) {
                    connecterV = true;
                }
            }

            if (row < 7) {
                if (column > 0 && cases.get(new Point(row + 1, column - 1)).getPion() != null) {
                    if (cases.get(new Point(row + 1, column - 1)).getPion().getCouleur() == team) {
                        defendu = true;
                    }
                }
                if (column < 7 && cases.get(new Point(row + 1, column + 1)).getPion() != null) {
                    if (cases.get(new Point(row + 1, column + 1)).getPion().getCouleur() == team) {
                        defendu = true;
                    }
                }
            }
            if (row > 0) {
                if (column > 0 && cases.get(new Point(row - 1, column - 1)).getPion() != null) {
                    if (cases.get(new Point(row - 1, column - 1)).getPion().getCouleur() == adversaire) {
                        vulnerable = true;
                    }
                }
                if (column < 7 && cases.get(new Point(row - 1, column + 1)).getPion() != null) {
                    if (cases.get(new Point(row - 1, column + 1)).getPion().getCouleur() == adversaire) {
                        vulnerable = true;
                    }
                }
            }
            // facteur mobile
            if (row > 0 && column > 0 && column < 7){
                if(cases.get(new Point(row - 1, column - 1)).getPion() == null){coupPossibles=coupPossibles+1;}
                if(cases.get(new Point(row - 1, column + 1)).getPion() == null){coupPossibles=coupPossibles+1;}
                if(cases.get(new Point(row - 1, column)).getPion() == null){coupPossibles=coupPossibles+1;}
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

    public HashMap<Point, Case> cloneMap(HashMap<Point, Case> cases, ArrayList<Pion> pionsIn, ArrayList<Pion> pionsOut){
        HashMap<Point, Case> clone = new HashMap<Point, Case>();
        boolean trouve = false;
        for(Point point : cases.keySet()){
            trouve = false;
            for(Pion pionIn : pionsIn){
                if(pionIn.getPosition().equals(point)){
                    clone.put(new Point(point.x, point.y), new Case(new Point(point.x,point.y), pionIn));
                    trouve = true;
                    break;
                }

            }
            for(Pion pionOut : pionsOut){
                if(pionOut.getPosition().equals(point)){
                    clone.put(new Point(point.x, point.y), new Case(new Point(point.x,point.y), pionOut));
                    trouve = true;
                    break;
                }
            }
            if(!trouve){
                clone.put(new Point(point.x, point.y), new Case(new Point(point.x,point.y), null));
            }
        }
        /*for(Point point : cases.keySet()){
            boolean couleur;
            Pion pion;
            if(cases.get(point).getPion() != null){
                couleur = cases.get(point).getPion().getCouleur();
                pion = new Pion(couleur, new Point(point.x, point.y), cases.get(point).getPion().getDirection());
            }else {
                pion = null;
            }
            clone.put(new Point(point.x,point.y), new Case(new Point(point.x,point.y), pion));
        }*/
        return clone;
    }

    public ArrayList<String> generateurMouvement(HashMap<Point, Case> cases, Point position, int direction, boolean joueur) {
        Point depart = new Point(position.x, position.y);
        Point devant = new Point(position.x + direction, position.y);
        Point gauche = new Point(position.x + direction, position.y - 1);
        Point droite = new Point(position.x + direction, position.y + 1);

        ArrayList<String> mouvementsPossibles = new ArrayList<String>();

        if(!(direction == -1 && position.x + direction == 0) && !(direction == 1 && position.x + direction == 7)) {

            if (cases.get(devant).getPion() == null) {
                mouvementsPossibles.add("" + depart.x + depart.y + devant.x + devant.y);
            }
            if (depart.y > 0) {
                if (cases.get(gauche).getPion() == null || cases.get(gauche).getPion().getCouleur() != joueur) {
                    mouvementsPossibles.add("" + depart.x + depart.y + gauche.x + gauche.y);
                }
            }
            if (depart.y < 7) {
                if (cases.get(droite).getPion() == null || cases.get(droite).getPion().getCouleur() != joueur) {
                    mouvementsPossibles.add("" + depart.x + depart.y + droite.x + droite.y);
                }
            }
        }
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

    public boolean checkCases(Map<Point, Case> casesL){
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
            this.cases.get(new Point(ancienX, ancienY)).setPion(null);
            this.cases.get(new Point(nouveauX, nouveauY)).setPion(pionRouge);
            pionRouge.setPosition(new Point(nouveauX, nouveauY));
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
            this.cases.get(new Point(ancienX, ancienY)).setPion(null);
            this.cases.get(new Point(nouveauX, nouveauY)).setPion(pionNoir);
            pionNoir.setPosition(new Point(nouveauX, nouveauY));
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
