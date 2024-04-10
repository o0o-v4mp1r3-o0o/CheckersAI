package AI;

import BoardAndGame.board;

import java.io.IOException;
import java.util.*;

public class Game {
    short[][] board = new short[8][8];
    int blackPiececount;
    int redPieceCount;
    HashMap<String,Integer> blackPieces = new HashMap<>();
    HashMap<String,Integer> redPieces = new HashMap<>();
    int drawmoves = 0;

    void initBoard(){
        blackPiececount=12;
        redPieceCount=12;
        blackPieces.clear();
        redPieces.clear();
        //init black side
        for(int i = 0; i < 3; i++){
            for(int c = 0; c < 8; c++){
                if(i%2==0) {
                    if (c % 2 == 1) {
                        board[i][c] = 2;
                        blackPieces.put(String.valueOf(i)+ c,2);
                    }
                }else{
                    if (c % 2 == 0) {
                        board[i][c] = 2;
                        //blackPieces.put(i*10+c,2);
                    }
                }
            }
        }

        //init red side
        for(int i = 5; i < 8; i++){
            for(int c = 0; c < 8; c++){
                if(i%2==1) {
                    if (c % 2 == 0) {
                        board[i][c] = 1;
                        //redPieces.put(i*10+c,2);
                    }
                }else{
                    if (c % 2 == 1) {
                        board[i][c] = 1;
                        //redPieces.put(i*10+c,2);
                    }
                }
            }
        }
    }

    void printboard(short[][] board){
        for(int i = 0; i < 8; i++){
            for(int c = 0; c < 8; c++){
                System.out.print(board[i][c] + " ");
            }
            System.out.println("");
        }
    }

    short checkWinner(Node node, HashMap<Integer,ArrayList<int[]>> piecesThatCanMove){
        if(node.blackpiececount==0){
            return 1;
        }else if(node.redpiececount==0){
            return 2;
        }else if(node.playerTurn==1 && piecesThatCanMove.size()==0){
            return 2;
        }else if(node.playerTurn==2 && piecesThatCanMove.size()==0){
            return 1;
        }else if(drawCounter(drawmoves)){
            return 0;
        }
        return 0;
    }

    boolean drawCounter(int drawmoves){
        if(drawmoves==50){
            return true;
        }
        return false;
    }

    void nodeMove(Node node,int playerTurn, int originx, int originy, int newx, int newy){
        if(node.board[originx][originy]==3 || node.board[originx][originy]==4){
            node.drawMoves++;
        }else{node.drawMoves=0;}
        short temp = node.board[originx][originy]; //store piece type from board
        node.board[originx][originy]=0; //remove piece from original square
        node.board[newx][newy] = temp; //update piece to the new square moved

        if(Math.abs(originx-newx)==2){ //if the move was a capture
            int piece = node.board[(originx+newx)/2][(originy+newy)/2];
            node.board[(originx+newx)/2][(originy+newy)/2] = 0;
            if(promoteMaterial(newx,newy,node.board)>0){
                if(playerTurn==1){
                    node.board[newx][newy]=3;
                    node.redKingcount++;
                }
                if(playerTurn==2){
                    node.board[newx][newy]=4;
                    node.blackKingcount++;
                }
            }
            if(playerTurn==2){
                node.redpiececount--;
                if(piece==3) node.redKingcount--;
            }
            if(playerTurn==1){
                node.blackpiececount--;
                if(piece==4) node.blackKingcount--;
            }
            node.justcaptured=true;
            return;
        }
        if(promoteMaterial(newx,newy,node.board)>0){
            if(playerTurn==1){
                node.board[newx][newy]=3;
                node.redKingcount++;
            }
            if(playerTurn==2){
                node.board[newx][newy]=4;
                node.blackKingcount++;
            }
        }
        node.justcaptured=false;
        changeTurn(node);
    }

    void undoTurn(Node node,int originx, int originy, int newx, int newy, int capturedPiece, int movedPiece){
        node.board[originx][originy] = (short)movedPiece;
        node.board[newx][newy] = 0;
        if(node.justcaptured){ //if the move was a capture
            node.board[(originx+newx)/2][(originy+newy)/2] = (short)capturedPiece;
        }
    }

    void changeTurn(Node node){
        if(node.playerTurn==1){
            node.playerTurn=2;
        }else{
            node.playerTurn=1;
        }
    }

