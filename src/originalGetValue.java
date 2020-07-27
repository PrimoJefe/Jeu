//using System;
//        2  using System.Collections.Generic;
//        3
//        4  namespace Breakthrough.Game.Engine
//        5  {
//        6  	internal static class BoardEvaluation
//  7  	{
//          8          internal const int WinValue = 500000;
//          9          internal const short PieceAlmostWinValue = 10000;
//          10          internal const short PieceValue = 1300;
//          11          internal const short PieceDangerValue = 10;
//          12          internal const short PieceHighDangerValue = 100;
//          13          internal const short PieceAttackValue = 50;
//          14          internal const short PieceProtectionValue = 65;
//          15          internal const short PieceConnectionHValue = 35;
//          16          internal const short PieceConnectionVValue = 15;
//          17          internal const short PieceColumnHoleValue = 20;
//          18          internal const short PieceHomeGroundValue = 10;
//          19
//          20          internal static void GetValue(Board board, GamePieceColor ColorMoving)
//        21          {
//        22  	        board.Value = 0;
//        23
//        24  	        // evaluate remaining pieces and the state of the game
//        25              int RemainingWhitePieces = 0;
//        26              int RemainingBlackPieces = 0;
//        27
//        28  	        // scan all squares
//        29              for (byte column = 0; column < 8; column++)
//        30              {
//        31                  int BlackPiecesOnColumn = 0;
//        32                  int WhitePiecesOnColumn = 0;
//        33
//        34                  for (byte row = 0; row < 8; row++)
//        35                  {
//        36
//        37                      int Position = GetPosition(column, row);
//        38                      BoardSquare square = board.BoardSquares[Position];
//        39
//        40                      if (square.CurrentPiece == null)
//        41                          continue;
//        42
//        43                      if (square.CurrentPiece.PieceColor == GamePieceColor.White)
//        44                      {
//        45                          RemainingWhitePieces++;
//        46                          WhitePiecesOnColumn++;
//        47                          board.Value += GetPieceValue(square, column, row);
//        48                          if (row == 7)
//        49                          { // winning position
//        50                              board.WhiteWins = true;
//        51                          }
//        52                          else if (row == 6)
//        53                          { // check almost win
//        54                              bool ThreatA = false;
//        55                              bool ThreatB = false;
//        56                              if (column > 0) ThreatA = (board.BoardSquares[GetPosition(column - 1, 7)].CurrentPiece == null);
//        57                              if (column < 7) ThreatB = (board.BoardSquares[GetPosition(column + 1, 7)].CurrentPiece == null);
//        58                              if (!(ThreatA && ThreatB)) // almost win
//        59                                  board.Value += PieceAlmostWinValue;
//        60                          }
//        61                          else if (row == 0)
//        62                          { // home ground
//        63                              board.Value += PieceHomeGroundValue;
//        64                          }
//        65                      }
//        66                      else
//        67                      {
//        68                          RemainingBlackPieces++;
//        69                          BlackPiecesOnColumn++;
//        70                          board.Value -= GetPieceValue(square, column, row);
//        71                          if (row == 0)
//        72                          { // winning position
//        73                              board.BlackWins = true;
//        74                          }
//        75                          else if (row == 1)
//        76                          { // check almost win
//        77                              bool ThreatA = false;
//        78                              bool ThreatB = false;
//        79                              if (column > 0) ThreatA = (board.BoardSquares[GetPosition(column - 1, 0)].CurrentPiece == null);
//        80                              if (column < 7) ThreatB = (board.BoardSquares[GetPosition(column + 1, 0)].CurrentPiece == null);
//        81                              if(!(ThreatA && ThreatB)) // almost win
//        82                                  board.Value -= PieceAlmostWinValue;
//        83                          }
//        84                          else if (row == 7)
//        85                          { // home ground
//        86                              board.Value -= PieceHomeGroundValue;
//        87                          }
//        88                      }
//        89                  }
//        90
//        91                  // Row hole feature
//        92                  if (WhitePiecesOnColumn == 0) board.Value -= PieceColumnHoleValue;
//        93                  if (BlackPiecesOnColumn == 0) board.Value += PieceColumnHoleValue;
//        94
//        95  	        }
//        96
//        97              // if no more material available
//        98              if (RemainingWhitePieces == 0) board.BlackWins = true;
//        99              if (RemainingBlackPieces == 0) board.WhiteWins = true;
//        100
//        101              // winning positions
//        102              if (board.WhiteWins) board.Value += WinValue;
//        103              if (board.BlackWins) board.Value -= WinValue;
//        104
//        105              // invert Value for Negamax
//        106              if (GetOppositeColor(ColorMoving) == GamePieceColor.Black)
//        107                  board.Value = -board.Value;
//        108          }
//        109
//        110
//        111  		/// <summary>
//        112  		/// Evaluates current piece value
//        113  		/// </summary>
//        114  		private static int GetPieceValue(BoardSquare square, byte Column, byte Row)
//        115  		{
//        116              int Value = PieceValue;
//        117  			var Piece = square.CurrentPiece;
//        118
//        119              // add connections value
//        120              if (Piece.ConnectedH) Value += PieceConnectionHValue;
//        121              if (Piece.ConnectedV) Value += PieceConnectionVValue;
//        122
//        123  			// add to the value the protected value
//        124  			Value += Piece.ProtectedValue;
//        125
//        126  			// evaluate attack
//        127  			if (Piece.AttackedValue > 0)
//        128              {
//        129                  Value -= Piece.AttackedValue;
//        130  				if (Piece.ProtectedValue == 0)
//        131  					Value -= Piece.AttackedValue;
//        132  			}else{
//        133  				if (Piece.ProtectedValue != 0)
//        134  				{
//        135  					// pawns at the end that are not attacked are worth more points
//        136  					if (Piece.PieceColor == GamePieceColor.White)
//        137  					{
//        138                          if (Row == 5) Value += PieceDangerValue;
//        139                          else if (Row == 6) Value += PieceHighDangerValue;
//        140  					}
//        141  					else
//        142  					{
//        143                          if (Row == 2) Value += PieceDangerValue;
//        144                          else if (Row == 1) Value += PieceHighDangerValue;
//        145  					}
//        146  				}
//        147  			}
//        148
//        149              // danger value
//        150              if (Piece.PieceColor == GamePieceColor.White)
//        151                  Value += Row * PieceDangerValue;
//        152              else
//        153                  Value += (8-Row) * PieceDangerValue;
//        154
//        155
//        156  			// mobility feature
//        157  			Value += Piece.ValidMoves.Count;
//        158
//        159  			return Value;
//        160  		}
//        161
//        162
//        163          #region Helper Methods
//        164          private static GamePieceColor GetOppositeColor(GamePieceColor color){ if (color == GamePieceColor.Black) return GamePieceColor.White; else return GamePieceColor.Black; }
//        165          private static byte ModifyDepth(byte depth, int PossibleMoves){if (PossibleMoves < 9) depth = (byte)(depth + 2); return depth;}
//        166  		private static int Sort(Board board1, Board board2){ return board1.Value - board2.Value; }
//        167          private static byte GetRow(byte position) { return (byte)(7 - (int)(position / 8)); }
//        168          private static byte GetColumn(byte position) { return (byte)(position % 8); }
//        169          private static int GetPosition(int column, int row) { return (7 - row) * 8 + column; }
//        170
//        171          #endregion
//        172
//        173
//        174  	}
//        175  }
//
//
//
//
///*public class originalGetValue {
//    int GetValue(board, ColorMoving){
//        for (byte x= 0; x < 8; x++){
//            for (byte y = 0; y< 8; y++){
//                BoardSquare square = board. GetPosition(x,y);
//                if (NoPieceOnSquare) continue;
//                if (square.CurrentPiece.PieceColor ==White)
//                {
//                    Value += GetPieceValue(square, x, y);
//                    if(y ==7)board.WhiteWins = true;
//                    if(y == 0)Value += HomeGroundValue;
//                    if (column > 0) ThreatA = (board[GetPosition(y - 1, 7).NoPieceOnSquare);
//                    if (column < 7) ThreatB = (board.GetPosition(y + 1, 7).NoPieceOnSquare);
//                    if (ThreatA && ThreatB) // almost win
//                        board.Value += PieceAlmostWinValue;
//                } else{
//                    // Same for black, with inverted signs for NegaMax
//                }
//            }
//            if (WhitePiecesOnColumn == 0)Value -= PieceColumnHoleValue;
//            if (BlackPiecesOnColumn == 0)Value += PieceColumnHoleValue;
//        }
//        // if no more material available
//        if (RemainingWhitePieces == 0) board.BlackWins = true;
//        if (RemainingBlackPieces == 0) board.WhiteWins = true;
//
//        // winning positions
//        if (board.WhiteWins)Value += WinValue;
//        if (board.BlackWins)Value -= WinValue;
//    }
//
//    int GetPieceValue(square, Column, Row)
//    {
//        int Value = PieceValue;
//        var Piece = square.CurrentPiece;
//
//        // add connections value
//        if (Piece.ConnectedH) Value += PieceConnectionHValue;
//        if (Piece.ConnectedV) Value += PieceConnectionVValue;
//
//        // add to the value the protected value
//        Value += Piece.ProtectedValue;
//
//        // evaluate attack
//        if (Piece.AttackedValue > 0)
//        {
//            Value -= Piece.AttackedValue;
//            if (Piece.ProtectedValue == 0)
//                Value -= Piece.AttackedValue;
//        }else{
//            if (Piece.ProtectedValue != 0){
//                if (Piece.PieceColor == White){
//                    if (Row == 5) Value += PieceDangerValue;
//                    else if (Row == 6) Value += PieceHighDangerValue;
//                }else{
//                    if (Row == 2) Value += PieceDangerValue;
//                    else if (Row == 1) Value += PieceHighDangerValue;
//                }
//            }
//        }
//        // danger value
//        if (Piece.PieceColor ==White)
//            Value += Row * PieceDangerValue;
//        else
//            Value += (8-Row) * PieceDangerValue;
//
//        // mobility feature
//        Value += Piece.ValidMoves.Count;
//
//        return Value;
//    }
//}*/
