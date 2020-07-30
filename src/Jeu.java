import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Jeu {

    private HashMap<Point, Case> cases;
    //private Integer[][] plateau;
    private boolean maCouleur;
    private boolean couleurAdverse;
    private ArrayList<Pion> pionsRouges;
    private ArrayList<Pion> pionsNoirs;

    public Jeu() {};
    public Jeu(Integer[][] plateau, boolean maCouleur) {
        this.cases = new HashMap<Point, Case>();

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
                    caseP = new Case(position, new Pion(null, position, 0));
                }
                this.cases.put(position, caseP);
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
        int victoire = 100;
        boolean victoireNoir = false;
        boolean victoireRouge = false;
        int piecesNoir = 0;
        int piecesRouges = 0;
        int value = 0;
        for (int x= 0; x < 8; x++){
            for (int y = 0; y< 8; y++){
                if (cases.get(new Point(x,y)).getPion().getCouleur() == null) continue;
                if (cases.get(new Point(x,y)).getPion().getCouleur()) // ROUGE MAX
                {
                    piecesRouges++;
                    value += getPieceValue(cases,x, y,true);
                    if(y ==0){victoireRouge = true;}
                    //if(y == 0){Value += HomeGroundValue;}
//                    if {(column > 0) ThreatA = (board[GetPosition(y - 1, 7).NoPieceOnSquare);}
//                    if (column < 7) ThreatB = (board.GetPosition(y + 1, 7).NoPieceOnSquare);
//                    if (ThreatA && ThreatB) // almost win
//                        board.Value += PieceAlmostWinValue;
                } else{ // NOIR MIN
                    // comme rouge
                    piecesNoir++;
                    value += getPieceValue(cases,x, y,false);
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

    public int getPieceValue(Map<Point, Case> cases, int row, int column, Boolean team)
    {
        int value = 0;

        // add connections value//

        if (team == false){ //NOIR MIN
            // add to the value the protected value
            if (row > 0 && column > 0 && column < 7){
                if(cases.get(new Point(row - 1, column - 1)).getPion().getCouleur() == team){value -= 5;}
                if(cases.get(new Point(row - 1, column + 1)).getPion().getCouleur() == team){value -= 5;}
            }
            // evaluate attack
            if (row < 7 && column > 0 && column < 7){
                if(cases.get(new Point(row + 1, column - 1)).getPion().getCouleur() != team){value += 5;}
                if(cases.get(new Point(row + 1, column + 1)).getPion().getCouleur() != team){value += 5;}
            }

            // evaluate block
            if(row <= 5 && column > 0 && column < 7){
                if(cases.get(new Point(row + 2, column - 1)).getPion().getCouleur() != team){value -= 5;}
                if(cases.get(new Point(row + 2, column)).getPion().getCouleur() != team){value -= 5;}
                if(cases.get(new Point(row + 2, column + 1)).getPion().getCouleur() != team){value -= 5;}
            }

            // mobility feature
            // Value += possibleMoves;

            // evalate distance to goal
            value = value*(row+1);

        }

        else if(team == true){ //rouge MAX
            // add to the value the protected value
            if (row < 7 && column > 0 && column < 7){
                if(cases.get(new Point(row + 1, column - 1)).getPion().getCouleur() == team){value += 5;}
                if(cases.get(new Point(row + 1, column + 1)).getPion().getCouleur() == team){value += 5;}
            }
            // evaluate attack
            if (row > 0 && column > 0 && column < 7){
                if(cases.get(new Point(row - 1, column - 1)).getPion().getCouleur() !=team ){value -= 5;}
                if(cases.get(new Point(row - 1, column + 1)).getPion().getCouleur() != team){value -= 5;}
            }
            // evaluate block
            if(row >= 2 && column > 0 && column < 7){
                if(cases.get(new Point(row + 2, column - 1)).getPion().getCouleur() != team){value += 5;}
                if(cases.get(new Point(row + 2, column)).getPion().getCouleur() != team){value += 5;}
                if(cases.get(new Point(row + 2, column + 1)).getPion().getCouleur() != team){value += 5;}
            }
            // mobility feature
            // Value += possibleMoves;

            // evaluante distance to goal
            value = value*(8-row);
        }
        return value;
    }

//    public int getValue(Integer[][] board){
//        int victoire = 100;
//        boolean victoireNoir = false;
//        boolean victoireRouge = false;
//        int piecesNoir = 0;
//        int piecesRouges = 0;
//        int value = 0;
//        for (int x= 0; x < 8; x++){
//            for (int y = 0; y< 8; y++){
//                if (board[x][y]==0) continue;
//                if (board[x][y] ==4) // ROUGE MAX
//                {
//                    piecesRouges++;
//                    value += getPieceValue(board,x, y,4);
//                    if(y ==0){victoireRouge = true;}
//                    //if(y == 0){Value += HomeGroundValue;}
////                    if {(column > 0) ThreatA = (board[GetPosition(y - 1, 7).NoPieceOnSquare);}
////                    if (column < 7) ThreatB = (board.GetPosition(y + 1, 7).NoPieceOnSquare);
////                    if (ThreatA && ThreatB) // almost win
////                        board.Value += PieceAlmostWinValue;
//                } else{ // NOIR MIN
//                    // comme rouge
//                    piecesNoir++;
//                    value += getPieceValue(board,x, y,2);
//                    if(y ==7){victoireNoir = true;}
//                }
//            }
//        }
//        // if no more material available
//        if (piecesRouges == 0) victoireNoir = true;
//        if (piecesNoir == 0) victoireRouge = true;
//
//        // winning positions
//        if (victoireNoir){value -= victoire;}
//        if (victoireRouge){value += victoire;}
//
//        return value;
//    }
//
//    public int getPieceValue(Integer[][] board, int row, int column, int team)
//    {
//        int value = 0;
//
//        // add connections value//
//
//        if (team == 2){ //NOIR MIN
//            // add to the value the protected value
//            if (row > 0 && column > 0 && column < 7){
//                if(board[row-1][column-1]==team){value -= 5;}
//                if(board[row-1][column+1]==team){value -= 5;}
//            }
//            // evaluate attack
//            if (row < 7 && column > 0 && column < 7){
//                if(board[row+1][column-1]!=team){value += 5;}
//                if(board[row+1][column+1]!=team){value += 5;}
//            }
//
//            // evaluate block
//            if(row <= 5 && column > 0 && column < 7){
//                if(board[row+2][column-1]!=team){value -= 5;}
//                if(board[row+2][column]!=team){value -= 5;}
//                if(board[row+2][column-1]!=team){value -= 5;}
//            }
//
//            // mobility feature
//            // Value += possibleMoves;
//
//            // evalate distance to goal
//            value = value*(row+1);
//
//        }
//
//        else{ //rouge MAX
//            // add to the value the protected value
//            if (row < 7 && column > 0 && column < 7){
//                if(board[row+1][column-1]==team){value += 5;}
//                if(board[row+1][column+1]==team){value += 5;}
//            }
//            // evaluate attack
//            if (row > 0 && column > 0 && column < 7){
//                if(board[row-1][column-1]!=team){value -= 5;}
//                if(board[row-1][column+1]!=team){value -= 5;}
//            }
//            // evaluate block
//            if(row >= 2 && column > 0 && column < 7){
//                if(board[row-2][column-1]!=team){value += 5;}
//                if(board[row-2][column]!=team){value += 5;}
//                if(board[row-2][column-1]!=team){value += 5;}
//            }
//            // mobility feature
//            // Value += possibleMoves;
//
//            // evaluante distance to goal
//            value = value*(8-row);
//        }
//        return value;
//    }

    public ArrayList<String> generateurMouvement(HashMap<Point, Case> cases, Point position, int direction, boolean joueur) {
        Point depart = new Point(position.x, position.y);
        Point devant = new Point(position.x + direction, position.y);
        Point gauche = new Point(position.x + direction, position.y - 1);
        Point droite = new Point(position.x + direction, position.y + 1);

        ArrayList<String> mouvementsPossibles = new ArrayList<String>();

        if(!(direction == -1 && position.x + direction == 0) && !(direction == 1 && position.x + direction == 7)) {

            if (cases.get(devant).getPion().getCouleur() == null) {
                mouvementsPossibles.add("" + devant.x + devant.y);
            }
            if (depart.y != 0) {
                if (cases.get(gauche).getPion().getCouleur() == null || cases.get(gauche).getPion().getCouleur() != joueur) {
                    mouvementsPossibles.add("" + gauche.x + gauche.y);
                }
            }
            if (depart.y != 7) {
                if (cases.get(droite).getPion().getCouleur() == null || cases.get(droite).getPion().getCouleur() != joueur) {
                    mouvementsPossibles.add("" + droite.x + droite.y);
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
            this.cases.get(new Point(ancienX, ancienY)).setPion(new Pion(null, new Point(ancienX, ancienY), 0));
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
            this.cases.get(new Point(ancienX, ancienY)).setPion(new Pion(null, new Point(ancienX, ancienY), 0));
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
