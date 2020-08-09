import java.awt.*;
import java.util.*;

public class Minmax {

    private Noeud racine;

    public Minmax(Noeud root){
        this.racine = root;
    }
    public Noeud minimax(Jeu jeu, Noeud noeud, int profondeur, int alpha, int beta, int joueur) {
        int[][] plateau = noeud.getPlateau();
        ArrayList<Pion> pionsIn = new ArrayList<>(joueur == 4 ? noeud.getPionsRouges() : noeud.getPionsNoirs());
        ArrayList<Pion> pionsOut = new ArrayList<>(joueur == 4 ? noeud.getPionsNoirs() : noeud.getPionsRouges());;

        ArrayList<String> deplacements = new ArrayList<String>();


        int eval;
        int minEval;
        int maxEval;

        Noeud noeudEnfant = new Noeud(plateau, noeud.getScore(), pionsIn, pionsOut);
        int i = 0;

        if (profondeur == 0) {
            int valeur = jeu.getValue(noeudEnfant.getPlateau());
            noeud.setScore(valeur);
            return noeud;
        }

        for (Pion pion : pionsIn) {
            Point position = pion.getPosition();

            deplacements.addAll(jeu.generateurMouvement(plateau, position, pion.getDirection(), joueur));
        }

        if (joueur == 4) {
            maxEval = -1000000000;
            Noeud temp = new Noeud(plateau, noeud.getScore(), pionsIn, pionsOut);

            for (String deplacement : deplacements) {
                Point positionDepart = new Point(Character.getNumericValue(deplacement.charAt(0)),
                        Character.getNumericValue(deplacement.charAt(1)));

                Point nouvellePosition = new Point(Character.getNumericValue(deplacement.charAt(2)),
                        Character.getNumericValue(deplacement.charAt(3)));

                int[][] plateauEnfant = jeu.copierTableau(plateau);
                ArrayList<Pion> pionsInEnfant = jeu.cloneListe(pionsIn);
                ArrayList<Pion> pionsOutEnfant = jeu.cloneListe(pionsOut);

                for(Pion pion : pionsInEnfant) {
                    if(pion.getPosition().equals(positionDepart)) {
                        pion.setPosition(nouvellePosition);
                        break;
                    }
                }

                if (plateauEnfant[nouvellePosition.x][nouvellePosition.y] != 0) {
                    for(Pion pion : pionsOutEnfant) {
                        if(pion.getPosition().equals(nouvellePosition)) {
                            pionsOutEnfant.remove(pion);
                            break;
                        }
                    }
                }
                plateauEnfant[nouvellePosition.x][nouvellePosition.y] = joueur;
                plateauEnfant[positionDepart.x][positionDepart.y] = 0;

                if (nouvellePosition.x == 0){
                    profondeur = 1;
                }

                noeudEnfant = new Noeud(plateauEnfant, maxEval, pionsInEnfant, pionsOutEnfant);
                Noeud noeudMax = minimax(jeu, noeudEnfant, profondeur - 1, alpha, beta, 2);
                eval = noeudMax.getScore();

                if (eval > maxEval) {
                    temp = noeudEnfant;
                }
                maxEval = Math.max(maxEval, eval);

                alpha = Math.max(alpha, noeudMax.getScore());

                if (beta <= alpha) {
                    break;
                }
            }

            if (maxEval > 100000) {
                maxEval = Math.floorDiv(maxEval,2);
            }

            return (new Noeud(temp.getPlateau(), maxEval, temp.getPionsRouges(), temp.getPionsNoirs()));
        }
        else {
            minEval = 1000000000;

            Noeud temp = new Noeud(plateau, noeud.getScore(), pionsOut, pionsIn);
            Collections.reverse(deplacements);
            for(String deplacement : deplacements) {

                Point positionDepart = new Point(Character.getNumericValue(deplacement.charAt(0)),
                        Character.getNumericValue(deplacement.charAt(1)));

                Point nouvellePosition = new Point(Character.getNumericValue(deplacement.charAt(2)),
                        Character.getNumericValue(deplacement.charAt(3)));

                int[][] plateauEnfant = jeu.copierTableau(plateau);
                ArrayList<Pion> pionsInEnfant = jeu.cloneListe(pionsIn);
                ArrayList<Pion> pionsOutEnfant = jeu.cloneListe(pionsOut);

                for(Pion pion : pionsInEnfant) {
                    if(pion.getPosition().equals(positionDepart)) {
                        pion.setPosition(nouvellePosition);
                        break;
                    }
                }

                if (plateauEnfant[nouvellePosition.x][nouvellePosition.y] != 0) {
                    for(Pion pion : pionsOutEnfant) {
                        if(pion.getPosition().equals(nouvellePosition)) {
                            pionsOutEnfant.remove(pion);
                            break;
                        }
                    }
                }
                plateauEnfant[nouvellePosition.x][nouvellePosition.y] = joueur;
                plateauEnfant[positionDepart.x][positionDepart.y] = 0;

                if (nouvellePosition.x == 7){
                    profondeur = 1;
                }

                noeudEnfant = new Noeud(plateauEnfant, minEval, pionsOutEnfant, pionsInEnfant);
                Noeud noeudMin = minimax(jeu, noeudEnfant, profondeur - 1, alpha, beta, 4);
                eval = noeudMin.getScore();

                if (eval < minEval) {
                    temp = noeudEnfant;
                }
                minEval = Math.min(minEval, eval);

                beta = Math.min(beta, noeudMin.getScore());

                if (beta <= alpha) {
                    break;
                }
            }
            if (minEval < -100000) {
                minEval = Math.floorDiv(minEval, 2);
            }
            return (new Noeud(temp.getPlateau(), minEval, temp.getPionsRouges(), temp.getPionsNoirs()));
        }

    }

    public String findPositionChange(Jeu jeu, Noeud parentNode, int depth, int joueurMax){
        String currentPosition = "";
        String newPosition = "";
        int oldX=-1,oldY=-1, newX=-1, newY=-1;


        Noeud noeudNextMove = minimax(jeu, parentNode, depth, -1000000000, 1000000000, joueurMax);

        int[][] currentMoveBoard = parentNode.getPlateau();
        int[][] nextMoveBoard = noeudNextMove.getPlateau();

        boolean nouveau = false;
        boolean ancien = false;

        for(int i = 0; i < currentMoveBoard.length; i++){
            if(nouveau && ancien){
                break;
            }
            for(int j = 0; j < currentMoveBoard.length; j++){
                if(nouveau && ancien){
                    break;
                }
                if(currentMoveBoard[i][j] != nextMoveBoard[i][j]) {
                    if(nextMoveBoard[i][j] != 0) {
                        newX = i;
                        newY = j;
                        nouveau = true;
                    }
                    else if(nextMoveBoard[i][j] == 0) {
                        oldX = i;
                        oldY = j;
                        ancien = true;
                    }
                }
            }
        }
        currentPosition += conversionChiffreEnLettre(oldY);
        currentPosition += conversionChiffreEnChiffre(oldX);

        newPosition += conversionChiffreEnLettre(newY);
        newPosition += conversionChiffreEnChiffre(newX);

        jeu.setPlateau(noeudNextMove.getPlateau());
        jeu.setPionsRouges(noeudNextMove.getPionsRouges());
        jeu.setPionsNoirs(noeudNextMove.getPionsNoirs());

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
