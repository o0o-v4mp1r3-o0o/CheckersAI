package AI;

import BoardAndGame.board;

import java.util.*;
import java.util.stream.Collectors;

public class Player extends Game{
    public static int maxdepth = 9;
    Evaluate addedScore = new Evaluate();
    public long numberOfNodes;
    public long nodesPruned = 0;
    public long bestmovenotpicked = 0;
    tt tt = new tt();
    Set<Long> avoidRepeats = new HashSet<>(6000);
    ArrayList<Node> debugger = new ArrayList<>();

    boolean checkposition(short[][] board){
        int check = 0;
        if(board[2][5]==3) check++;
        if(board[2][7]==2) check++;
        if(board[3][2]==2) check++;
        if(board[4][3]==3) check++;
        if(board[6][3]==2) check++;
        if(board[7][6]==4) check++;
        if(board[6][7]==1) check++;
        return check==7?true:false;
    }

    boolean integriryChecker(Node node){
        int blackpieces = 0;
        int redpieces = 0;
        for(int i = 0; i < 8; i++){
            for(int c = 0; c < 8; c++){
                if(node.board[i][c]==3 || node.board[i][c]==1) redpieces++;
                if(node.board[i][c]==4 || node.board[i][c]==2) blackpieces++;
            }
        }
        return node.blackpiececount == blackpieces && (node.redpiececount == redpieces ? true : false);
    }

