import java.io.*;
import java.net.*;


class Client {
    public static final int PROFONDEUR = 5;
    public static void main(String[] args) {

        Socket MyClient;
        BufferedInputStream input;
        BufferedOutputStream output;
        int[][] board = new int[8][8];

        Jeu jeu = new Jeu();

        try {
            MyClient = new Socket("localhost", 8888);

            input    = new BufferedInputStream(MyClient.getInputStream());
            output   = new BufferedOutputStream(MyClient.getOutputStream());
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            while(1 == 1){
                char cmd = 0;

                cmd = (char)input.read();
                System.out.println(cmd);
                // Debut de la partie en joueur blanc
                if(cmd == '1'){
                    byte[] aBuffer = new byte[1024];

                    int size = input.available();
                    //System.out.println("size " + size);
                    input.read(aBuffer,0,size);
                    String s = new String(aBuffer).trim();

                    System.out.println(s);
                    String[] boardValues;
                    boardValues = s.split(" ");
                    int x=0,y=0;
                    for(int i=0; i<boardValues.length;i++){
                        board[y][x] = Integer.parseInt(boardValues[i]);
                        x++;
                        if(x == 8){
                            x = 0;
                            y++;
                        }
                    }
                    jeu = new Jeu(board, true);

                    System.out.println("Nouvelle partie! Vous jouer blanc, entrez votre premier coup : ");
                    Noeud racine = new Noeud(jeu.getPlateau(), 0, jeu.getPionsRouges(), jeu.getPionsNoirs());
                    Minmax minmax = new Minmax(racine);
                    String nextMove = minmax.findPositionChange(jeu, racine, PROFONDEUR, jeu.getMaCouleur());
                    System.out.println("Mon coup : " + nextMove);

                    output.write(nextMove.getBytes(),0,nextMove.length());
                    output.flush();
                    jeu.modifierPlateau(nextMove, jeu.getMaCouleur());
                }
                // Debut de la partie en joueur Noir
                if(cmd == '2'){
                    System.out.println("Nouvelle partie! Vous jouer noir, attendez le coup des blancs");
                    byte[] aBuffer = new byte[1024];

                    int size = input.available();
                    //System.out.println("size " + size);
                    input.read(aBuffer,0,size);
                    String s = new String(aBuffer).trim();

                    System.out.println(s);
                    String[] boardValues;
                    boardValues = s.split(" ");
                    int x=0,y=0;
                    for(int i=0; i<boardValues.length;i++){
                        board[y][x] = Integer.parseInt(boardValues[i]);
                        x++;
                        if(x == 8){
                            x = 0;
                            y++;
                        }
                    }
                    jeu = new Jeu(board, false);
                    //jeu.afficherPlateau(jeu.getPlateau());
                }

                // Le serveur demande le prochain coup
                // Le message contient aussi le dernier coup joue.
                if(cmd == '3'){
                    byte[] aBuffer = new byte[16];

                    int size = input.available();
                    System.out.println("size :" + size);
                    input.read(aBuffer,0,size);
                    String s = new String(aBuffer);
                    System.out.println("Dernier coup :"+ s);
                    String str = s.substring(1);
                    jeu.modifierPlateau(str, jeu.getCouleurAdverse());
                    System.out.println("Entrez votre coup : ");


                    Noeud racine = new Noeud(jeu.getPlateau(), 0, jeu.getPionsRouges(), jeu.getPionsNoirs());
                    Minmax minmax = new Minmax(racine);
                    String nextMove = minmax.findPositionChange(jeu, racine,PROFONDEUR,jeu.getMaCouleur());
                    System.out.println("Mon coup : " + nextMove);

                    output.write(nextMove.getBytes(),0,nextMove.length());
                    output.flush();
                    jeu.modifierPlateau(nextMove, jeu.getMaCouleur());

                }
                // Le dernier coup est invalide
                if(cmd == '4'){
                    System.out.println("Coup invalide, entrez un nouveau coup : ");
                    Noeud racine = new Noeud(jeu.getPlateau(), 0, jeu.getPionsRouges(), jeu.getPionsNoirs());
                    Minmax minmax = new Minmax(racine);
                    String nextMove = minmax.findPositionChange(jeu, racine,PROFONDEUR,jeu.getMaCouleur());
                    System.out.println("Mon coup : " + nextMove);
                    output.write(nextMove.getBytes(),0,nextMove.length());
                    output.flush();

                }
                // La partie est terminée
                if(cmd == '5'){
                    byte[] aBuffer = new byte[16];
                    int size = input.available();
                    input.read(aBuffer,0,size);
                    String s = new String(aBuffer);
                    System.out.println("Partie Terminé. Le dernier coup joué est: "+s);
                    String move = null;
                    move = console.readLine();
                    output.write(move.getBytes(),0,move.length());
                    output.flush();
                }
            }
        }
        catch (IOException e) {
            System.out.println(e);
        }

    }

}
