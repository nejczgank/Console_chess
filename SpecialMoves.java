public class SpecialMoves {
    
    public static Figure[][] figureResurrection(Figure[][] board, int row, int col, int color) {
        if (row == 0 || row == 7) {
            board[row][col] = new Queen(board[row][col].getPos(), 9, color);
        }
        return board;
    }

    public static void enPassant() {
        
    }
}