    float dfs(int depth, float alpha, float beta,
            int jumpx, int jumpy, Node curr, int increment, long hashNumber, float trueAlpha, int freePiecePlayerTurn
            , int incstorage, float freepieceScore){
        numberOfNodes++;
        if(numberOfNodes==309){
            System.out.println();
        }
        if((curr.redKingcount > 0 || curr.blackKingcount > 0)){
            if(avoidRepeats.contains(hashNumber)){
                return 0;
            }
            avoidRepeats.add(hashNumber);
        }
//        if(checkposition(curr.board)== true){
//            System.out.println("here");
//        }
        if(tt.ss.containsKey(hashNumber) && (curr.redKingcount > 0 || curr.blackKingcount > 0) ){
            nodesPruned++;
            if(tt.overrideValue(depth,tt.ss.get(hashNumber),hashNumber,alpha)){
                nodesPruned--;
            }else{
                avoidRepeats.remove(hashNumber);
                return tt.ss.get(hashNumber).score;
            }
        }
        Node pastNode = copyNode(curr);
        HashMap<Integer, ArrayList<int[]>> availableMoves;
        if(curr.justcaptured){
            availableMoves = pieceThatCanCapture(curr.playerTurn,curr.board,jumpx,jumpy, curr);
            if(availableMoves.size()==0){
                changeTurn(pastNode);
                changeTurn(curr);
                availableMoves = piecesThatCanMove(curr.playerTurn,curr.board,curr);
            }
        }else{
                availableMoves = piecesThatCanMove(curr.playerTurn, curr.board, curr);
        }

        if(freePiecePlayerTurn>0){
            if(freePiecePlayerTurn==1){
                if(increment-incstorage==4){
                    if(freepieceScore >= pastNode.redpiececount-pastNode.blackpiececount){
                        avoidRepeats.remove(hashNumber);
                        return evaluate(pastNode,availableMoves);
                    }
                    freepieceScore = 0; freePiecePlayerTurn = 0; incstorage = 0;
                }
            }else{
                if(increment-incstorage==4){
                    if(freepieceScore >= pastNode.blackpiececount-pastNode.redpiececount){
                        avoidRepeats.remove(hashNumber);
                        return evaluate(pastNode,availableMoves);
                    }
                    freepieceScore = 0; freePiecePlayerTurn = 0; incstorage = 0;
                }
            }
        }

        if(curr.captureAvailable==true) depth++;
        if(depth<=0 || checkWinner(curr,availableMoves)>0 || increment > maxdepth+10){
            if((curr.redKingcount > 0 || curr.blackKingcount > 0)) avoidRepeats.remove(hashNumber);
            return evaluate(curr,availableMoves);
        }
        if(curr.playerTurn==1){
            float maximum = -Float.MAX_VALUE;
            for(Map.Entry<Integer, ArrayList<int[]>> eachPiece : availableMoves.entrySet()) {
                int x = eachPiece.getKey()/10;
                int y = eachPiece.getKey()%10;
                ArrayList<int[]> eachMove = eachPiece.getValue();
                for(int i = 0; i < eachMove.size(); i++) {
                    curr = copyNode(pastNode);
                    int newx = eachMove.get(i)[0];
                    int newy = eachMove.get(i)[1];
                    int capturedpiece = curr.board[(x+newx)/2][(y+newy)/2];
                    int movedPiece = curr.board[x][y];
                    nodeMove(curr, curr.playerTurn, x, y, newx, newy);
                    if(curr.justcaptured){jumpx = newx; jumpy=newy; freePiecePlayerTurn = 1;incstorage
                            = increment; increment-=1; freepieceScore = curr.redpiececount-curr.blackpiececount;}
                    long newhash = tt.createHash(curr,pastNode,0,hashNumber,x,y,newx,newy,(x+newx)/2,(y+newy)/2,
                            capturedpiece);
                    float eval = dfs(depth - 1, alpha, beta,jumpx,jumpy,curr, increment+1, newhash,trueAlpha,
                            freePiecePlayerTurn, incstorage, freepieceScore);
                    undoTurn(curr,x,y,newx,newy,capturedpiece,movedPiece);
                    maximum = Math.max(maximum, eval);
                    alpha = Math.max(alpha, maximum);
                    //if(alpha >= maximum) newhash ^= AI.tt.overridden[0];
                    if(curr.redKingcount > 0 || curr.blackKingcount > 0){
                        ttstorage storage = new ttstorage(); storage.depth = depth; storage.score = eval;
                        tt.ss.put(newhash, storage);
                    }
                    if(trueAlpha >= beta){
                        break;
                    }
                    if (alpha >= beta) {
                        break;
                    }
                }
            }
            if((curr.redKingcount > 0 || curr.blackKingcount > 0)) avoidRepeats.remove(hashNumber);
            return alpha;
        }else{
            float minimum = Float.MAX_VALUE;
            for(Map.Entry<Integer, ArrayList<int[]>> eachPiece : availableMoves.entrySet()) {
                int x = eachPiece.getKey()/10;
                int y = eachPiece.getKey()%10;
                ArrayList<int[]> eachMove = eachPiece.getValue();
                for(int i = 0; i < eachMove.size(); i++) {
                    curr = copyNode(pastNode);
                    int newx = eachMove.get(i)[0];
                    int newy = eachMove.get(i)[1];
                    int capturedpiece = curr.board[(x+newx)/2][(y+newy)/2];
                    int movedPiece = curr.board[x][y];
                    nodeMove(curr, curr.playerTurn, x, y, newx, newy);
                    if(curr.justcaptured){jumpx = newx; jumpy=newy;freePiecePlayerTurn = 2; incstorage
                            = increment; increment-=1; freepieceScore = curr.blackpiececount-curr.redpiececount;}
                    long newhash = tt.createHash(curr,pastNode,0,hashNumber,x,y,newx,newy,(x+newx)/2,(y+newy)/2,
                            capturedpiece);
                    float eval = dfs(depth - 1, alpha, beta,jumpx,jumpy,curr,increment+1, newhash, trueAlpha,
                            freePiecePlayerTurn,incstorage,freepieceScore);
                    undoTurn(curr,x,y,newx,newy,capturedpiece,movedPiece);
                    minimum = Math.min(minimum,eval);
                    beta = Math.min(beta,minimum);
                    //if(beta <= minimum) newhash ^= AI.tt.overridden[0];
                    if(curr.redKingcount > 0 || curr.blackKingcount > 0){
                        ttstorage storage = new ttstorage(); storage.depth = depth; storage.score = eval;
                        tt.ss.put(newhash, storage);
                    }
                    if(trueAlpha >= beta){
                        break;
                    }
                    if (alpha >= beta) {
                        break;
                    }
                }
            }
            if((curr.redKingcount > 0 || curr.blackKingcount > 0)) avoidRepeats.remove(hashNumber);
            return beta;
        }
    }

