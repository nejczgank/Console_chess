import java.util.ArrayList;

class InputOutput {

    public static void displayBoard(Figure[][] board, int whiteFigureCount, int blackFigureCount, ArrayList<String> whitesAquiredPieces, ArrayList<String> blacksAquiredPieces) {
        int count = 8;
        for(int i=0; i<board.length; i++) {
            for(int j=0; j<board[i].length; j++) {
                try {
                    if(j==0) {
                        System.out.print(count + "   ");
                        System.out.print(board[i][j].getSymbol() + " ");
                    }
                    else if(j>0 && j<board[i].length) {
                        System.out.print(board[i][j].getSymbol() + " ");
                    } 
                } 
                catch (Exception e) {
                    if((j+i)%2==0) {
                        System.out.print("□ ");
                    }
                    else if((j+i)%2==1) {
                        System.out.print("■ ");
                    }
                    
                }
            }
            if(i==0) {
                System.out.print("  W : " + whiteFigureCount + " : ");
                for(String piece : whitesAquiredPieces) {
                    System.out.print(piece + ",");
                }
            }
            else if(i==1) {
                System.out.print("  B : " + blackFigureCount  + " : ");
                for(String piece : blacksAquiredPieces) {
                        System.out.print(piece + ",");
                }
            }
            count--;
            System.out.println();
        }
        letterRow();
    }

    public static void letterRow() {
        System.out.println();
        System.out.print("    ");
        String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h"};
        for(int i=0; i<letters.length; i++) {
            System.out.print(letters[i] + " ");
        }
    }

    public static void clearDisplay() { 
        try {
            //for windows
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
            //for unix and unix derived systems
            else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            System.out.println("Error clearing the console: " + e.getMessage());
        }
    }
}
