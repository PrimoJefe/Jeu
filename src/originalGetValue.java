public class originalGetValue {
    int GetValue(board, ColorMoving){
        for (byte x= 0; x < 8; x++){
            for (byte y = 0; y< 8; y++){
                BoardSquare square = board. GetPosition(x,y);
                if (NoPieceOnSquare) continue;
                if (square.CurrentPiece.PieceColor ==White)
                {
                    Value += GetPieceValue(square, x, y);
                    if(y ==7)board.WhiteWins = true;
                    if(y == 0)Value += HomeGroundValue;
                    if (column > 0) ThreatA = (board[GetPosition(y - 1, 7).NoPieceOnSquare);
                    if (column < 7) ThreatB = (board.GetPosition(y + 1, 7).NoPieceOnSquare);
                    if (ThreatA && ThreatB) // almost win
                        board.Value += PieceAlmostWinValue;
                } else{
                    // Same for black, with inverted signs for NegaMax
                }
            }
            if (WhitePiecesOnColumn == 0)Value -= PieceColumnHoleValue;
            if (BlackPiecesOnColumn == 0)Value += PieceColumnHoleValue;
        }
        // if no more material available
        if (RemainingWhitePieces == 0) board.BlackWins = true;
        if (RemainingBlackPieces == 0) board.WhiteWins = true;

        // winning positions
        if (board.WhiteWins)Value += WinValue;
        if (board.BlackWins)Value -= WinValue;
    }

    int GetPieceValue(square, Column, Row)
    {
        int Value = PieceValue;
        var Piece = square.CurrentPiece;

        // add connections value
        if (Piece.ConnectedH) Value += PieceConnectionHValue;
        if (Piece.ConnectedV) Value += PieceConnectionVValue;

        // add to the value the protected value
        Value += Piece.ProtectedValue;

        // evaluate attack
        if (Piece.AttackedValue > 0)
        {
            Value -= Piece.AttackedValue;
            if (Piece.ProtectedValue == 0)
                Value -= Piece.AttackedValue;
        }else{
            if (Piece.ProtectedValue != 0){
                if (Piece.PieceColor == White){
                    if (Row == 5) Value += PieceDangerValue;
                    else if (Row == 6) Value += PieceHighDangerValue;
                }else{
                    if (Row == 2) Value += PieceDangerValue;
                    else if (Row == 1) Value += PieceHighDangerValue;
                }
            }
        }
        // danger value
        if (Piece.PieceColor ==White)
            Value += Row * PieceDangerValue;
        else
            Value += (8-Row) * PieceDangerValue;

        // mobility feature
        Value += Piece.ValidMoves.Count;

        return Value;
    }
}
