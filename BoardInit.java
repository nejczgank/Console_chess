class BoardInit {
    
    public static Figure[][] boardInit() {
        Figure[][] board = new Figure[8][8];

        //init pawns
        Pawn[] pawns = new Pawn[8];
        //white pawns
        for(int i=0; i<8; i++) {
            String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h"};
            pawns[i] = new Pawn(letters[i]+"2", 1, 0);
            board[6][i] = pawns[i];
        }
        //black pawns
        for (int i=0; i<8; i++) {
            String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h"};
            pawns[i] = new Pawn(letters[i]+"7", 1, 1);
            board[1][i] = pawns[i];
        }

        Knight[] knights = new Knight[2];
        //white knights
        knights[0] = new Knight("b1", 3, 0);
        knights[1] = new Knight("g1", 3, 0);
        board[7][1] = knights[0];
        board[7][6] = knights[1];
        //black knights
        knights[0] = new Knight("b8", 3, 1);
        knights[1] = new Knight("g8", 3, 1);
        board[0][1] = knights[0];
        board[0][6] = knights[1];

        Bishop[] bishops = new Bishop[2];
        //white bishops
        bishops[0] = new Bishop("c1", 3, 0);
        bishops[1] = new Bishop("f1", 3, 0);
        board[7][2] = bishops[0];
        board[7][5] = bishops[1];
        //black bishops
        bishops[0] = new Bishop("c8", 3, 1);
        bishops[1] = new Bishop("f8", 3, 1);
        board[0][2] = bishops[0];
        board[0][5] = bishops[1];

        //init rooks
        Rook[] rooks = new Rook[2];
        //white rooks
        rooks[0] = new Rook("a1", 5, 0);
        rooks[1] = new Rook("h1", 5, 0);
        board[7][0] = rooks[0];
        board[7][7] = rooks[1];
        //black rooks
        rooks[0] = new Rook("a8", 5, 1);
        rooks[1] = new Rook("h8", 5, 1);
        board[0][0] = rooks[0];
        board[0][7] = rooks[1];

        //init queens
        Queen[] queens = new Queen[1];
        //white queen
        queens[0] = new Queen("d1", 9, 0);
        board[7][3] = queens[0];
        //black queen
        queens[0] = new Queen("d8", 9, 1);
        board[0][3] = queens[0];

        //init kings
        King[] kings = new King[1];
        //white king
        kings[0] = new King("e1", 0, 0);
        board[7][4] = kings[0];
        //black king
        kings[0] = new King("e8", 0, 1);
        board[0][4] = kings[0];

        return board;
    }
}