    public float iterativedfsREAL(int depth, float alpha, float beta,
              int jumpx, int jumpy, Node curr, int increment, long hashNumber, float trueAlpha, int freePiecePlayerTurn
            , int incstorage, float freepieceScore){
        numberOfNodes++;
        //curr.hashNumber = hashNumber;
        if((curr.redKingcount > 0 || curr.blackKingcount > 0)){
            if(avoidRepeats.contains(hashNumber)){
                return 0;
            }
            avoidRepeats.add(hashNumber);
        }
//        if(checkposition(curr.board)){
//            debugger.add(deepCopyNode(curr));
//        }
        int bestMove = 0; boolean bmtrigger = false;
        ttstorage ttt = null;
        if(tt.ss.containsKey(hashNumber)){
            nodesPruned++;
            ttt = tt.ss.get(hashNumber);
            if(tt.overrideValue(depth,tt.ss.get(hashNumber),hashNumber,alpha,beta, trueAlpha)){
                nodesPruned--;
                bestMove = tt.ss.get(hashNumber).bestmove;
                bmtrigger=true;
//                if(bestMove==4321 && depth==2 && increment==4){
//                    System.out.println();
//                }
            }else{
                avoidRepeats.remove(hashNumber);
                return tt.ss.get(hashNumber).score;
            }
        }
        Node pastNode = copyNode(curr);
        HashMap<Integer, ArrayList<int[]>> availableMoves;
        if(curr.justcaptured){
            availableMoves = pieceThatCanCapture(curr.playerTurn,curr.board,jumpx,jumpy, curr);
            if(availableMoves.size()==0){
                changeTurn(pastNode);
                changeTurn(curr);
                availableMoves = piecesThatCanMove(curr.playerTurn,curr.board,curr);
            }
        }else{
            availableMoves = piecesThatCanMove(curr.playerTurn, curr.board, curr);
        }
        if(availableMoves.size()==1){
            bmtrigger=false;
            bestMove=0;
        }

        if(freePiecePlayerTurn>0){
            if(freePiecePlayerTurn==1){
                if(increment-incstorage==4){
                    if(freepieceScore >= pastNode.redpiececount-pastNode.blackpiececount){
                        avoidRepeats.remove(hashNumber);
                        return evaluate(pastNode,availableMoves);
                    } freepieceScore = 0; freePiecePlayerTurn = 0; incstorage = 0;
                }
            }else{
                if(increment-incstorage==4){
                    if(freepieceScore >= pastNode.blackpiececount-pastNode.redpiececount) {
                        avoidRepeats.remove(hashNumber);
                        return evaluate(pastNode,availableMoves);
                    } freepieceScore = 0; freePiecePlayerTurn = 0; incstorage = 0;
                }
            }
        }

        if(curr.captureAvailable==true) depth++;
        if(depth<=0 || checkWinner(curr,availableMoves)>0 || increment > maxdepth+10){
            if((curr.redKingcount > 0 || curr.blackKingcount > 0)) avoidRepeats.remove(hashNumber);
            return evaluate(curr,availableMoves);
        }
        if(curr.playerTurn==1){
            float maximum = -Float.MAX_VALUE;
            for(int ii = 0; ii < 2; ii++){
                if(!bmtrigger && ii==0) continue;
                for(Map.Entry<Integer, ArrayList<int[]>> eachPiece : availableMoves.entrySet()) {
                    int x;
                    int y;
                    if (bestMove > 0 && ii==0) {
                        x = bestMove / 1000;
                        y = (bestMove % 1000) / 100;
                    } else {
                        x = eachPiece.getKey() / 10;
                        y = eachPiece.getKey() % 10;
                    }
                    if(ii==1){
                        if(x==bestMove / 1000 && y==(bestMove % 1000) / 100) continue;
                    }
                    ArrayList<int[]> eachMove;
                    if (bestMove > 0 && ii==0) {
                        eachMove = availableMoves.get(x*10+y);
                        if(eachMove==null){
                            bestmovenotpicked++;
                            break;
                        }
                        Collections.swap(eachMove, 0, findIndex(eachMove,(bestMove % 100) / 10,bestMove % 10));
                    } else {
                        eachMove = eachPiece.getValue();
                    }

                    for (int i = 0; i < eachMove.size(); i++) {
                        curr = copyNode(pastNode);
                        int newx;
                        int newy;

                        newx = eachMove.get(i)[0];
                        newy = eachMove.get(i)[1];
                        int capturedpiece = curr.board[(x + newx) / 2][(y + newy) / 2];
                        int movedPiece = curr.board[x][y];
//                        if (!integriryChecker(curr)) {
//                            System.out.println();
//                        }
//                        if (newy == 1 && newx == 2 && y == 3 && x == 4 && depth==2) {
//                            System.out.println();
//                        }
                        nodeMove(curr, curr.playerTurn, x, y, newx, newy);
//                        if (!integriryChecker(curr)) {
//                            System.out.println();
//                        }
                        if (curr.justcaptured) {
                            jumpx = newx;
                            jumpy = newy;
                            freePiecePlayerTurn = 1;
                            incstorage
                                    = increment;
                            increment -= 1;
                            freepieceScore = curr.redpiececount - curr.blackpiececount;
                        }
//                        if(checkposition(curr.board)){
//                            System.out.println();
//                        }
                        long newhash = tt.initHash(curr, 0);
                        float eval = iterativedfs(depth - 1, alpha, beta, jumpx, jumpy, curr, increment + 1, newhash, trueAlpha,
                                freePiecePlayerTurn, incstorage, freepieceScore);
        //                    if(!integriryChecker(curr)){
        //                        System.out.println();
        //                    }
                        undoTurn(curr, x, y, newx, newy, capturedpiece, movedPiece);
        //                    if(!integriryChecker(curr)){
        //                        System.out.println();
        //                    }
                        boolean foundnewbest = false;
                        if (eval > maximum) {
                            maximum = eval;
                            foundnewbest = true;
                        }
                        alpha = Math.max(alpha, maximum);
                        //if(alpha >= maximum) newhash ^= AI.tt.overridden[0];
                        ttstorage storage = new ttstorage();
                        storage.depth = depth;
                        storage.score = eval;
                        int scoreCode = x;
                        scoreCode = (scoreCode * 10) + y;
                        scoreCode = (scoreCode * 10) + newx;
                        scoreCode = (scoreCode * 10) + newy;

                        if (!tt.ss.containsKey(hashNumber)) {
                            storage.bestmove = scoreCode;
                            tt.ss.put(hashNumber, storage);
                        } else if (foundnewbest) {
                            storage.bestmove = scoreCode;
                            tt.ss.put(hashNumber, storage);
                        }
                        if (trueAlpha >= beta) {
                            break;
                        }
                        if (alpha >= beta) {
                            break;
                        }
                    }
                    if(bmtrigger && ii==0) {
                        break;
                    }
                }
            }
            if((curr.redKingcount > 0 || curr.blackKingcount > 0)) avoidRepeats.remove(hashNumber);
            return alpha;
        }else{
            float minimum = Float.MAX_VALUE;
            for(int ii = 0; ii < 2; ii++){
                if(!bmtrigger && ii==0) continue;
                for(Map.Entry<Integer, ArrayList<int[]>> eachPiece : availableMoves.entrySet()) {
                    int x;
                    int y;
                    if (bestMove > 0 && ii==0) {
                        x = bestMove / 1000;
                        y = (bestMove % 1000) / 100;
                    } else {
                        x = eachPiece.getKey() / 10;
                        y = eachPiece.getKey() % 10;
                    }
                    if(ii==1){
                        if(x==bestMove / 1000 && y==(bestMove % 1000) / 100) continue;
                    }
                    ArrayList<int[]> eachMove;
                    if (bestMove > 0 && ii==0) {
                        eachMove = availableMoves.get(x*10+y);
                        if(eachMove==null){
                            bestmovenotpicked++;
                            break;
                        }
                        Collections.swap(eachMove, 0, findIndex(eachMove,(bestMove % 100) / 10,bestMove % 10));

                    } else {
                        eachMove = eachPiece.getValue();
                    }
                    for (int i = 0; i < eachMove.size(); i++) {
                        curr = copyNode(pastNode);
                        int newx;
                        int newy;
                        newx = eachMove.get(i)[0];
                        newy = eachMove.get(i)[1];

                        int capturedpiece = curr.board[(x + newx) / 2][(y + newy) / 2];
                        int movedPiece = curr.board[x][y];
//                        if (!integriryChecker(curr)) {
//                            System.out.println();
//                        }
//                        if (newy == 1 && newx == 2 && y == 3 && x == 4 && depth==2) {
//                            System.out.println();
//                        }
                        nodeMove(curr, curr.playerTurn, x, y, newx, newy);
//                        if (!integriryChecker(curr)) {
//                            System.out.println();
//                        }
                        if (curr.justcaptured) {
                            jumpx = newx;
                            jumpy = newy;
                            freePiecePlayerTurn = 2;
                            incstorage
                                    = increment;
                            increment -= 1;
                            freepieceScore = curr.blackpiececount - curr.redpiececount;
                        }
//                        if(checkposition(curr.board)){
//                            System.out.println();
//                        }
                        long newhash = tt.initHash(curr, 0);
                        float eval = iterativedfs(depth - 1, alpha, beta, jumpx, jumpy, curr, increment + 1, newhash, trueAlpha,
                                freePiecePlayerTurn, incstorage, freepieceScore);
    //                    if(!integriryChecker(curr)){
    //                        System.out.println();
    //                    }
                        undoTurn(curr, x, y, newx, newy, capturedpiece, movedPiece);
    //                    if(!integriryChecker(curr)){
    //                        System.out.println();
    //                    }
                        boolean foundnewbest = false;
                        if (eval < minimum) {
                            minimum = eval;
                            foundnewbest = true;
                        }
                        beta = Math.min(beta, minimum);

                        ttstorage storage = new ttstorage();
                        storage.depth = depth;
                        storage.score = eval;
                        int scoreCode = x;
                        scoreCode = (scoreCode * 10) + y;
                        scoreCode = (scoreCode * 10) + newx;
                        scoreCode = (scoreCode * 10) + newy;

                        if (!tt.ss.containsKey(hashNumber)) {
                            storage.bestmove = scoreCode;
                            tt.ss.put(hashNumber, storage);
//                            if(scoreCode==4321){
//                                System.out.println();
//                            }
                        }
                        if (foundnewbest) {
                            storage.bestmove = scoreCode;
                            tt.ss.put(hashNumber, storage);
//                            if(scoreCode==4321){
//                                System.out.println();
//                            }
                        }
                        if (trueAlpha >= beta) {
                            break;
                        }
                        if (alpha >= beta) {
                            break;
                        }
                    }
                    if(bmtrigger && ii==0) {
                        break;
                    }
                }
            }
            if((curr.redKingcount > 0 || curr.blackKingcount > 0)) avoidRepeats.remove(hashNumber);
            return beta;
        }
    }

