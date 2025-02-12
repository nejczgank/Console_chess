//abstract because the object class won't be instantiated
//it serves as a default class where new attributes can be added immediately and apply to all derived object classess

//figure refers to chess piece
public abstract class Figure {

    private String position;
    private String type;
    private Integer color;
    private Integer figureValue;
    protected String symbol;

    public Figure(String position, String type, Integer figureValue, Integer color) {
        this.position = position;
        this.type = type;
        this.figureValue = figureValue;
        this.color = color;
    }

    public String getPos() {
        return this.position;
    }

    public void setPos(String position) {
        this.position = position;
    }

    public String getType() {
        return this.type;
    }

    public int getFigureValue() {
        return this.figureValue;
    }

    public int getColor() {
        return this.color;
    }

    public String getSymbol() {
        return this.symbol;
    }
}

class Pawn extends Figure {
    
    private boolean initPos;
    private boolean movedByTwo;
    private boolean enPassantLeft;
    private boolean enPassantRight;

    public Pawn(String position, Integer figureValue, Integer color) {
        super(position, "Pawn", figureValue, color);
        this.symbol = (color == 0) ? "♙" : "♟";
        this.initPos = true;
    }

    public String getPosPawn() {
        return getPos();
    }

    public String getSymbol() {
        return this.symbol;
    }

    public Boolean getInitPos() {
        return this.initPos;
    }

    public void setInitPosFalse() {
        this.initPos = false;
    }

    public Boolean getMovedByTwo() {
        return this.movedByTwo;
    }

    public void setMovedByTwo(Boolean newMovedByTwo) {
        this.movedByTwo = newMovedByTwo;
    }

    public Boolean getEnPassantLeft() {
        return this.enPassantLeft;
    }

    public void setEnPassantLeft(Boolean newEnPassant) {
        this.enPassantLeft = newEnPassant;
    }

    public Boolean getEnPassantRight() {
        return this.enPassantRight;
    }

    public void setEnPassantRight(Boolean newEnPassant) {
        this.enPassantRight = newEnPassant;
    }
}

class Knight extends Figure {

    public Knight(String position, Integer figureValue, Integer color) {
        super(position, "Knight", figureValue, color);
        this.symbol = (color == 0) ? "♘" : "♞";
    }

    public String getPosKnight() {
        return getPos();
    }

    public String getSymbol() {
        return this.symbol;
    }
}

class Bishop extends Figure {

    public Bishop(String position, Integer figureValue, Integer color) {
        super(position, "Bishop", figureValue, color);
        this.symbol = (color == 0) ? "♗"  : "♝";
    }

    public String getPosBishop() {
        return getPos();
    }

    public String getSymbol() {
        return this.symbol;
    }

}

class Rook extends Figure {

    public Rook(String position, Integer figureValue, Integer color) {
        super(position, "Rook", figureValue, color);
        this.symbol = (color == 0) ? "♖" : "♜";
    }

    public String getPosRook() {
        return getPos();
    }

    public String getSymbol() {
        return this.symbol;
    }
}

class Queen extends Figure {

    public Queen(String position, Integer figureValue, Integer color) {
        super(position, "Queen", figureValue, color);
        this.symbol = (color == 0) ? "♕" : "♛";
    }

    public String getPosQueen() {
        return getPos();
    }

    public String getSymbol() {
        return this.symbol;
    }
}

class King extends Figure {

    public King(String position, Integer figureValue, Integer color) {
        super(position, "King", figureValue, color);
        this.symbol = (color == 0) ? "♔" : "♚";
    }

    public String getPosKing() {
        return getPos();
    }

    public String getSymbol() {
        return this.symbol;
    }
}