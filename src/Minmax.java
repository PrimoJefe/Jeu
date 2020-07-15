import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

public class Minmax {

    private Arbre tree;

    public void constructTree(Integer[][] initBoard) {
        tree = new Arbre();
        Noeud root = new Noeud(initBoard, true);
        tree.setRacine(root);
        //constructTree(root, 6, true);
    }

    private Noeud constructTree(Noeud parentNode, int depth, boolean joueurMax) {
        boolean isChildMaxPlayer = !parentNode.estJoueurMax();

        if(depth == 0){
            double valeur = Jeu.getValue(parentNode.getBoard());
            parentNode.setScore(valeur);
            return parentNode;
        }
        if(joueurMax){
            double maxEval = Double.NEGATIVE_INFINITY;
            Noeud maxNoeud = null;
            List<Integer[][]> listeMouvementsRouges = Jeu.generateurMouvement(parentNode.getBoard(), Jeu.getPionsRouges());
            for(int i=0; i<listeMouvementsRouges.size(); i++){
                Noeud enfant = new Noeud(listeMouvementsRouges.get(i),isChildMaxPlayer);
                Noeud valNoeud = constructTree(enfant,depth-1, isChildMaxPlayer);
                maxEval = Math.max(maxEval, valNoeud.getScore());
                if(maxEval == valNoeud.getScore()){
                    maxNoeud = valNoeud;
                }
            }
            return maxNoeud;
        } else{
            List<Integer[][]> listeMouvementsNoirs = Jeu.generateurMouvement(parentNode.getBoard(), Jeu.getPionsNoirs());
            for(int i=0; i<listeMouvementsNoirs.size(); i++){
                Noeud noeudEnfant = new Noeud(listeMouvementsNoirs.get(i), isChildMaxPlayer);
                parentNode.ajouterEnfant(noeudEnfant);
                if(depth > 0){
                    constructTree(noeudEnfant, depth-1, isChildMaxPlayer);
                }
            }
        }
        return checkWin(parentNode);
    }

    private Noeud checkWin(Noeud node) {
        List<Noeud> children = node.getEnfants();
        boolean isMaxPlayer = node.estJoueurMax();
        children.forEach(child -> {
            if (child.getDepth() == 0) {
                double score = Jeu.getValue(child.getBoard());
                child.setScore(score);
            } else {
                checkWin(child);
            }
        });
        Noeud bestChild = trouverBestEnfant(isMaxPlayer, children);
        node.setScore(bestChild.getScore());

        return bestChild;
    }

    public Noeud trouverBestEnfant(boolean estJoueurMax, List<Noeud> enfants){
        Comparator<Noeud> comparerParScore = Comparator.comparing(Noeud::getScore);
        return enfants.stream()
                .max(estJoueurMax ? comparerParScore : comparerParScore.reversed())
                .orElseThrow(NoSuchElementException::new);
    }



    public Noeud minMax(Noeud parentNode, int depth, boolean joueurMax) {

        boolean isChildMaxPlayer = !parentNode.estJoueurMax();

        if(depth == 0){
            double valeur = Jeu.getValue(parentNode.getBoard());
            parentNode.setScore(valeur);
            return parentNode;
        }
        if(joueurMax){
            double maxEval = Double.NEGATIVE_INFINITY;
            Noeud maxNoeud = null;
            List<Integer[][]> listeMouvementsRouges = Jeu.generateurMouvement(parentNode.getBoard(), Jeu.getPionsRouges());
            for(int i=0; i<listeMouvementsRouges.size(); i++){
                Noeud enfant = new Noeud(listeMouvementsRouges.get(i),isChildMaxPlayer);
                parentNode.ajouterEnfant(enfant);
                Noeud valNoeud = minMax(enfant,depth-1, isChildMaxPlayer);
                maxEval = Math.max(maxEval, valNoeud.getScore());
                if(maxEval == valNoeud.getScore()){
                    maxNoeud = valNoeud;
                }
            }
            return maxNoeud;
        } else{
            double minEval = Double.POSITIVE_INFINITY;
            Noeud minNoeud = null;
            List<Integer[][]> listeMouvementsRouges = Jeu.generateurMouvement(parentNode.getBoard(), Jeu.getPionsRouges());
            for(int i=0; i<listeMouvementsRouges.size(); i++){
                Noeud enfant = new Noeud(listeMouvementsRouges.get(i),isChildMaxPlayer);
                parentNode.ajouterEnfant(enfant);
                Noeud valNoeud = minMax(enfant,depth-1, isChildMaxPlayer);
                minEval = Math.min(minEval, valNoeud.getScore());
                if(minEval == valNoeud.getScore()){
                    minNoeud = valNoeud;
                }
            }
            return minNoeud;
        }
    }


}