    public float iterativedfs(int depth, float alpha, float beta,
                              int jumpx, int jumpy, Node curr, int increment, long hashNumber, float trueAlpha, int freePiecePlayerTurn
            , int incstorage, float freepieceScore){
        numberOfNodes++;
        //curr.hashNumber = hashNumber;
        if((curr.redKingcount > 0 || curr.blackKingcount > 0)){
//            if(avoidRepeats.contains(hashNumber)){
//                return -50;
//            }
            avoidRepeats.add(hashNumber);
        }
//        if(checkposition(curr.board)){
//            debugger.add(deepCopyNode(curr));
//        }

        ttstorage ttt = null;
        if(tt.ss.containsKey(hashNumber)){
            nodesPruned++;
            ttt = tt.ss.get(hashNumber);
            if(tt.overrideValue(depth,tt.ss.get(hashNumber),hashNumber,alpha,beta, trueAlpha)){
                nodesPruned--;

//                if(bestMove==4321 && depth==2 && increment==4){
//                    System.out.println();
//                }
            }else{
                avoidRepeats.remove(hashNumber);
                return tt.ss.get(hashNumber).score;
            }
        }
        Node pastNode = copyNode(curr);
        HashMap<Integer, ArrayList<int[]>> availableMoves;
        if(curr.justcaptured){
            availableMoves = pieceThatCanCapture(curr.playerTurn,curr.board,jumpx,jumpy, curr);
            if(availableMoves.size()==0){
                changeTurn(pastNode);
                changeTurn(curr);
                availableMoves = piecesThatCanMove(curr.playerTurn,curr.board,curr);
            }
        }else{
            availableMoves = piecesThatCanMove(curr.playerTurn, curr.board, curr);
        }


        if(freePiecePlayerTurn>0){
            if(freePiecePlayerTurn==1){
                if(increment-incstorage==4){
                    if(freepieceScore >= pastNode.redpiececount-pastNode.blackpiececount){
                        avoidRepeats.remove(hashNumber);
                        return evaluate(pastNode,availableMoves);
                    } freepieceScore = 0; freePiecePlayerTurn = 0; incstorage = 0;
                }
            }else{
                if(increment-incstorage==4){
                    if(freepieceScore >= pastNode.blackpiececount-pastNode.redpiececount) {
                        avoidRepeats.remove(hashNumber);
                        return evaluate(pastNode,availableMoves);
                    } freepieceScore = 0; freePiecePlayerTurn = 0; incstorage = 0;
                }
            }
        }

        if(curr.captureAvailable==true) depth++;
        if(depth<=0 || checkWinner(curr,availableMoves)>0 || increment > maxdepth+10){
            if((curr.redKingcount > 0 || curr.blackKingcount > 0)) avoidRepeats.remove(hashNumber);
            return evaluate(curr,availableMoves);
        }
        if(curr.playerTurn==1){
            float maximum = -Float.MAX_VALUE;
            for(int ii = 0; ii < 2; ii++){

                for(Map.Entry<Integer, ArrayList<int[]>> eachPiece : availableMoves.entrySet()) {
                    int x;
                    int y;

                        x = eachPiece.getKey() / 10;
                        y = eachPiece.getKey() % 10;

                    ArrayList<int[]> eachMove;

                        eachMove = eachPiece.getValue();


                    for (int i = 0; i < eachMove.size(); i++) {
                        curr = copyNode(pastNode);
                        int newx;
                        int newy;

                        newx = eachMove.get(i)[0];
                        newy = eachMove.get(i)[1];
                        int capturedpiece = curr.board[(x + newx) / 2][(y + newy) / 2];
                        int movedPiece = curr.board[x][y];
//                        if (!integriryChecker(curr)) {
//                            System.out.println();
//                        }
//                        if (newy == 1 && newx == 2 && y == 3 && x == 4 && depth==2) {
//                            System.out.println();
//                        }
                        nodeMove(curr, curr.playerTurn, x, y, newx, newy);
//                        if (!integriryChecker(curr)) {
//                            System.out.println();
//                        }
                        if (curr.justcaptured) {
                            jumpx = newx;
                            jumpy = newy;
                            freePiecePlayerTurn = 1;
                            incstorage
                                    = increment;
                            increment -= 1;
                            freepieceScore = curr.redpiececount - curr.blackpiececount;
                        }
//                        if(checkposition(curr.board)){
//                            System.out.println();
//                        }
                        long newhash = tt.initHash(curr, 0);
                        float eval = iterativedfs(depth - 1, alpha, beta, jumpx, jumpy, curr, increment + 1, newhash, trueAlpha,
                                freePiecePlayerTurn, incstorage, freepieceScore);
                        //                    if(!integriryChecker(curr)){
                        //                        System.out.println();
                        //                    }
                        undoTurn(curr, x, y, newx, newy, capturedpiece, movedPiece);
                        //                    if(!integriryChecker(curr)){
                        //                        System.out.println();
                        //                    }
                        boolean foundnewbest = false;
                        if (eval > maximum) {
                            maximum = eval;
                            foundnewbest = true;
                        }
                        alpha = Math.max(alpha, maximum);
                        //if(alpha >= maximum) newhash ^= AI.tt.overridden[0];
                        ttstorage storage = new ttstorage();
                        storage.depth = depth;
                        storage.score = eval;
                        int scoreCode = x;
                        scoreCode = (scoreCode * 10) + y;
                        scoreCode = (scoreCode * 10) + newx;
                        scoreCode = (scoreCode * 10) + newy;

                        if (!tt.ss.containsKey(hashNumber)) {
                            storage.bestmove = scoreCode;
                            tt.ss.put(hashNumber, storage);
                        } else if (foundnewbest) {
                            storage.bestmove = scoreCode;
                            tt.ss.put(hashNumber, storage);
                        }
                        if (trueAlpha >= beta) {
                            break;
                        }
                        if (alpha >= beta) {
                            break;
                        }
                    }

                }
            }
            if((curr.redKingcount > 0 || curr.blackKingcount > 0)) avoidRepeats.remove(hashNumber);
            return alpha;
        }else{
            float minimum = Float.MAX_VALUE;
            for(int ii = 0; ii < 2; ii++){

                for(Map.Entry<Integer, ArrayList<int[]>> eachPiece : availableMoves.entrySet()) {
                    int x;
                    int y;

                        x = eachPiece.getKey() / 10;
                        y = eachPiece.getKey() % 10;


                    ArrayList<int[]> eachMove;

                        eachMove = eachPiece.getValue();

                    for (int i = 0; i < eachMove.size(); i++) {
                        curr = copyNode(pastNode);
                        int newx;
                        int newy;
                        newx = eachMove.get(i)[0];
                        newy = eachMove.get(i)[1];

                        int capturedpiece = curr.board[(x + newx) / 2][(y + newy) / 2];
                        int movedPiece = curr.board[x][y];
//                        if (!integriryChecker(curr)) {
//                            System.out.println();
//                        }
//                        if (newy == 1 && newx == 2 && y == 3 && x == 4 && depth==2) {
//                            System.out.println();
//                        }
                        nodeMove(curr, curr.playerTurn, x, y, newx, newy);
//                        if (!integriryChecker(curr)) {
//                            System.out.println();
//                        }
                        if (curr.justcaptured) {
                            jumpx = newx;
                            jumpy = newy;
                            freePiecePlayerTurn = 2;
                            incstorage
                                    = increment;
                            increment -= 1;
                            freepieceScore = curr.blackpiececount - curr.redpiececount;
                        }
//                        if(checkposition(curr.board)){
//                            System.out.println();
//                        }
                        long newhash = tt.initHash(curr, 0);
                        float eval = iterativedfs(depth - 1, alpha, beta, jumpx, jumpy, curr, increment + 1, newhash, trueAlpha,
                                freePiecePlayerTurn, incstorage, freepieceScore);
                        //                    if(!integriryChecker(curr)){
                        //                        System.out.println();
                        //                    }
                        undoTurn(curr, x, y, newx, newy, capturedpiece, movedPiece);
                        //                    if(!integriryChecker(curr)){
                        //                        System.out.println();
                        //                    }
                        boolean foundnewbest = false;
                        if (eval < minimum) {
                            minimum = eval;
                            foundnewbest = true;
                        }
                        beta = Math.min(beta, minimum);

                        ttstorage storage = new ttstorage();
                        storage.depth = depth;
                        storage.score = eval;
                        int scoreCode = x;
                        scoreCode = (scoreCode * 10) + y;
                        scoreCode = (scoreCode * 10) + newx;
                        scoreCode = (scoreCode * 10) + newy;

                        if (!tt.ss.containsKey(hashNumber)) {
                            storage.bestmove = scoreCode;
                            tt.ss.put(hashNumber, storage);
//                            if(scoreCode==4321){
//                                System.out.println();
//                            }
                        }
                        if (foundnewbest) {
                            storage.bestmove = scoreCode;
                            tt.ss.put(hashNumber, storage);
//                            if(scoreCode==4321){
//                                System.out.println();
//                            }
                        }
                        if (trueAlpha >= beta) {
                            break;
                        }
                        if (alpha >= beta) {
                            break;
                        }
                    }

                }
            }
            if((curr.redKingcount > 0 || curr.blackKingcount > 0)) avoidRepeats.remove(hashNumber);
            return beta;
        }
    }

