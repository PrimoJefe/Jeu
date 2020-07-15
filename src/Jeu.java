import java.awt.*;
import java.util.ArrayList;


public class Jeu {

    //private static final int NOIR = 2;
    //private static final int ROUGE = 4;
    private static final int A = 1;
    private static final int B = 2;
    private static final int C = 3;
    private static final int D = 4;
    private static final int E = 5;
    private static final int F = 6;
    private static final int G = 7;
    private static final int H = 8;

    private Integer[][] plateau;
    private boolean maCouleur;
    private boolean couleurAdverse;
    private ArrayList<Pion> pionsRouges;
    private ArrayList<Pion> pionsNoirs;

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

    public ArrayList<Pion> getPionsRouges() {
        return this.pionsRouges;
    }

    public ArrayList<Pion> getPionsNoirs() {
        return this.pionsNoirs;
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
}
