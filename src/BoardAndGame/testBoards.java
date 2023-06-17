package BoardAndGame;

import BoardAndGame.CheckerPiece;
import BoardAndGame.CheckersSquare;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class testBoards {
    void addnewPiece(Set<CheckersSquare> redPieces, Set<CheckersSquare> blackPieces, CheckersSquare[][] squares,
                     int id, int x, int y, boolean isPlayerRed) throws IOException {
        if(isPlayerRed){
            if(id==2 || id==4) {
                CheckerPiece insertBlack = new CheckerPiece(id);
                insertBlack.canPlayerMoveThis = false;
                squares[x][y].add(insertBlack.picLabel);
                squares[x][y].piece = insertBlack;
                blackPieces.add(squares[x][y]);
            }else{
                CheckerPiece insertRed = new CheckerPiece(id);
                insertRed.canPlayerMoveThis = true;
                squares[x][y].add(insertRed.picLabel);
                squares[x][y].piece=insertRed;
                redPieces.add(squares[x][y]);
            }
        }else {
            if(id==1 || id==3) {
                CheckerPiece insertRed = new CheckerPiece(id);
                insertRed.canPlayerMoveThis = false;
                squares[x][y].add(insertRed.picLabel);
                squares[x][y].piece = insertRed;
                redPieces.add(squares[x][y]);
            }else{
                CheckerPiece insertRed = new CheckerPiece(id);
                insertRed.canPlayerMoveThis = true;
                squares[x][y].add(insertRed.picLabel);
                squares[x][y].piece = insertRed;
                blackPieces.add(squares[x][y]);
            }
        }

    }
    void initBoard(Set<CheckersSquare> redPieces, Set<CheckersSquare> blackPieces, CheckersSquare[][] squares) throws IOException {
        CheckerPiece insertBlack = new CheckerPiece(2);
        insertBlack.canPlayerMoveThis = false;
        squares[2][1].add(insertBlack.picLabel);
        squares[2][1].piece=insertBlack;
        blackPieces.add(squares[2][1]);

        CheckerPiece insertBlack1 = new CheckerPiece(1);
        insertBlack1.canPlayerMoveThis = true;
        squares[3][2].add(insertBlack1.picLabel);
        squares[3][2].piece=insertBlack1;
        redPieces.add(squares[3][2]);

        CheckerPiece insertBlack2 = new CheckerPiece(1);
        insertBlack2.canPlayerMoveThis = true;
        squares[5][6].add(insertBlack2.picLabel);
        squares[5][6].piece=insertBlack2;
        redPieces.add(squares[5][6]);

        CheckerPiece insertBlack3 = new CheckerPiece(1);
        insertBlack3.canPlayerMoveThis = true;
        squares[2][7].add(insertBlack3.picLabel);
        squares[2][7].piece=insertBlack3;
        redPieces.add(squares[2][7]);

        CheckerPiece insertBlack4 = new CheckerPiece(2);
        insertBlack4.canPlayerMoveThis = false;
        squares[1][6].add(insertBlack4.picLabel);
        squares[1][6].piece=insertBlack4;
        blackPieces.add(squares[1][6]);

        CheckerPiece insertBlack5 = new CheckerPiece(2);
        insertBlack5.canPlayerMoveThis = false;
        squares[1][2].add(insertBlack5.picLabel);
        squares[1][2].piece=insertBlack5;
        blackPieces.add(squares[1][2]);

        CheckerPiece insertBlack6 = new CheckerPiece(2);
        insertBlack5.canPlayerMoveThis = false;
        squares[1][4].add(insertBlack6.picLabel);
        squares[1][4].piece=insertBlack6;
        blackPieces.add(squares[1][4]);
    }

    void initboard2(Set<CheckersSquare> redPieces, Set<CheckersSquare> blackPieces, CheckersSquare[][] squares,
                    board board) throws IOException {
        addnewPiece(redPieces,blackPieces,squares,2,0,3,false);
        addnewPiece(redPieces,blackPieces,squares,2,0,7,false);
        addnewPiece(redPieces,blackPieces,squares,2,1,4,false);
        addnewPiece(redPieces,blackPieces,squares,2,1,6,false);
        addnewPiece(redPieces,blackPieces,squares,2,2,3,false);
        addnewPiece(redPieces,blackPieces,squares,2,3,0,false);
        addnewPiece(redPieces,blackPieces,squares,2,4,5,false);
        addnewPiece(redPieces,blackPieces,squares,1,7,0,true);
        addnewPiece(redPieces,blackPieces,squares,1,7,2,true);
        addnewPiece(redPieces,blackPieces,squares,1,7,6,true);
        addnewPiece(redPieces,blackPieces,squares,1,6,7,true);
        addnewPiece(redPieces,blackPieces,squares,1,5,0,true);
        addnewPiece(redPieces,blackPieces,squares,1,5,2,true);
        addnewPiece(redPieces,blackPieces,squares,1,4,7,true);
    }

    void initboard3(Set<CheckersSquare> redPieces, Set<CheckersSquare> blackPieces, CheckersSquare[][] squares,
                    board board) throws IOException {
        addnewPiece(redPieces,blackPieces,squares,3,0,3,true);
        addnewPiece(redPieces,blackPieces,squares,1,1,4,true);
        addnewPiece(redPieces,blackPieces,squares,1,5,2,true);
        addnewPiece(redPieces,blackPieces,squares,1,4,7,true);
        addnewPiece(redPieces,blackPieces,squares,2,3,4,false);
        addnewPiece(redPieces,blackPieces,squares,2,5,4,false);
        addnewPiece(redPieces,blackPieces,squares,4,3,0,false);
        board.redKings=1;
        board.blackKings=1;
    }

    void initboard4(Set<CheckersSquare> redPieces, Set<CheckersSquare> blackPieces, CheckersSquare[][] squares,
                    board board) throws IOException {
        addnewPiece(redPieces,blackPieces,squares,1,7,0,true);
        addnewPiece(redPieces,blackPieces,squares,1,7,2,true);
        addnewPiece(redPieces,blackPieces,squares,1,7,4,true);
        addnewPiece(redPieces,blackPieces,squares,1,7,6,true);
        addnewPiece(redPieces,blackPieces,squares,1,6,7,true);
        addnewPiece(redPieces,blackPieces,squares,1,5,0,true);
        addnewPiece(redPieces,blackPieces,squares,1,5,2,true);
        addnewPiece(redPieces,blackPieces,squares,1,5,4,true);
        addnewPiece(redPieces,blackPieces,squares,1,4,3,true);
        addnewPiece(redPieces,blackPieces,squares,1,5,6,true);
        addnewPiece(redPieces,blackPieces,squares,1,4,7,true);
        addnewPiece(redPieces,blackPieces,squares,2,0,1,false);
        addnewPiece(redPieces,blackPieces,squares,2,0,5,false);
        addnewPiece(redPieces,blackPieces,squares,2,0,7,false);
        addnewPiece(redPieces,blackPieces,squares,2,1,0,false);
        addnewPiece(redPieces,blackPieces,squares,2,1,2,false);
        addnewPiece(redPieces,blackPieces,squares,2,1,4,false);
        addnewPiece(redPieces,blackPieces,squares,2,1,6,false);
        addnewPiece(redPieces,blackPieces,squares,2,2,1,false);
        addnewPiece(redPieces,blackPieces,squares,2,2,3,false);
        addnewPiece(redPieces,blackPieces,squares,2,2,5,false);
        addnewPiece(redPieces,blackPieces,squares,2,3,6,false);

    }

    void initboardcantmove(Set<CheckersSquare> redPieces, Set<CheckersSquare> blackPieces, CheckersSquare[][] squares,
                           board board) throws IOException {
        addnewPiece(redPieces,blackPieces,squares,2,6,7,true);
        addnewPiece(redPieces,blackPieces,squares,1,7,6,true);
        addnewPiece(redPieces,blackPieces,squares,1,5,6,true);
        addnewPiece(redPieces,blackPieces,squares,1,4,5,true);
        addnewPiece(redPieces,blackPieces,squares,1,3,2,true);

    }
}