    float evaluate(Node node,HashMap<Integer, ArrayList<int[]>> availableMoves){
        float score = 0;
        if(node.blackpiececount==0) return 100;
        if(node.redpiececount==0) return -100;
        if(availableMoves.size()==0){
            if(node.playerTurn==1) return -100;
            if(node.playerTurn==2) return 100;
        }
        if(node.blackpiececount > node.redpiececount) score -= (node.blackpiececount - node.redpiececount);
        if(node.redpiececount > node.blackpiececount) score += (node.redpiececount - node.blackpiececount);
        if(node.blackKingcount > node.redKingcount) score -= ((node.blackKingcount - node.redKingcount)*.5);
        if(node.redKingcount > node.blackKingcount) score += ((node.redKingcount - node.blackKingcount)*.5);
        return score;
    }

    int findIndex(ArrayList<int[]> availableMoves, int x, int y){
        int index = 0;
        for(int i = 0; i < availableMoves.size(); i++){
            if(availableMoves.get(i)[0]==x && availableMoves.get(i)[1]==y) return i;
        }
        return -1;
    }

    HashMap<Integer,Float> iterateThroughMoves(Node curr){
        HashMap<Integer,Float> scores = new HashMap<>();
        HashMap<Integer, ArrayList<int[]>> availableMoves;
        availableMoves = piecesThatCanMove(curr.playerTurn,curr.board,curr);
        float trueAlpha = -Float.MAX_VALUE;
        for(Map.Entry<Integer, ArrayList<int[]>> eachPiece : availableMoves.entrySet()) {
            int x = eachPiece.getKey()/10;
            int y = eachPiece.getKey()%10;
            ArrayList<int[]> eachMove = eachPiece.getValue();
            for(int i = 0; i < eachMove.size(); i++) {
                Node node = copyNode(curr);
                int newx = eachMove.get(i)[0];
                int newy = eachMove.get(i)[1];
                int capturedpiece = node.board[(x+newx)/2][(y+newy)/2];
                int movedPiece = node.board[x][y];
                nodeMove(node,node.playerTurn,x,y,newx,newy);
                int jumpx = 0; int jumpy = 0;
                if(node.justcaptured){jumpx = newx; jumpy=newy;}
                if(x==7 && y==2){
                    System.out.println("hi"); //o
                }
                if(x==7 && y==6 && newx==6 && newy==5){
                    System.out.println("hi");
                }
                long hashNumber = tt.initHash(node,0);
                float score = dfs(maxdepth,-Float.MAX_VALUE,Float.MAX_VALUE,jumpx,jumpy,node,0, hashNumber, trueAlpha
                        , 0,0,0);
                undoTurn(curr,x,y,newx,newy,capturedpiece,movedPiece);
                trueAlpha = Math.max(trueAlpha,score);
                int scoreCode = x;
                scoreCode = (scoreCode*10)+y;
                scoreCode = (scoreCode*10)+newx;
                scoreCode = (scoreCode*10)+newy;
                scores.put(scoreCode,score);
            }
        }
        return scores;
    }

