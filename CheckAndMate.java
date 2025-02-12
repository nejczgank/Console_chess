import java.util.HashMap;
import java.util.ArrayList;

public class CheckAndMate {

    private static King findKing(Figure[][] board, int color) {

        for(int i=0; i<board.length; i++) {
            for(int j=0; j<board[i].length; j++) {
                if(board[i][j] != null && board[i][j].getColor() == color && board[i][j].getType() == "King") {
                    return (King) board[i][j];
                }
            }
        }

        throw new IllegalStateException("No king found");
    }

    public static Object[] findAtkPiece(Figure[][] board, int color, int oppositeColor) {

        King enemyKing = findKing(board, oppositeColor);
        int[] kingCoords = Conversion.convertPosToIndex(enemyKing.getPos());
        
        //principal directions
        int directions[][] = {
            {1, 0}, {1, 1}, {0, 1}, {-1, 1},
            {-1, 0}, {-1, -1}, {0, -1}, {1, -1}
        };

        for (int[] dir : directions) {
            ArrayList<String> attackPath = new ArrayList<>();
            for (int j = 1; j < 8; j++) {
                int row = kingCoords[0] + j * dir[0];
                int col = kingCoords[1] + j * dir[1];
                if (row < 0 || row >= 8 || col < 0 || col >= 8) {
                    break;
                }
                Figure piece = board[row][col];
                if (piece != null) {
                    if (piece.getColor() == oppositeColor) break; //ally piece blocks
                    if (piece.getColor() == color) {
                        //check if this piece can attack the king
                        ArrayList<String> enemyMoves = MovesHandling.findMoves(piece, board);
                        if (enemyMoves.contains(enemyKing.getPos())) {
                            attackPath.add(Conversion.convertIndexToPos(row, col));
                            Object[] obj = {(Figure) piece, attackPath};
                            return obj;
                        }
                    }
                    break;
                }
                attackPath.add(Conversion.convertIndexToPos(row, col));
            }
        }

        //knight check
        int[][] knightMoves = {
            {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
            {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };

        for (int[] move : knightMoves) {
            int row = kingCoords[0] + move[0];
            int col = kingCoords[1] + move[1];
            if (row >= 0 && row < 8 && col >= 0 && col < 8 &&
                board[row][col] != null &&
                board[row][col].getColor() == color &&
                board[row][col] instanceof Knight) {
                Object[] obj = {(Figure) board[row][col], Conversion.convertIndexToPos(row, col)};
                return obj;
            }
        }

        return null;
    }

    private static ArrayList<String> kingMoves(Figure[][] board, int oppositeColor, int color) {

        King king = findKing(board, oppositeColor);
        int[] kingCoords = Conversion.convertPosToIndex(king.getPos());
        ArrayList<String> eligibleMoves = MovesHandling.checkKingMoves(king, king.getColor(), kingCoords[0], kingCoords[1], board);
        
        if(eligibleMoves.isEmpty()) {
            return null;
        }

        //find the attacking figures path and if it intersects with the kings eligible moves, exclude those moves
        ArrayList<String> attackPath = (ArrayList<String>) findAtkPiece(board, color, oppositeColor)[1];
        Figure attackFigure = (Figure) findAtkPiece(board, color, oppositeColor)[0];

        String type = attackFigure.getType();
        switch (type) {
            case "Bishop":
                attackPath = MovesHandling.checkBishopMoves(color, attackFigure, board);
                break;
            case "Rook":
                attackPath = MovesHandling.checkRookMoves(color, attackFigure, board);
                break;
            case "Queen":
                attackPath = MovesHandling.checkQueenMoves(color, attackFigure, board);
                break;
            default:
                break;
        }

        eligibleMoves.removeAll(attackPath);

        //find if any of the remaining moves have enemy pieces applying check on those squares
        //if so exclude those moves as well
        for(int i=0; i<eligibleMoves.size(); i++) {
            int[] tempKingCoords = Conversion.convertPosToIndex(eligibleMoves.get(i));

            board[tempKingCoords[0]][tempKingCoords[1]] = king;
            board[kingCoords[0]][kingCoords[1]] = null;
            String originalKingPos = king.getPos();
            king.setPos(eligibleMoves.get(i));

            Object[] attackDetails = findAtkPiece(board, color, oppositeColor);
            if (attackDetails == null) {
                king.setPos(originalKingPos);
                board[tempKingCoords[0]][tempKingCoords[1]] = null;
                board[kingCoords[0]][kingCoords[1]] = king;
                continue;
            }
            attackPath = (ArrayList<String>) attackDetails[1];
            if(attackPath == null) {
                king.setPos(originalKingPos);
                board[tempKingCoords[0]][tempKingCoords[1]] = null;
                board[kingCoords[0]][kingCoords[1]] = king;
                continue;
            }
            attackFigure = (Figure) attackDetails[0];
            if(attackFigure == null) {
                king.setPos(originalKingPos);
                board[tempKingCoords[0]][tempKingCoords[1]] = null;
                board[kingCoords[0]][kingCoords[1]] = king;
                continue;
            }

            type = attackFigure.getType();

            switch (type) {
                case "Bishop":
                    attackPath = MovesHandling.checkBishopMoves(color, attackFigure, board);
                    break;
                case "Rook":
                    attackPath = MovesHandling.checkRookMoves(color, attackFigure, board);
                    break;
                case "Queen":
                    attackPath = MovesHandling.checkQueenMoves(color, attackFigure, board);
                    break;
                default:
                    break;
            }

            eligibleMoves.removeAll(attackPath);
            king.setPos(originalKingPos);
            
            board[tempKingCoords[0]][tempKingCoords[1]] = null;
            board[kingCoords[0]][kingCoords[1]] = king;
        }

        if(eligibleMoves.isEmpty() || eligibleMoves == null) {
            return null;
        }

        return eligibleMoves;
    }
    
    private static HashMap<String, ArrayList<String>> pieceCheckmateBlock(Figure[][] board, int oppositeColor, int color) {

        //find the attack path array
        Object[] attackDetails = findAtkPiece(board, color, oppositeColor);
    
        if (attackDetails == null) {
            return null;
        }
    
        ArrayList<String> attackPath = (ArrayList<String>) attackDetails[1];
    
        if (attackPath == null || attackPath.isEmpty()) {
            return null;
        }
    
        //map blocking pieces (like the bishop) and their valid moves
        HashMap<String, ArrayList<String>> blockingPieces = new HashMap<>();
    
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != null && board[i][j].getColor() == oppositeColor && board[i][j].getType() != "King") {
                    ArrayList<String> possibleMoves = MovesHandling.findMoves(board[i][j], board);
    
                    //retain only moves that intersect with the attack path
                    possibleMoves.retainAll(attackPath);
    
                    if (!possibleMoves.isEmpty()) {
                        //Use the blocking piece as the key in the new map
                        blockingPieces.put(board[i][j].getPos(), new ArrayList<>(possibleMoves));
                    }
                }
            }
        }

        return blockingPieces;
    }
    
    public static HashMap<String, ArrayList<String>> combineMovesHelper(Figure[][] board, int oppositeColor, int color) {

        HashMap<String, ArrayList<String>> blockingPieces = pieceCheckmateBlock(board, oppositeColor, color);
        if(kingMoves(board, oppositeColor, color) != null) {
            ArrayList<String> kingMoves = kingMoves(board, oppositeColor, color);
            blockingPieces.put(findKing(board, oppositeColor).getPos(), kingMoves);
        }
        
        System.out.println(blockingPieces);
        return blockingPieces;
    }
}