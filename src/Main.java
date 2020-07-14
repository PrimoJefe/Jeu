public class Main {


    public static void main(String[] args) {
	    System.out.println(2);
        int[][] board = new int[8][8];
        String[] boardValues = {"2","2","2","2","2","2","2","2","2","2","2","2","2","2","2","2","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","4","4","4","4","4","4","4","4","4","4","4","4","4","4","4","4"};
        int x=0,y=0;
        for(int i=0; i<boardValues.length;i++){

            board[x][y] = Integer.parseInt(boardValues[i]);
            x++;
            if(x == 8){
                x = 0;
                y++;
            }
        }
        System.out.println("done");

        GetValue(board,"Black");

    }

    // 2 = noir (MIN)  4 = rouge (MAX)
    // [ligne][column]
    // [0][0] = [8][A]

    private static int GetValue(int[][]board, String joueur){
        int victoire = 100;
        boolean victoireNoir = false;
        boolean victoireRouge = false;
        int piecesNoir = 0;
        int piecesRouges = 0;
        int value = 0;
        for (int x= 0; x < 8; x++){
            for (int y = 0; y< 8; y++){
                if (board[x][y]==0) continue;
                if (board[x][y] ==4) // ROUGE MAX
                {
                    piecesRouges++;
                    value += GetPieceValue(board,x, y,4);
                    if(y ==0){victoireRouge = true;}
                    //if(y == 0){Value += HomeGroundValue;}
//                    if {(column > 0) ThreatA = (board[GetPosition(y - 1, 7).NoPieceOnSquare);}
//                    if (column < 7) ThreatB = (board.GetPosition(y + 1, 7).NoPieceOnSquare);
//                    if (ThreatA && ThreatB) // almost win
//                        board.Value += PieceAlmostWinValue;
                } else{ // NOIR MIN
                    // comme rouge
                    piecesNoir++;
                    value += GetPieceValue(board,x, y,2);
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

    private static int GetPieceValue(int[][]board, int row,int column,int team)
    {
        int value = 0;

        // add connections value//

        if (team == 2){ //NOIR MIN
            // add to the value the protected value
            if (row>0){
                if(board[row-1][column-1]==team){value -= 5;}
                if(board[row-1][column+1]==team){value -= 5;}
            }
            // evaluate attack
            if(board[row+1][column-1]!=team){value += 5;}
            if(board[row+1][column+1]!=team){value += 5;}

            // evaluate block
            if(row<=5){
                if(board[row+2][column-1]!=team){value -= 5;}
                if(board[row+2][column]!=team){value -= 5;}
                if(board[row+2][column-1]!=team){value -= 5;}
            }

            // mobility feature
            // Value += possibleMoves;

            // evalate distance to goal
            value = value*(row+1);

        }

        else{ //rouge MAX
            // add to the value the protected value
            if (row<7){
                if(board[row+1][column-1]==team){value += 5;}
                if(board[row+1][column+1]==team){value += 5;}
            }
            // evaluate attack
            if(board[row-1][column-1]!=team){value -= 5;}
            if(board[row-1][column+1]!=team){value -= 5;}

            // evaluate block
            if(row>=2){
                if(board[row-2][column-1]!=team){value += 5;}
                if(board[row-2][column]!=team){value += 5;}
                if(board[row-2][column-1]!=team){value += 5;}
            }
            // mobility feature
            // Value += possibleMoves;

            // evaluante distance to goal
            value = value*(8-row);


        }

        return value;
    }

    public static int[][] generateurMouvement(int[][] board, int row, int col){

        int player = board[row][col];
        int[][] possibleMoves = new int[1][3];

        if(player == 2){

            for (int j = col - 1, c = 0; j <= col + 1; j++, c++) {
                if (board[row-1][j] == 2) {
                    //These are your teammates, don't eat, can't jump over either
                    possibleMoves[0][c] = -1;
                } else if(board[row-1][j] == 4){
                    //Red is in front of you -> EAT
                    if(c != 1){
                        //Move current position to new position
                        possibleMoves[0][c] = board[row-1][j];
                    }
                } else if(board[row-1][j] == 0){
                    //Empty space, move down
                    possibleMoves[0][c] = board[row-1][j];
                }
            }

        } else if(player == 4){

            for (int j = col - 1, c = 0; j <= col + 1; j++, c++) {
                if (board[row+1][j] == 2) {
                    //These are your teammates, don't eat, can't jump over either
                    possibleMoves[0][c] = -1;
                } else if(board[row+1][j] == 4){
                    //Red is in front of you -> EAT
                    if(c != 1){
                        //Move current position to new position
                        possibleMoves[0][c] = board[row+1][j];
                    }
                } else if(board[row-1][j] == 0){
                    //Empty space, move down
                    possibleMoves[0][c] = board[row+1][j];
                }
            }
        }
        return possibleMoves;
    }

}