    LinkedHashMap<Integer,Float> shallowSearch(Node curr){
        HashMap<Integer,Float> scores = new HashMap<>();
        HashMap<Integer, ArrayList<int[]>> availableMoves;
        availableMoves = piecesThatCanMove(curr.playerTurn,curr.board,curr);
        float trueAlpha = -Float.MAX_VALUE;
        for(Map.Entry<Integer, ArrayList<int[]>> eachPiece : availableMoves.entrySet()) {
            int x = eachPiece.getKey()/10;
            int y = eachPiece.getKey()%10;
            ArrayList<int[]> eachMove = eachPiece.getValue();
            for(int i = 0; i < eachMove.size(); i++) {
                Node node = copyNode(curr);
                int newx = eachMove.get(i)[0];
                int newy = eachMove.get(i)[1];
                int capturedpiece = node.board[(x+newx)/2][(y+newy)/2];
                int movedPiece = node.board[x][y];
                nodeMove(node,node.playerTurn,x,y,newx,newy);
                int jumpx = 0; int jumpy = 0;
                if(node.justcaptured){jumpx = newx; jumpy=newy;}
                if(x==7 && y==2){
                    System.out.println("hi"); //o
                }
                if(x==7 && y==6 && newx==6 && newy==5){
                    System.out.println("hi");
                }
                long hashNumber = tt.initHash(node,0);
                float score = dfs(3,-Float.MAX_VALUE,Float.MAX_VALUE,jumpx,jumpy,node,0, hashNumber, trueAlpha, 0,0,0);
                undoTurn(curr,x,y,newx,newy,capturedpiece,movedPiece);
                trueAlpha = Math.max(trueAlpha,score);
                int scoreCode = x;
                scoreCode = (scoreCode*10)+y;
                scoreCode = (scoreCode*10)+newx;
                scoreCode = (scoreCode*10)+newy;
                scores.put(scoreCode,score);
            }
            numberOfNodes = 0;
        }
        LinkedHashMap ss;
        ss = scores.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        tt.ss.clear();
        return ss;
    }

