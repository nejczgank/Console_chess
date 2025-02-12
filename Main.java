import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        //scanner
        Scanner sc = new Scanner(System.in);

        //init board
        Figure[][] board = BoardInit.boardInit();

        //make turn
        int color = 0;

        Boolean wincondition = false;

        //white's figure counter
        int whiteFigureCount = 0;
        ArrayList<String> whitesAquiredPieces = new ArrayList<>();

        //black's figure counter
        int blackFigureCount = 0;
        ArrayList<String> blacksAquiredPieces = new ArrayList<>();

        while (wincondition == false) {
            //clear previous display
            //InputOutput.clearDisplay();           //TOLE POL OMOGOČI!!!!
            //board display
            InputOutput.displayBoard(board, whiteFigureCount, blackFigureCount, whitesAquiredPieces, blacksAquiredPieces);

            String playerName = "";

            if(color%2==0) {
                System.out.println("\n\nWhite's turn");
                playerName = "White";
            }
            else if(color%2==1) {
                System.out.println("\n\nBlack's turn");
                playerName = "Black";
            }

            color = color%2;

            try {
                Boolean retry = true;
                while(retry == true) {

                    retry = false;
          
                    String selectPos = "";
                    String placePos = "";
                    Figure selectedFigure = null;

                    //ensures input validation
                    selectPos = inputValidation(selectPos, sc);
         
                    //ensures proper figure selection
                    Object[] selectedFigureAndSelectPos = figureSelection(selectPos, selectedFigure, color, board, sc);
                    selectedFigure = (Figure) selectedFigureAndSelectPos[0];
                    selectPos = (String) selectedFigureAndSelectPos[1];

                    int oppositeColor = (color == 0) ? 1 : 0;

                    //validates placement of the figure
                    Object[] retryAndPlacePos = validateFigurePlacement(selectPos, selectedFigure, placePos, oppositeColor, retry, board, sc);
                    placePos = (String) retryAndPlacePos[0];
                    retry = (Boolean) retryAndPlacePos[1];
                    if(placePos.equals(selectPos)) {
                        continue;
                    }
                    
                    //places the figure
                    Figure replacedFigure = MovesHandling.selectFigure(placePos, oppositeColor, board);
                    
                    //places the figure on the selected position and removes it from the previous position
                    Object[] figureCounts = placeFigure(replacedFigure, selectedFigure, placePos, color, oppositeColor, whiteFigureCount, whitesAquiredPieces, blackFigureCount, blacksAquiredPieces, board);
                    whiteFigureCount = (int)figureCounts[0];
                    blackFigureCount = (int)figureCounts[1];
                    board = (Figure[][])figureCounts[2];

                    //self-check validation
                    retry = selfCheckValidation(board, color, oppositeColor, retry);
                    
                    //reverses the previous board placement
                    if(retry==true) {
                        int[] placePosIndex = Conversion.convertPosToIndex(placePos);
                        int[] selectPosIndex = Conversion.convertPosToIndex(selectPos);
                        board[selectPosIndex[0]][selectPosIndex[1]] = board[placePosIndex[0]][placePosIndex[1]];
                        board[selectPosIndex[0]][selectPosIndex[1]].setPos(selectPos);
                        board[placePosIndex[0]][placePosIndex[1]] = null;
                        continue;
                    }

                    //checks for check
                    if(CheckAndMate.findAtkPiece(board, color, oppositeColor) != null) {
                        //checks for checkmate
                        if(CheckAndMate.combineMovesHelper(board, oppositeColor, color).isEmpty()) {
                            System.out.println(playerName + " is the winner!\n");
                            InputOutput.displayBoard(board, whiteFigureCount, blackFigureCount, whitesAquiredPieces, blacksAquiredPieces);
                            
                            wincondition = true;
                            break;
                        }
                    }
                color++;
            }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        sc.close();
    }

    private static String inputValidation(String selectPos, Scanner sc) {
        //ensures input validation
        System.out.print("\nEnter a figure position (a1-h8): ");
        selectPos = sc.nextLine();

        while(!selectPos.matches("[a-h][1-8]")) {
            System.out.println("Invalid position! Try again.");
            System.out.print("Enter a figure position (a1-h8): ");
            selectPos = sc.nextLine();
        }

        return selectPos;
    }

    private static Object[] figureSelection(String selectPos, Figure selectedFigure, int color, Figure[][] board, Scanner sc) {

        while(selectedFigure == null) {
            selectedFigure = MovesHandling.selectFigure(selectPos, color, board);
            if(selectedFigure == null) {
                System.out.println("Choose a valid figure!");
                System.out.print("Enter a figure position (a1-h8): ");
                selectPos = sc.nextLine();
            }
        }
        Object[] obj = {selectedFigure, selectPos};
        return obj;
    }

    private static Object[] validateFigurePlacement(String selectPos, Figure selectedFigure, String placePos, int oppositeColor, Boolean retry, Figure[][] board, Scanner sc) {

        List<String> moves = MovesHandling.findMoves(selectedFigure, board);
        while(!moves.contains(placePos)) { //tlele gre iz nekega razloga dvakrat v findMoves
            //retries figure selection and placement
            if(placePos.equals(selectPos)) {
                retry = true;
                selectedFigure = null;
                break;
            }
            System.out.print("\nEnter figure's placement: ");
            placePos = sc.nextLine();
        }
        Object[] obj = {placePos, retry};
        return obj;
    }

    private static Object[] placeFigure(Figure replacedFigure, Figure selectedFigure, String placePos, int color, int oppositeColor, int whiteFigureCount, ArrayList<String> whitesAquiredPieces, int blackFigureCount, ArrayList<String> blacksAquiredPieces, Figure[][] board) {
        //take figure
        int[] replacedFigurePos = new int[2];
        if(replacedFigure != null) {
            //replace figure
            replacedFigurePos = Conversion.convertPosToIndex(replacedFigure);
            board[replacedFigurePos[0]][replacedFigurePos[1]] = selectedFigure;
            if(color%2==0) {
                whiteFigureCount += replacedFigure.getFigureValue();
                whitesAquiredPieces.add(replacedFigure.getSymbol());
            }
            else if(color%2==1) {
                blackFigureCount += replacedFigure.getFigureValue();
                blacksAquiredPieces.add(replacedFigure.getSymbol());
            }
        }
        else {
            //replace empty square
            replacedFigurePos = Conversion.convertPosToIndex(placePos);
            board[replacedFigurePos[0]][replacedFigurePos[1]] = selectedFigure;     
        }
        //delete previous
        int[] selectedFigurePos = Conversion.convertPosToIndex(selectedFigure);
        board[selectedFigurePos[0]][selectedFigurePos[1]] = null;
        
        //if a pawn was selected and placed then ensure it's position is no longer set as initial
        if (selectedFigure instanceof Pawn && ((Pawn)selectedFigure).getInitPos()!=false) {
            ((Pawn) selectedFigure).setInitPosFalse();
            if(Integer.valueOf(selectedFigure.getPos().charAt(1))+1<Integer.valueOf(placePos.charAt(1))) {
                ((Pawn)selectedFigure).setMovedByTwo(true);
            }
            else if(Integer.valueOf(selectedFigure.getPos().charAt(1))-1>Integer.valueOf(placePos.charAt(1))) {
                ((Pawn)selectedFigure).setMovedByTwo(true);
            }
        }

        if (selectedFigure instanceof Pawn && //selected figure is pawn
        ((Pawn)selectedFigure).getInitPos()==false &&  //it has left its initial pos
        (Integer.valueOf(selectedFigure.getPos().charAt(1))+1==Integer.valueOf(placePos.charAt(1)) || //it has moved exactly one square up OR
        Integer.valueOf(selectedFigure.getPos().charAt(1))-1==Integer.valueOf(placePos.charAt(1))) && //it has moved exactly one square down
        selectedFigure.getPos().charAt(0) == placePos.charAt(0)) {
            if(board[replacedFigurePos[0]][replacedFigurePos[1]-1] instanceof Pawn && board[replacedFigurePos[0]][replacedFigurePos[1]-1].getColor()==oppositeColor && ((Pawn)board[replacedFigurePos[0]][replacedFigurePos[1]-1]).getMovedByTwo() == true) { //an enemy pawn on the left has been moved by two
                ((Pawn)board[replacedFigurePos[0]][replacedFigurePos[1]-1]).setEnPassantRight(true);
            }
            if(board[replacedFigurePos[0]][replacedFigurePos[1]+1] instanceof Pawn && board[replacedFigurePos[0]][replacedFigurePos[1]+1].getColor()==oppositeColor && ((Pawn)board[replacedFigurePos[0]][replacedFigurePos[1]+1]).getMovedByTwo() == true) { //an enemy pawn on the right has been moved by two
                ((Pawn)board[replacedFigurePos[0]][replacedFigurePos[1]+1]).setEnPassantLeft(true);
            }
        }

        //delete overtaken figure
        /*
        if(selectedFigure instanceof Pawn && replacedFigure == null && selectedFigure.getPos().charAt(0) != placePos.charAt(0)) {
            if(selectedFigure.getPos().charAt(1)) {

            }
        } */


        //update position
        selectedFigure.setPos(placePos);
        
        //checks if a pawn can be converted to a queen
        int row = Conversion.convertPosToIndex(placePos)[0];
        int col = Conversion.convertPosToIndex(placePos)[1];
        board = SpecialMoves.figureResurrection(board, row, col, color);

        Object[] figureCounts = {whiteFigureCount, blackFigureCount, board};
        return figureCounts;
    }

    private static Boolean selfCheckValidation(Figure[][] board, int color, int oppositeColor, Boolean retry) {
        Object[] result = CheckAndMate.findAtkPiece(board, oppositeColor, color);
        if(result!=null) {
            System.out.println("Illegal move. Your king is in check!");
            retry = true;
        }
        return retry;
    }
}

//implement pawn conversions at opposide end of the chess board (DONE)
//implement en-passant
//implement castling
//do research on draws and implement the easier ones
//enable board switching as an option at the start*
//enable figure's symbol choice at the start (letters or chess symbols)

/*en passant
 * 1. remove the overtaken pawn from the board and apply it to the takenFiguresCounter
 * 2. move the whole en passant check into specialMoves from Main
 */

/*KNOWN BUGS
 * java.lang.ClassCastException: class java.lang.String cannot be cast to class java.util.ArrayList (java.lang.String and java.util.ArrayList are in module java.base of loader 'bootstrap') OCCURS AT TIMES WHEN PROVIDING A CHECK
 * java.lang.ArrayIndexOutOfBoundsException: Index -1 out of bounds for length 8 HAPPENS WITH PAWNS FOR SOME REASON
 */