    Node copyNode(Node node){
        Node newnode = new Node();
        newnode.playerTurn = node.playerTurn;
        newnode.redpiececount = node.redpiececount;
        newnode.blackpiececount = node.blackpiececount;
        newnode.redKingcount = node.redKingcount;
        newnode.blackKingcount = node.blackKingcount;
        newnode.captureAvailable = node.captureAvailable;
        newnode.drawMoves = node.drawMoves;
        //newnode.board = new short[8][8];
        //boolean inc = true;
//        for(int i = 0; i < node.board.length; i++){
//            for(int c = inc?1:0; c < node.board.length; c+=2){
//                newnode.board[i][c] = node.board[i][c];
//            }
//            inc = !inc;
//        }
        newnode.board = node.board;
        newnode.justcaptured = node.justcaptured;

        return newnode;
    }

    Node deepCopyNode(Node node){
        Node newnode = new Node();
        newnode.playerTurn = node.playerTurn;
        newnode.redpiececount = node.redpiececount;
        newnode.blackpiececount = node.blackpiececount;
        newnode.redKingcount = node.redKingcount;
        newnode.blackKingcount = node.blackKingcount;
        newnode.captureAvailable = node.captureAvailable;
        newnode.drawMoves = node.drawMoves;
        newnode.board = new short[8][8];
        boolean inc = true;
        for(int i = 0; i < node.board.length; i++){
            for(int c = inc?1:0; c < node.board.length; c+=2){
                newnode.board[i][c] = node.board[i][c];
            }
            inc = !inc;
        }
        newnode.board = node.board;
        newnode.justcaptured = node.justcaptured;
        newnode.hashNumber = node.hashNumber;
        return newnode;
    }

    public Node createNode(short[][] board, int blackpiececount, int redpiececount, int drawMoves, boolean justcaptured,
                     int playerTurn, int blackKingcount, int redKingcount, boolean captureAvailable, int depthLevel){
        Node node = new Node();
        node.board = board;
        node.blackpiececount = blackpiececount;
        node.redpiececount = redpiececount; node.drawMoves = drawMoves;
        node.justcaptured = justcaptured;
        node.playerTurn = playerTurn; node.blackKingcount = blackKingcount;
        node.redKingcount = redKingcount;
        node.captureAvailable = captureAvailable;
        node.depthLevel = depthLevel;
//        initCoords(node);
        return node;
    }

    short promoteMaterial(int x, int y, short[][] board){
        if(board[x][y]==2 && x == 7) return 4;
        if(board[x][y]==1 && x == 0) return 3;
        return -1;
    }