    LinkedHashMap<Integer,Float> shallowSearch(Node curr, int finalI, int finalR){
        HashMap<Integer,Float> scores = new HashMap<>();
        HashMap<Integer, ArrayList<int[]>> availableMoves;
        availableMoves = pieceThatCanCapture(1,curr.board,finalI,finalR,curr);
        float trueAlpha = -Float.MAX_VALUE;
        for(Map.Entry<Integer, ArrayList<int[]>> eachPiece : availableMoves.entrySet()) {
            int x = eachPiece.getKey()/10;
            int y = eachPiece.getKey()%10;
            ArrayList<int[]> eachMove = eachPiece.getValue();
            for(int i = 0; i < eachMove.size(); i++) {
                Node node = copyNode(curr);
                int newx = eachMove.get(i)[0];
                int newy = eachMove.get(i)[1];
                int capturedpiece = node.board[(x+newx)/2][(y+newy)/2];
                int movedPiece = node.board[x][y];
                nodeMove(node,node.playerTurn,x,y,newx,newy);
                int jumpx = 0; int jumpy = 0;
                if(node.justcaptured){jumpx = newx; jumpy=newy;}
                if(x==7 && y==2){
                    System.out.println("hi"); //o
                }
                if(x==7 && y==6 && newx==6 && newy==5){
                    System.out.println("hi");
                }
                long hashNumber = tt.initHash(node,0);
                float score = dfs(3,-Float.MAX_VALUE,Float.MAX_VALUE,jumpx,jumpy,node,0, hashNumber, trueAlpha, 0,0,0);
                undoTurn(curr,x,y,newx,newy,capturedpiece,movedPiece);
                trueAlpha = Math.max(trueAlpha,score);
                int scoreCode = x;
                scoreCode = (scoreCode*10)+y;
                scoreCode = (scoreCode*10)+newx;
                scoreCode = (scoreCode*10)+newy;
                scores.put(scoreCode,score);
            }
            numberOfNodes = 0;
        }
        LinkedHashMap ss;
        ss = scores.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        tt.ss.clear();
        return ss;
    }

