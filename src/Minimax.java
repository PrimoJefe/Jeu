import java.awt.*;
import java.util.*;

public class Minimax {

    private Noeud racine;

    /*
     * CONSTRUCTEUR
     */
    public Minimax(Noeud root){
        this.racine = root;
    }

    /*
     * METHODES
     */
    public Noeud minimax(Jeu jeu, Noeud noeud, int profondeur, int alpha, int beta, int joueur) {
        int[][] plateau = noeud.getPlateau();
        ArrayList<Pion> pionsIn = new ArrayList<>(joueur == 4 ? noeud.getPionsRouges() : noeud.getPionsNoirs());
        ArrayList<Pion> pionsOut = new ArrayList<>(joueur == 4 ? noeud.getPionsNoirs() : noeud.getPionsRouges());;

        ArrayList<String> deplacements = new ArrayList<String>();

        int eval;
        int minEval;
        int maxEval;

        Noeud noeudEnfant = new Noeud(plateau, noeud.getScore(), pionsIn, pionsOut);

        if (profondeur == 0) {
            int valeur = jeu.evaluerPlateau(noeudEnfant.getPlateau());
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
                ArrayList<Pion> pionsInEnfant = jeu.clonerListe(pionsIn);
                ArrayList<Pion> pionsOutEnfant = jeu.clonerListe(pionsOut);

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
                ArrayList<Pion> pionsInEnfant = jeu.clonerListe(pionsIn);
                ArrayList<Pion> pionsOutEnfant = jeu.clonerListe(pionsOut);

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

    public String trouverDeplacement(Jeu jeu, Noeud parentNode, int depth, int joueurMax){
        String positionCourante = "";
        String nouvellePosition = "";
        int ancienX = -1, ancienY = -1, nouveauX = -1, nouveauY=-1;

        Noeud noeudMouvement = minimax(jeu, parentNode, depth, -1000000000, 1000000000, joueurMax);

        int[][] plateauCourant = parentNode.getPlateau();
        int[][] prochainPlateau = noeudMouvement.getPlateau();

        boolean nouveau = false;
        boolean ancien = false;

        for(int i = 0; i < plateauCourant.length; i++){
            if(nouveau && ancien){
                break;
            }
            for(int j = 0; j < plateauCourant.length; j++){
                if(nouveau && ancien){
                    break;
                }
                if(plateauCourant[i][j] != prochainPlateau[i][j]) {
                    if(prochainPlateau[i][j] != 0) {
                        nouveauX = i;
                        nouveauY = j;
                        nouveau = true;
                    }
                    else if(prochainPlateau[i][j] == 0) {
                        ancienX = i;
                        ancienY = j;
                        ancien = true;
                    }
                }
            }
        }
        positionCourante += conversionChiffreEnLettre(ancienY);
        positionCourante += conversionChiffreEnChiffre(ancienX);

        nouvellePosition += conversionChiffreEnLettre(nouveauY);
        nouvellePosition += conversionChiffreEnChiffre(nouveauX);

        jeu.setPlateau(noeudMouvement.getPlateau());
        jeu.setPionsRouges(noeudMouvement.getPionsRouges());
        jeu.setPionsNoirs(noeudMouvement.getPionsNoirs());

        return positionCourante + " - " + nouvellePosition;
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
