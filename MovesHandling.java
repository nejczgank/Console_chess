import java.util.ArrayList;

class MovesHandling {
    
    public static Figure selectFigure(String selectPos, int color, Figure[][] board) {

        Figure selectedFigure = null;

        for(int i=0; i<board.length; i++) {
            for(int j=0; j<board[i].length; j++) {
                if(board[i][j] instanceof Figure && board[i][j].getPos().equals(selectPos) && board[i][j].getColor() == color) {
                    selectedFigure = board[i][j];
                    return selectedFigure;
                }
            }
        }
        return null;
    }
    
    //need to add so that if a figure is selected that has no legal turns your selection retries
    public static ArrayList<String> findMoves(Figure selectedFigure, Figure[][] board) {

        String type = selectedFigure.getType();
        int color = selectedFigure.getColor();

        ArrayList<String> possibleMoves = new ArrayList<>();
        int[] rowCol = Conversion.convertPosToIndex(selectedFigure);

        switch (type) {
            case "Pawn":
                if (selectedFigure instanceof Pawn) {
                    Pawn pawn = (Pawn) selectedFigure;
                    possibleMoves = checkPawnMoves(pawn, color, rowCol[0], rowCol[1], board);
                }
                break;
            case "Knight":
                possibleMoves = checkKnightMoves(color, rowCol[0], rowCol[1], board);
                break;
            case "Bishop":
                possibleMoves = checkBishopMoves(color, rowCol[0], rowCol[1], board);
                break;
            case "Rook":
                possibleMoves = checkRookMoves(color, rowCol[0], rowCol[1], board);
                break;
            case "Queen":
                possibleMoves = checkQueenMoves(color, rowCol[0], rowCol[1], board);
                break;
            case "King":
                if (selectedFigure instanceof King) {
                    King king = (King) selectedFigure;
                    possibleMoves = checkKingMoves(king, color, rowCol[0], rowCol[1], board);
                }
                break;
            default:
                break;
        }

        return possibleMoves;
    }

    private static ArrayList<String> checkPawnMoves(Pawn selectedFigure, int color, int rowNum, int colNum, Figure[][] board) {
        
        ArrayList<String> validPos = new ArrayList<>();

        int newRow;
        int newCol;
        int playerTurns = (color == 0) ? 1 : -1;

        //normal move
        newRow = rowNum - (1*playerTurns);
        newCol = colNum;

        if(newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
            if(board[newRow][newCol] == null) {
                validPos.add(Conversion.convertIndexToPos(newRow, newCol));
            }
            else if(board[newRow][newCol].getColor() == color && board[newRow][newCol].getColor() != color) {
                validPos.add(Conversion.convertIndexToPos(newRow, newCol));
            }
        }

        //initial double move
        if(selectedFigure.getInitPos() == true) {
            if(board[newRow][newCol] == null && board[newRow-(1*playerTurns)][newCol] == null) {
                validPos.add(Conversion.convertIndexToPos(newRow-(1*playerTurns), newCol));
            }
        }

        //diagonal capture moves
        int[][] diagonalMoves = {{1, -1}, {1, 1}};
        for(int[] move : diagonalMoves) {
            newRow = rowNum - (move[0]*playerTurns);
            newCol = colNum - (move[1]);

            if(newRow >= 0 && newRow<8 && newCol >= 0 && newCol <8) {
                if(board[newRow][newCol] != null && board[newRow][newCol].getColor() != color) {
                    validPos.add(Conversion.convertIndexToPos(newRow, newCol));
                }
            }
        }

        //en-passant
        if(selectedFigure.getEnPassantLeft() == true) {
            int[] passantMoves = {1, 1};
            newRow = rowNum - (passantMoves[0]*playerTurns);
            newCol = colNum - (passantMoves[1]);
            if(newRow >= 0 && newRow<8 && newCol >= 0 && newCol <8) {
                if(board[newRow][newCol] == null) {
                    validPos.add(Conversion.convertIndexToPos(newRow, newCol));
                    selectedFigure.setEnPassantLeft(false);
                }
                else {
                    selectedFigure.setEnPassantLeft(false);
                }
            }
        }
        if(selectedFigure.getEnPassantRight() == true) {
            int[] passantMoves = {1, -1};
            newRow = rowNum - (passantMoves[0]*playerTurns);
            newCol = colNum - (passantMoves[1]);
            if(newRow >= 0 && newRow<8 && newCol >= 0 && newCol <8) {
                if(board[newRow][newCol] == null) {
                    validPos.add(Conversion.convertIndexToPos(newRow, newCol));
                    selectedFigure.setEnPassantRight(false);
                }
                else {
                    selectedFigure.setEnPassantRight(false);
                }
            }
        }
        //če en igralec premakne kmeta 2+1 in nasprotini točno 2, tako da sta si sosednja,
        //potem lahko prvotni igralcec zbije nasprotnikovega kmeta tako da se premakne diagonalno uzadi njega 

        return validPos;
    }
    