    public HashMap<Integer,Float> iterativeDeepening(Node curr, int finalI, int finalR){
        LinkedHashMap<Integer,Float> shallow = finalI==-1?shallowSearch(curr):shallowSearch(curr,finalI,finalR);
        HashMap<Integer,Float> scores = new HashMap<>();
        float trueAlpha = -Float.MAX_VALUE;
        int depth = maxdepth;
        if(curr.blackpiececount+curr.redpiececount<9) {
            depth = maxdepth+5;
        }
        for(int i = 0; i < depth; i++) {
            if(i==14){
                System.out.println();
            }
            for (Map.Entry<Integer, Float> ss : shallow.entrySet()) {
                shallow.entrySet().stream()
                        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()));
                int x = ss.getKey() / 1000;
                int y = (ss.getKey() % 1000) / 100;
                int newx = (ss.getKey() % 100) / 10;
                int newy = (ss.getKey() % 10);
                Node node = copyNode(curr);
                int capturedpiece = node.board[(x + newx) / 2][(y + newy) / 2];
                int movedPiece = node.board[x][y];
                nodeMove(node, node.playerTurn, x, y, newx, newy);
                int jumpx = 0;
                int jumpy = 0;
                if (node.justcaptured) {
                    jumpx = newx;
                    jumpy = newy;
                }
                if (x == 7 && y == 2) {
                    System.out.println("hi"); //o
                }
                if (x == 4 && y == 3 && newx == 3 && newy == 4 && i==10) {
                    System.out.println("hi");
                }
                long hashNumber = tt.initHash(node, 0);
                float score = iterativedfs(i + 1, -Float.MAX_VALUE, Float.MAX_VALUE, jumpx, jumpy, node, 0,
                        hashNumber, trueAlpha, 0,0,0);
                undoTurn(curr, x, y, newx, newy, capturedpiece, movedPiece);
//                if(!checkposition(node.board) && !integriryChecker(node)){
//                    System.out.println();
//                }
                trueAlpha = Math.max(trueAlpha, score);
                int scoreCode = x;
                scoreCode = (scoreCode * 10) + y;
                scoreCode = (scoreCode * 10) + newx;
                scoreCode = (scoreCode * 10) + newy;
                shallow.put(scoreCode, score);
                avoidRepeats.clear();
            }
        }
        scores.putAll(shallow);
        tt.ss.clear();

        return scores;
    }
}
