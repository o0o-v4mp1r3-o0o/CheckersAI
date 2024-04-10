package AI;

import java.util.HashMap;
import java.util.Random;

public class tt {
    long board[][][] = new long[2][2][64];
    long playerturn[] = new long[2];
    long drawmoves[] = new long[50];
    long justcaptured[] = new long[2];
    long captureAvailable[] = new long[2];
    long overridden[] = new long[1];
    HashMap<Long,ttstorage> ss = new HashMap<>(400000);

    tt(){
        zobristFillArrays();
    }


    void zobristFillArrays(){
        for(int player = 0; player < 2; player++){
            for(int uniquePieces = 0; uniquePieces < 2; uniquePieces++){
                for(int square = 1; square < 64; square++){
                    board[player][uniquePieces][square] = new Random().nextLong();
                }
            }
        }
        for(int i = 0; i < 2; i++){
            playerturn[i] = new Random().nextLong();
        }
        for(int i = 0; i < 50; i++){
            drawmoves[i] = new Random().nextLong();
        }
        for(int i = 0; i < 2; i++){
            justcaptured[i] = new Random().nextLong();
        }
        for(int i = 0; i < 2; i++){
            captureAvailable[i] = new Random().nextLong();
        }
        for(int i = 0; i < 1; i++){
            overridden[i] = new Random().nextLong();
        }
    }

    long initHash(Node node, int depthindex){
        long returnKey = 0;

        for(int i = 0; i < 8; i++){
            int c;
            if(i==0){
                c = 1;
            }else if(i % 2 == 0){
                c = 1;
            }else{
                c=0;
            }
            for(; c < 8; c+=2){
                if(node.board[i][c]==0){
                    continue;
                }
                else if(node.board[i][c]==1){
                    returnKey ^= board[0][0][(i*8)+c];
                }
                else if(node.board[i][c]==2){
                    returnKey ^= board[1][0][(i*8)+c];
                }
                else if(node.board[i][c]==3){
                    returnKey ^= board[0][1][(i*8)+c];
                }
                else if(node.board[i][c]==4){
                    returnKey ^= board[1][1][(i*8)+c];
                }
            }
        }

        if(node.justcaptured==true){
            returnKey ^= justcaptured[0];
        }
//
//        if(node.captureAvailable==false){
//            returnKey ^= captureAvailable[0];
//        }else{
//            returnKey ^= captureAvailable[1];
//        }

        if(node.playerTurn==1){
            returnKey ^= playerturn[0];
        }else{
            returnKey ^= playerturn[1];
        }

        return returnKey;
    }

    long createHash(Node node, int depthindex, long hash, int originx, int originy, int newx, int newy,
                    int capturex,
                    int capturey, int capturedpiece){

        if(node.playerTurn==1){
            hash^=playerturn[1];
            hash^=playerturn[0];
        }else{
            hash^=playerturn[0];
            hash^=playerturn[1];
        }

        if(node.board[newx][newy]==1){ //xor to remove moved piece from original square
            hash ^= board[0][0][(originx*8)+originy];
        }
        else if(node.board[newx][newy]==2){
            hash ^= board[1][0][(originx*8)+originy];
        }
        else if(node.board[newx][newy]==3){
            hash ^= board[0][1][(originx*8)+originy];
        }
        else if(node.board[newx][newy]==4){
            hash ^= board[1][1][(originx*8)+originy];
        }


        if(node.board[newx][newy]==1){ //xor to place new piece to new square
            hash ^= board[0][0][(newx*8)+newy];
        }
        else if(node.board[newx][newy]==2){
            hash ^= board[1][0][(newx*8)+newy];
        }
        else if(node.board[newx][newy]==3){
            hash ^= board[0][1][(newx*8)+newy];
        }
        else if(node.board[newx][newy]==4){
            hash ^= board[1][1][(newx*8)+newy];
        }

        if(capturedpiece>0){
            if(capturedpiece==1){ //xor captured piece to remove from board
                hash ^= board[0][0][(capturex*8)+capturey];
            }
            else if(capturedpiece==2){
                hash ^= board[1][0][(capturex*8)+capturey];
            }
            else if(capturedpiece==3){
                hash ^= board[0][1][(capturex*8)+capturey];
            }
            else if(capturedpiece==4){
                hash ^= board[1][1][(capturex*8)+capturey];
            }
            hash^=justcaptured[0];
        }

        return hash;
    }

    boolean overrideValue(int depth, ttstorage storage){
        if(depth > storage.depth){
            return true;
        }
        return false;
    }
    boolean overrideValue(int depth, ttstorage storage, long hashNumber, float alpha, float beta, float trueAlpha){
        if(depth > storage.depth && beta > alpha && beta > trueAlpha){
                storage.depth = depth;
                ss.put(hashNumber,storage);
                return true;
        }
        return false;
    }
}