    private static ArrayList<String> checkKnightMoves(int color, int rowNum, int colNum, Figure[][] board) {

        ArrayList<String> validPos = new ArrayList<>();
        
        //normal move
        int[][] moves = {
            {2, 1}, {1, 2}, {-1, 2}, {-2, 1}, 
            {-2, -1}, {-1, -2}, {1, -2}, {2, -1}
        };
    
        for (int[] move : moves) {
            int newRow = rowNum - move[0];
            int newCol = colNum - move[1];
    
            if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                if (board[newRow][newCol] == null) {
                    validPos.add(Conversion.convertIndexToPos(newRow, newCol));
                } else if (board[newRow][newCol].getColor() != color) {
                    validPos.add(Conversion.convertIndexToPos(newRow, newCol));
                }
            }
        }
        return validPos;
    }
    
    private static ArrayList<String> checkBishopMoves(int color, int rowNum, int colNum, Figure[][] board) {

        ArrayList<String> validPos = new ArrayList<>();

        int[][] movePrefixes = {
            {1, 1}, {-1, 1}, {-1, -1}, {1, -1}
        };

        //four directions of movement
        for(int i=0; i<4; i++) {
            int rowPrefix = movePrefixes[i][0];
            int colPrefix = movePrefixes[i][1];
            int newRow = rowNum - rowPrefix;
            int newCol = colNum - colPrefix;
            //maximum of 8 possible movements in one direction
            for(int j=0; j<board.length; j++) {
                if(newRow < 0 || newRow >= 8 || newCol < 0 || newCol >= 8) {
                    break;
                }
                if(board[newRow][newCol] == null) {
                    validPos.add(Conversion.convertIndexToPos(newRow, newCol));
                }
                else if(board[newRow][newCol] != null && board[newRow][newCol].getColor() != color) {
                    validPos.add(Conversion.convertIndexToPos(newRow, newCol));
                    break;
                }
                else {
                    break;
                }
                newRow = newRow - movePrefixes[i][0];
                newCol = newCol - movePrefixes[i][1];
            }
        }

        return validPos;
    }

    private static ArrayList<String> checkRookMoves(int color, int rowNum, int colNum, Figure[][] board) {
        
        ArrayList<String> validPos = new ArrayList<>();

        int[][] movePrefixes = {
            {1, 0}, {0, -1}, {-1, 0}, {0, 1}
        };

        //four directions of movement
        for(int i=0; i<4; i++) {
            int rowPrefix = movePrefixes[i][0];
            int colPrefix = movePrefixes[i][1];
            int newRow = rowNum - rowPrefix;
            int newCol = colNum - colPrefix;
            //maximum of 8 possible movements in one direction
            for(int j=0; j<board.length; j++) {
                if(newRow < 0 || newRow >= 8 || newCol < 0 || newCol >= 8) {
                    break;
                }
                if(board[newRow][newCol] == null) {
                    validPos.add(Conversion.convertIndexToPos(newRow, newCol));
                }
                else if(board[newRow][newCol] != null && board[newRow][newCol].getColor() != color) {
                    validPos.add(Conversion.convertIndexToPos(newRow, newCol));
                    break;
                }
                else {
                    break;
                }
                newRow = newRow - movePrefixes[i][0];
                newCol = newCol - movePrefixes[i][1];
            }
        }

        return validPos;
    }

    private static ArrayList<String> checkQueenMoves(int color, int rowNum, int colNum, Figure[][] board) {

        ArrayList<String> validPos = new ArrayList<>();

        int[][] movePrefixes = {
            {1, 0}, {0, -1}, {-1, 0}, {0, 1}, {1, 1}, {-1, 1}, {-1, -1}, {1, -1}
        };

        //eight directions of movement
        for(int i=0; i<8; i++) {
            int rowPrefix = movePrefixes[i][0];
            int colPrefix = movePrefixes[i][1];
            int newRow = rowNum - rowPrefix;
            int newCol = colNum - colPrefix;
            //maximum of 8 possible movements in one direction
            for(int j=0; j<board.length; j++) {
                if(newRow < 0 || newRow >= 8 || newCol < 0 || newCol >= 8) {
                    break;
                }
                if(board[newRow][newCol] == null) {
                    validPos.add(Conversion.convertIndexToPos(newRow, newCol));
                }
                else if(board[newRow][newCol] != null && board[newRow][newCol].getColor() != color) {
                    validPos.add(Conversion.convertIndexToPos(newRow, newCol));
                    break;
                }
                else {
                    break;
                }
                newRow = newRow - movePrefixes[i][0];
                newCol = newCol - movePrefixes[i][1];
            }
        }
        return validPos;
    }

    public static ArrayList<String> checkKingMoves(King king, int color, int rowNum, int colNum, Figure[][] board) {

        ArrayList<String> validPos = new ArrayList<>();
        ArrayList<String> noNearbyKing = new ArrayList<>();

        int[][] moves = {
            {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1}
        };

        int totalNum = 0;

        //checks each square around the king if it's eligible
        for (int[] move : moves) {
            noNearbyKing.clear();
            totalNum = 0;
            int newRow = rowNum - move[0];
            int newCol = colNum - move[1];
            //counts checked indexes that fall outside of the chessboard
            if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                //takes a selected square and checks it's adjacent squares if they contain an enemy king square
                for(int i=0; i<moves.length; i++) {
                    int newRow1 = newRow - moves[i][0];
                    int newCol1 = newCol - moves[i][1];
                    if (newRow1 >= 0 && newRow1 < 8 && newCol1 >= 0 && newCol1 < 8) {
                        if(board[newRow1][newCol1] == null) {
                            noNearbyKing.add(Conversion.convertIndexToPos(newRow1, newCol1));
                        }
                        else if(board[newRow1][newCol1].getSymbol() != "♔" && board[newRow1][newCol1].getSymbol() != "♚" || board[newRow1][newCol1].getColor() == color) {
                            noNearbyKing.add(Conversion.convertIndexToPos(newRow1, newCol1));
                        }
                    }
                    else {
                        totalNum++;
                    }
                }
                //if the size of the surrounding squares doesn't match the typical amount of king's moves adjusted for outside indexes
                //then there exists an enemy king adjacent to that square
                //therefore continue the loop and not add it among the possible moves
                if(noNearbyKing.size() != 8 - totalNum) {
                    continue;
                }
                if (board[newRow][newCol] == null) {
                    validPos.add(Conversion.convertIndexToPos(newRow, newCol));
                }
                else if (board[newRow][newCol].getColor() != color) {
                    validPos.add(Conversion.convertIndexToPos(newRow, newCol));
                }
            }
        }

        return validPos;
    }

    //polymorphic functions for determining disallowed king moves when king under check
    public static ArrayList<String> checkBishopMoves(int color, Figure attackFigure, Figure[][] board) {

        ArrayList<String> validPos = new ArrayList<>();

        int rowAndCol[] = Conversion.convertPosToIndex(attackFigure.getPos());
        int rowNum = rowAndCol[0];
        int colNum = rowAndCol[1];

        int[][] movePrefixes = {
            {1, 1}, {-1, 1}, {-1, -1}, {1, -1}
        };

        //four directions of movement
        for(int i=0; i<4; i++) {
            int rowPrefix = movePrefixes[i][0];
            int colPrefix = movePrefixes[i][1];
            int newRow = rowNum - rowPrefix;
            int newCol = colNum - colPrefix;
            //maximum of 8 possible movements in one direction
            for(int j=0; j<board.length; j++) {
                if(newRow < 0 || newRow >= 8 || newCol < 0 || newCol >= 8) {
                    break;
                }
                if(board[newRow][newCol] == null) {
                    validPos.add(Conversion.convertIndexToPos(newRow, newCol));
                }
                else if(board[newRow][newCol] != null && board[newRow][newCol].getColor() != color && board[newRow][newCol].getType()=="King") {
                    validPos.add(Conversion.convertIndexToPos(newRow, newCol));
                }
                else if(board[newRow][newCol] != null && board[newRow][newCol].getColor() != color && board[newRow][newCol].getType()!="King") {
                    validPos.add(Conversion.convertIndexToPos(newRow, newCol));
                    break;
                }
                else {
                    break;
                }
                newRow = newRow - movePrefixes[i][0];
                newCol = newCol - movePrefixes[i][1];
            }
        }

        return validPos;
    }

    public static ArrayList<String> checkRookMoves(int color, Figure attackFigure, Figure[][] board) {
        
        ArrayList<String> validPos = new ArrayList<>();

        int rowAndCol[] = Conversion.convertPosToIndex(attackFigure.getPos());
        int rowNum = rowAndCol[0];
        int colNum = rowAndCol[1];

        int[][] movePrefixes = {
            {1, 0}, {0, -1}, {-1, 0}, {0, 1}
        };

        //four directions of movement
        for(int i=0; i<4; i++) {
            int rowPrefix = movePrefixes[i][0];
            int colPrefix = movePrefixes[i][1];
            int newRow = rowNum - rowPrefix;
            int newCol = colNum - colPrefix;
            //maximum of 8 possible movements in one direction
            for(int j=0; j<board.length; j++) {
                if(newRow < 0 || newRow >= 8 || newCol < 0 || newCol >= 8) {
                    break;
                }
                if(board[newRow][newCol] == null) {
                    validPos.add(Conversion.convertIndexToPos(newRow, newCol));
                }
                else if(board[newRow][newCol] != null && board[newRow][newCol].getColor() != color && board[newRow][newCol].getType()=="King") {
                    validPos.add(Conversion.convertIndexToPos(newRow, newCol));
                }
                else if(board[newRow][newCol] != null && board[newRow][newCol].getColor() != color && board[newRow][newCol].getType()!="King") {
                    validPos.add(Conversion.convertIndexToPos(newRow, newCol));
                    break;
                }
                else {
                    break;
                }
                newRow = newRow - movePrefixes[i][0];
                newCol = newCol - movePrefixes[i][1];
            }
        }

        return validPos;
    }

    public static ArrayList<String> checkQueenMoves(int color, Figure attackFigure, Figure[][] board) {

        ArrayList<String> validPos = new ArrayList<>();

        int rowAndCol[] = Conversion.convertPosToIndex(attackFigure.getPos());
        int rowNum = rowAndCol[0];
        int colNum = rowAndCol[1];

        int[][] movePrefixes = {
            {1, 0}, {0, -1}, {-1, 0}, {0, 1}, {1, 1}, {-1, 1}, {-1, -1}, {1, -1}
        };

        //eight directions of movement
        for(int i=0; i<8; i++) {
            int rowPrefix = movePrefixes[i][0];
            int colPrefix = movePrefixes[i][1];
            int newRow = rowNum - rowPrefix;
            int newCol = colNum - colPrefix;
            //maximum of 8 possible movements in one direction
            for(int j=0; j<board.length; j++) {
                if(newRow < 0 || newRow >= 8 || newCol < 0 || newCol >= 8) {
                    break;
                }
                if(board[newRow][newCol] == null) {
                    validPos.add(Conversion.convertIndexToPos(newRow, newCol));
                }
                else if(board[newRow][newCol] != null && board[newRow][newCol].getColor() != color && board[newRow][newCol].getType()=="King") {
                    validPos.add(Conversion.convertIndexToPos(newRow, newCol));
                }
                else if(board[newRow][newCol] != null && board[newRow][newCol].getColor() != color && board[newRow][newCol].getType()!="King") {
                    validPos.add(Conversion.convertIndexToPos(newRow, newCol));
                    break;
                }
                else {
                    break;
                }
                newRow = newRow - movePrefixes[i][0];
                newCol = newCol - movePrefixes[i][1];
            }
        }
        return validPos;
    }
}

//java.lang.ClassCastException: class java.lang.String cannot be cast to class java.util.ArrayList (java.lang.String and java.util.ArrayList are in module java.base of loader 'bootstrap')