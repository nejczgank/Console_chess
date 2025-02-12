public class Conversion {

    public static int[] convertPosToIndex(Figure selectedFigure) {

        String currentPos = selectedFigure.getPos();
        char letter = currentPos.charAt(0);
        char number = currentPos.charAt(1);

        int rowNum = 0;
        int colNum = 0;

        rowNum = 8-Character.getNumericValue(number);

        String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h"};
        for(int i=0; i<letters.length; i++) {
            if(letters[i].equals(String.valueOf(letter))) {
                colNum = i;
            }
        }
        return new int[]{rowNum, colNum};
    }

    public static int[] convertPosToIndex(String selectedPos) {

        char letter = selectedPos.charAt(0);
        char number = selectedPos.charAt(1);

        int rowNum = 0;
        int colNum = 0;

        rowNum = 8-Character.getNumericValue(number);

        String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h"};
        for(int i=0; i<letters.length; i++) {
            if(letters[i].equals(String.valueOf(letter))) {
                colNum = i;
            }
        }
        return new int[]{rowNum, colNum};
    }

    public static String convertIndexToPos(int rowNum, int colNum) {

        String pos = "";
        String letter = "";
        String number = "";

        number = String.valueOf(8-rowNum); //index to value correction

        String[] possibleLetters = {"a", "b", "c", "d", "e", "f", "g", "h"};
    
        for(int i=0; i<possibleLetters.length; i++) {
            if(i == colNum) {
                letter = possibleLetters[i];
                break;
            }
        }

        pos = letter + number;
        return pos;
    }
}