    HashMap<Integer,ArrayList<int[]>> piecesThatCanMove(int playerturn, short[][] board, Node node){
        HashMap<Integer,ArrayList<int[]>> capture = piecesThatCanCapture(playerturn, board);;
        if(capture.size()>0){
            node.captureAvailable=true;
            return capture; //check if mandatory capture necessary
        }
        node.captureAvailable=false;
        HashMap<Integer,ArrayList<int[]>> ss = new HashMap<>();
        boolean inc = true;
        if(playerturn==2){ //black
        for(int i = 0; i < 8; i++){ //loop through all pieces to find out who can move
            for(int c = inc?1:0; c < 8; c+=2){
                if(board[i][c]==0) continue;
                int index = i*10+c;
                if(board[i][c]==2 || board[i][c]==4){
                    if(i+1<8 && c-1 > -1){
                        if(board[i+1][c-1]==0){
                            int[] coords = new int[2];
                            coords[0]=i+1; coords[1]=c-1;
                            if(!ss.containsKey(index)){
                                ArrayList<int[]> addCoords = new ArrayList<>();
                                addCoords.add(coords);
                                ss.put(index, addCoords);
                            }else{
                                ss.get(index).add(coords);
                            }
                        }
                    }
                    if(i+1 < 8 && c+1 < 8){
                        if(board[i+1][c+1]==0){
                            int[] coords = new int[2];
                            coords[0]=i+1; coords[1]=c+1;
                            if(!ss.containsKey(index)){
                                ArrayList<int[]> addCoords = new ArrayList<>();
                                addCoords.add(coords);
                                ss.put(index, addCoords);
                            }else{
                                ss.get(index).add(coords);
                            }
                        }
                    }
                }
                if(board[i][c]==4){ //black king moves
                    if(i-1>-1 && c-1 > -1){
                        if(board[i-1][c-1]==0){
                            int[] coords = new int[2];
                            coords[0]=i-1; coords[1]=c-1;
                            if(!ss.containsKey(index)){
                                ArrayList<int[]> addCoords = new ArrayList<>();
                                addCoords.add(coords);
                                ss.put(index, addCoords);
                            }else{
                                ss.get(index).add(coords);
                            }
                        }
                    }
                    if(i-1 >-1 && c+1 < 8){
                        if(board[i-1][c+1]==0){
                            int[] coords = new int[2];
                            coords[0]=i-1; coords[1]=c+1;
                            if(!ss.containsKey(index)){
                                ArrayList<int[]> addCoords = new ArrayList<>();
                                addCoords.add(coords);
                                ss.put(index, addCoords);
                            }else{
                                ss.get(index).add(coords);
                            }
                        }
                    }
                }
            }
            inc = !inc;
            }
        }else{
            for(int i = 0; i < 8; i++){ // red loop through all pieces to find out who can move
                for(int c = inc?1:0; c < 8; c+=2){
                    if(board[i][c]==0) continue;
                    int index = i*10+c;
                    if(board[i][c]==1 || board[i][c]==3){
                        if(i-1>-1 && c+1 < 8){
                            if(board[i-1][c+1]==0){
                                int[] coords = new int[2];
                                coords[0]=i-1; coords[1]=c+1;
                                if(!ss.containsKey(index)){
                                    ArrayList<int[]> addCoords = new ArrayList<>();
                                    addCoords.add(coords);
                                    ss.put(index, addCoords);
                                }else{
                                    ss.get(index).add(coords);
                                }
                            }
                        }
                        if(i-1>-1 && c-1>-1){
                            if(board[i-1][c-1]==0){
                                int[] coords = new int[2];
                                coords[0]=i-1; coords[1]=c-1;
                                if(!ss.containsKey(index)){
                                    ArrayList<int[]> addCoords = new ArrayList<>();
                                    addCoords.add(coords);
                                    ss.put(index, addCoords);
                                }else{
                                    ss.get(index).add(coords);
                                }
                            }
                        }
                    }
                    if(board[i][c]==3){ //red king moves
                        if(i+1<8 && c-1 > -1){
                            if(board[i+1][c-1]==0){
                                int[] coords = new int[2];
                                coords[0]=i+1; coords[1]=c-1;
                                if(!ss.containsKey(index)){
                                    ArrayList<int[]> addCoords = new ArrayList<>();
                                    addCoords.add(coords);
                                    ss.put(index, addCoords);
                                }else{
                                    ss.get(index).add(coords);
                                }
                            }
                        }
                        if(i+1 < 8 && c+1 < 8){
                            if(board[i+1][c+1]==0){
                                int[] coords = new int[2];
                                coords[0]=i+1; coords[1]=c+1;
                                if(!ss.containsKey(index)){
                                    ArrayList<int[]> addCoords = new ArrayList<>();
                                    addCoords.add(coords);
                                    ss.put(index, addCoords);
                                }else{
                                    ss.get(index).add(coords);
                                }
                            }
                        }
                    }
                }
                inc = !inc;
            }
        }
        return ss;
    }
    HashMap<Integer,ArrayList<int[]>> pieceThatCanCapture(int playerTurn, short[][] board, int i, int c, Node node){
        HashMap<Integer, ArrayList<int[]>> temp = new HashMap<>();
        int index = i*10+c;
        if (board[i][c] == 2 || board[i][c] == 4) {
            if (i + 2 < 8 && c - 2 > -1 && board[i + 2][c - 2] == 0) { //capture left
                if (board[i + 1][c - 1] == 1 || board[i + 1][c - 1] == 3) {
                    int[] coords = new int[2];
                    coords[0] = i + 2;
                    coords[1] = c - 2;
                    if (!temp.containsKey(index)) {
                        ArrayList<int[]> addCoords = new ArrayList<>();
                        addCoords.add(coords);
                        temp.put(index, addCoords);
                    } else {
                        temp.get(index).add(coords);
                    }
                }
            }
            if (i + 2 < 8 && c + 2 < 8 && board[i + 2][c + 2] == 0) { //capture right
                if (board[i + 1][c + 1] == 1 || board[i + 1][c + 1] == 3) {
                    int[] coords = new int[2];
                    coords[0] = i + 2;
                    coords[1] = c + 2;
                    if (!temp.containsKey(index)) {
                        ArrayList<int[]> addCoords = new ArrayList<>();
                        addCoords.add(coords);
                        temp.put(index, addCoords);
                    } else {
                        temp.get(index).add(coords);
                    }
                }
            }
            if (board[i][c] == 4) {
                if (i - 2 > -1 && c + 2 < 8 && board[i - 2][c + 2] == 0) { //capture right black king
                    if (board[i - 1][c + 1] == 1 || board[i - 1][c + 1] == 3) {
                        int[] coords = new int[2];
                        coords[0] = i - 2;
                        coords[1] = c + 2;
                        if (!temp.containsKey(index)) {
                            ArrayList<int[]> addCoords = new ArrayList<>();
                            addCoords.add(coords);
                            temp.put(index, addCoords);
                        } else {
                            temp.get(index).add(coords);
                        }
                    }
                }

                if (i - 2 > -1 && c - 2 > -1 && board[i - 2][c - 2] == 0) { //capture left black king
                    if (board[i - 1][c - 1] == 1 || board[i - 1][c - 1] == 3) {
                        int[] coords = new int[2];
                        coords[0] = i - 2;
                        coords[1] = c - 2;
                        if (!temp.containsKey(index)) {
                            ArrayList<int[]> addCoords = new ArrayList<>();
                            addCoords.add(coords);
                            temp.put(index, addCoords);
                        } else {
                            temp.get(index).add(coords);
                        }
                    }
                }
            }
        }else{
            if (board[i][c] == 1 | board[i][c]==3) {
                if (i - 2 > -1 && c + 2 < 8 && board[i-2][c+2]==0) { //capture right
                    if (board[i - 1][c + 1] == 2 || board[i - 1][c + 1] == 4) {
                        int[] coords = new int[2];
                        coords[0]=i-2; coords[1]=c+2;
                        if(!temp.containsKey(index)){
                            ArrayList<int[]> addCoords = new ArrayList<>();
                            addCoords.add(coords);
                            temp.put(index, addCoords);
                        }else{
                            temp.get(index).add(coords);
                        }
                    }
                }
                if (i - 2 > -1 && c - 2 > -1 && board[i-2][c-2]==0) { //capture left
                    if (board[i - 1][c - 1] == 2 || board[i - 1][c - 1] == 4) {
                        int[] coords = new int[2];
                        coords[0]=i-2; coords[1]=c-2;
                        if(!temp.containsKey(index)){
                            ArrayList<int[]> addCoords = new ArrayList<>();
                            addCoords.add(coords);
                            temp.put(index, addCoords);
                        }else{
                            temp.get(index).add(coords);
                        }
                    }
                }
                if(board[i][c]==3){
                    if (i + 2 < 8 && c - 2 > -1 && board[i+2][c-2]==0) { //capture left red king
                        if (board[i + 1][c - 1] == 2 || board[i + 1][c - 1] == 4) {
                            int[] coords = new int[2];
                            coords[0]=i+2; coords[1]=c-2;
                            if(!temp.containsKey(index)){
                                ArrayList<int[]> addCoords = new ArrayList<>();
                                addCoords.add(coords);
                                temp.put(index, addCoords);
                            }else{
                                temp.get(index).add(coords);
                            }
                        }
                    }
                    if (i + 2 < 8 && c + 2 < 8 && board[i+2][c+2]==0) { //capture right red king
                        if (board[i + 1][c + 1] == 2 || board[i + 1][c + 1] == 4) {
                            int[] coords = new int[2];
                            coords[0]=i+2; coords[1]=c+2;
                            if(!temp.containsKey(index)){
                                ArrayList<int[]> addCoords = new ArrayList<>();
                                addCoords.add(coords);
                                temp.put(index, addCoords);
                            }else{
                                temp.get(index).add(coords);
                            }
                        }
                    }
                }
            }
        }
        if(temp.size()>0){
            node.captureAvailable = true;
        }else{
            node.captureAvailable = false;
        }
                return temp;
    }
    HashMap<Integer,ArrayList<int[]>> piecesThatCanCapture(int playerTurn, short[][] board){
        HashMap<Integer, ArrayList<int[]>> temp = new HashMap<>();
        boolean inc = true;
        if(playerTurn==2) { //if black's turn, what pieces can it capture
            for (int i = 0; i < 8; i++) {
                for (int c = inc?1:0; c < 8; c+=2) {
                    if(board[i][c]==0) continue;
                    int index = i*10+c;
                    if (board[i][c] == 2 || board[i][c] == 4) {
                        if (i + 2 < 8 && c - 2 > -1 && board[i+2][c-2]==0) { //capture left
                            if (board[i + 1][c - 1] == 1 || board[i + 1][c - 1] == 3) {
                                int[] coords = new int[2];
                                coords[0]=i+2; coords[1]=c-2;
                                if(!temp.containsKey(index)){
                                    ArrayList<int[]> addCoords = new ArrayList<>();
                                    addCoords.add(coords);
                                    temp.put(index, addCoords);
                                }else{
                                    temp.get(index).add(coords);
                                }
                            }
                        }
                        if (i + 2 < 8 && c + 2 < 8 && board[i+2][c+2]==0) { //capture right
                            if (board[i + 1][c + 1] == 1 || board[i + 1][c + 1] == 3) {
                                int[] coords = new int[2];
                                coords[0]=i+2; coords[1]=c+2;
                                if(!temp.containsKey(index)){
                                    ArrayList<int[]> addCoords = new ArrayList<>();
                                    addCoords.add(coords);
                                    temp.put(index, addCoords);
                                }else{
                                    temp.get(index).add(coords);
                                }
                            }
                        }
                        if(board[i][c]==4){
                            if (i - 2 > -1 && c + 2 < 8 && board[i-2][c+2]==0) { //capture right black king
                                if (board[i - 1][c + 1] == 1 || board[i - 1][c + 1] == 3) {
                                    int[] coords = new int[2];
                                    coords[0]=i-2; coords[1]=c+2;
                                    if(!temp.containsKey(index)){
                                        ArrayList<int[]> addCoords = new ArrayList<>();
                                        addCoords.add(coords);
                                        temp.put(index, addCoords);
                                    }else{
                                        temp.get(index).add(coords);
                                    }
                                }
                            }
                            if (i - 2 > -1 && c - 2 > -1 && board[i-2][c-2]==0) { //capture left black king
                                if (board[i - 1][c - 1] == 1 || board[i - 1][c - 1] == 3) {
                                    int[] coords = new int[2];
                                    coords[0]=i-2; coords[1]=c-2;
                                    if(!temp.containsKey(index)){
                                        ArrayList<int[]> addCoords = new ArrayList<>();
                                        addCoords.add(coords);
                                        temp.put(index, addCoords);
                                    }else{
                                        temp.get(index).add(coords);
                                    }
                                }
                            }
                        }
                    }
                }
                inc = !inc;
            }
        }else{
            for (int i = 0; i < 8; i++) { //if red's turn, what pieces can it capture
                for (int c = inc?1:0; c < 8; c+=2) {
                    if(board[i][c]==0) continue;
                    int index = i*10+c;
                    if (board[i][c] == 1 | board[i][c]==3) {
                        if (i - 2 > -1 && c + 2 < 8 && board[i-2][c+2]==0) { //capture right
                            if (board[i - 1][c + 1] == 2 || board[i - 1][c + 1] == 4) {
                                int[] coords = new int[2];
                                coords[0]=i-2; coords[1]=c+2;
                                if(!temp.containsKey(index)){
                                    ArrayList<int[]> addCoords = new ArrayList<>();
                                    addCoords.add(coords);
                                    temp.put(index, addCoords);
                                }else{
                                    temp.get(index).add(coords);
                                }
                            }
                        }
                        if (i - 2 > -1 && c - 2 > -1 && board[i-2][c-2]==0) { //capture left
                            if (board[i - 1][c - 1] == 2 || board[i - 1][c - 1] == 4) {
                                int[] coords = new int[2];
                                coords[0]=i-2; coords[1]=c-2;
                                if(!temp.containsKey(index)){
                                    ArrayList<int[]> addCoords = new ArrayList<>();
                                    addCoords.add(coords);
                                    temp.put(index, addCoords);
                                }else{
                                    temp.get(index).add(coords);
                                }
                            }
                        }
                        if(board[i][c]==3){
                            if (i + 2 < 8 && c - 2 > -1 && board[i+2][c-2]==0) { //capture left red king
                                if (board[i + 1][c - 1] == 2 || board[i + 1][c - 1] == 4) {
                                    int[] coords = new int[2];
                                    coords[0]=i+2; coords[1]=c-2;
                                    if(!temp.containsKey(index)){
                                        ArrayList<int[]> addCoords = new ArrayList<>();
                                        addCoords.add(coords);
                                        temp.put(index, addCoords);
                                    }else{
                                        temp.get(index).add(coords);
                                    }
                                }
                            }
                            if (i + 2 < 8 && c + 2 < 8 && board[i+2][c+2]==0) { //capture right red king
                                if (board[i + 1][c + 1] == 2 || board[i + 1][c + 1] == 4) {
                                    int[] coords = new int[2];
                                    coords[0]=i+2; coords[1]=c+2;
                                    if(!temp.containsKey(index)){
                                        ArrayList<int[]> addCoords = new ArrayList<>();
                                        addCoords.add(coords);
                                        temp.put(index, addCoords);
                                    }else{
                                        temp.get(index).add(coords);
                                    }
                                }
                            }
                        }
                    }
                }
                inc = !inc;
            }
        }
        return temp;
    }


    public void start() throws IOException {
        Player player = new Player();
        Tests testing = new Tests();
        Node node = new Node();
        node.board = testing.testdfs1();
        node.redpiececount = 4;
        node.blackpiececount = 4;
        node.redKingcount=0;
        node.blackKingcount=0;
        node.justcaptured=false;
        node.playerTurn=1;
        node.drawMoves=0;
        node.captureAvailable=false;
        tt tt = new tt();
        tt.zobristFillArrays();
        Node puzzlesub1node = createNode(testing.puzzleTest1sub1(),8,6,0,false,1,0,0,false,0);
        Node puzzle1node = createNode(testing.puzzleTest1(),8,7,0,false,1,0,0,false,0);
        Node puzzle2node = createNode(testing.puzzleTest2(),4,4,0,false,1,1,2,false,0);
        Node testcoords = createNode(testing.testcoords(),1,1,0,false,1,1,1,false,0);
        Node testcoordsxor1 = createNode(testing.testxor3(),4,3,0,true,2,1,2,false,0);
        Node testcoordsxor2 = createNode(testing.testxor3(),4,3,0,false,1,1,2,false,0);
        long dd = tt.initHash(testcoordsxor1,0);
        long xx = tt.initHash(testcoordsxor2,0);
        Node puzzle2nodesimple = createNode(testing.puzzleTest2simple(),2,2,0,false,1,2,2,false,0);
        Node puzzleKings = createNode(testing.puzzleKings(),1,1,0,false,1,1,1,false,0);
        Node onewaytowin = createNode(testing.onewaytowin(),1,1,0,false,1,1,1,false,0);
        //        long testx = tt.initHash(puzzleKings,0);
//        nodeMove(puzzleKings,1,4,5,5,6);
//        long testA = tt.createHash(puzzleKings,0,testx,4,5,5,6,-1,-1,0);
//        nodeMove(puzzleKings,2,1,6,0,7);
//        long testB = tt.createHash(puzzleKings,0,testA,1,6,0,7,-1,-1,0);
//        nodeMove(puzzleKings,1,5,6,4,5);
//        long testC = tt.createHash(puzzleKings,0,testB,5,6,4,5,-1,-1,0);
//        nodeMove(puzzleKings,2,0,7,1,6);
//        long testD = tt.createHash(puzzleKings,0,testC,0,7,1,6,-1,-1,0);
//        nodeMove(puzzleKings,1,4,5,5,6);
//        long testE = tt.createHash(puzzleKings,0,testD,4,5,5,6,-1,-1,0);

        long start = System.nanoTime()/1000000000;
//        LinkedHashMap<Integer,Float> testmap = player.shallowSearch(node);
//        testmap.entrySet().stream().forEach(System.out::println);
        //HashMap<Integer,Float> scores = player.iterateThroughMoves(puzzle2node);
        //HashMap<Integer,Float> scores = player.iterativeDeepening(puzzle1node,-1,-1);
        //HashMap<Integer,Float> scores1 = player.iterativeDeepening(puzzleKings,-1,-1);
        HashMap<Integer,Float> scores1 = player.iterativeDeepening(onewaytowin,-1,-1);
        //HashMap<Integer,Float> scores = player.iterateThroughMovesshallow(node);
        long end = System.nanoTime()/1000000000;
        System.out.println(player.numberOfNodes);
        System.out.println("pruned " + player.nodesPruned+ " nodes");
        System.out.println("draw positions reached: " + player.drawPositionsReached);
        System.out.println(end-start + " seconds");
    }
}
