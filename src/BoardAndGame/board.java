package BoardAndGame;

import AI.Game;
import AI.Node;
import AI.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.*;

public class board extends JPanel {

    CheckersSquare[][] squares = new CheckersSquare[8][8];
    public boolean playersTurn = false;
    boolean captureRequired = false;
    public boolean isPlayerRed = false;
    Point pieceSelected;
    Set<Integer> CheckIfLegal = new HashSet();
    Set<Integer> validPieces = new HashSet();
    ArrayList<CheckersSquare> highlightedSquares = new ArrayList<>();
    Set<CheckersSquare> redPieces = new HashSet<>();
    Set<CheckersSquare> blackPieces = new HashSet<>();
    int redKings = 0;
    int blackKings = 0;
    int drawMoves = 0;
    int threadcounter = 0;
    int numMoves = 0;
    Game game = new Game();
    public Player player = new Player();
    testBoards testboards = new testBoards();
    Menu menu;

    public board(Menu menu) {
        this.menu = menu;
    }

    public void display() throws IOException {
        //setTitle("Checkers");

        //setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Container contentPane = getContentPane();
        GridLayout gridLayout = new GridLayout(8, 8);

        //contentPane.setLayout(gridLayout);
        setLayout(gridLayout);

        for (int i = 0; i < 8; i++) {
            for (int row = 0; row < 8; row++) {
                CheckersSquare insert = new CheckersSquare();
                insert.set(i,row);
                CheckerPiece emptySquare = new CheckerPiece(0);
                squares[i][row] = insert;
                squares[i][row].piece = emptySquare;
                //contentPane.add(squares[i][row]);
                add(squares[i][row]);
                int finalI = i;
                int finalR = row;
                squares[i][row].addMouseListener(new MouseAdapter() {
                    public void mousePressed(MouseEvent event) {
                        System.out.println(squares[finalI][finalR].identifier);
                        try {
                            PossibleMoves(finalI, finalR);
                            printNboard(constructNboard());
                            System.out.println("black " + blackPieces.size());
                            System.out.println("red "+redPieces.size());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                squares[i][row].identifier = i * 8 + row;
                squares[i][row].xcoord=i;
                squares[i][row].ycoord=row;
            }
        }

        initBoard();
//        testboards.initboardcantmove(redPieces,blackPieces,squares,this);
//        if(!isPlayerRed) machineMove(-1,-1);
        //testboards.initboard3(redPieces,blackPieces,squares,this);
        //testboards.initboard4(redPieces,blackPieces,squares,this);
        //checkForCapture();
        //printNboard(constructNboard());
        //machineMove(-1,-1);
        //setSize(600, 600);
        //setLocationRelativeTo(null);
//        java.awt.EventQueue.invokeLater(() -> {
//            setVisible(true);
//        });

    }

    void PossibleMoves(int finalI, int finalR) throws IOException {
        Thread machineThreadedMove = new Thread(new Runnable() {
            public void run() {
                try {
                    machineMove(-1,-1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        if(playersTurn && !machineThreadedMove.isAlive()) {
            if(CheckIfLegal.contains(squares[finalI][finalR].identifier)){
                //checkForCapture();
                movePiece(finalI,finalR);
                if(!checkWin()){
                    if(!playersTurn){
                        machineThreadedMove.start();
                    }
                }

            }else {
                if ((pieceSelected != null) && pieceSelected.x == finalI && pieceSelected.y == finalR) { //unhigh if click same sq
                    unhighlightPrevSq();
                }
                else if(squares[finalI][finalR].piece.canPlayerMoveThis) {
                    if (captureRequired) {
                        if (validPieces.contains(squares[finalI][finalR].identifier)) {
                            highlightSq(finalI, finalR);
                        } else {
                            unhighlightPrevSq();
                        }
                    } else if (canMove(finalI, finalR)) {
                        if (squares[finalI][finalR].piece.id != 0) {
                            highlightSq(finalI, finalR);
                        }
                    }
                }else if (pieceSelected != null) {
                    unhighlightPrevSq();
                }
            }
        }
    }

    void unhighlightPrevSq(){
        if(pieceSelected!=null) {
            squares[pieceSelected.x][pieceSelected.y].set(pieceSelected.x, pieceSelected.y);

                for (int i = 0; i < highlightedSquares.size(); i++) {
                    CheckersSquare ss = highlightedSquares.get(i);
                    highlightedSquares.get(i).set(ss.xcoord, ss.ycoord);
                }
                highlightedSquares.clear();
                pieceSelected = null;
                CheckIfLegal.clear();
        }
    }

    void highlightSq(int finalI, int finalR){
        unhighlightPrevSq();
        canMove(finalI,finalR);
        squares[finalI][finalR].setBackground(new Color(255, 255, 0));
        pieceSelected = new Point(finalI,finalR);
        for(int c : CheckIfLegal){
            System.out.println("u"+c+" ");
        }
        for(int i = 0; i < highlightedSquares.size(); i++){
            highlightedSquares.get(i).setBackground(new Color(255, 255, 0));
        }
    }

    void initBoard() throws IOException {
        //init black side

        for(int i = 0; i < 3; i++){
            for(int c = 0; c < 8; c++){
                if(i%2==0) {
                    if (c % 2 == 1) {
                        CheckerPiece insertBlack = new CheckerPiece(2);
                        if(!isPlayerRed){
                            insertBlack.canPlayerMoveThis=true;
                            squares[mirrorNumber(i)][mirrorNumber(c)].add(insertBlack.picLabel);
                            squares[mirrorNumber(i)][mirrorNumber(c)].piece=insertBlack;
                            blackPieces.add(squares[mirrorNumber(i)][mirrorNumber(c)]);
                        }else {
                            squares[i][c].add(insertBlack.picLabel);
                            squares[i][c].piece = insertBlack;
                            blackPieces.add(squares[i][c]);
                        }
                    }
                }else{
                    if (c % 2 == 0) {
                        CheckerPiece insertBlack = new CheckerPiece(2);
                        if(!isPlayerRed){
                            insertBlack.canPlayerMoveThis=true;
                            squares[mirrorNumber(i)][mirrorNumber(c)].add(insertBlack.picLabel);
                            squares[mirrorNumber(i)][mirrorNumber(c)].piece=insertBlack;
                            blackPieces.add(squares[mirrorNumber(i)][mirrorNumber(c)]);
                        }else {
                            squares[i][c].add(insertBlack.picLabel);
                            squares[i][c].piece = insertBlack;
                            blackPieces.add(squares[i][c]);
                        }
                    }
                }
            }
        }

        //init red side
        for(int i = 5; i < 8; i++){
            for(int c = 0; c < 8; c++){
                if(i%2==1) {
                    if (c % 2 == 0) {
                        CheckerPiece insertRed = new CheckerPiece(1);
                        if(isPlayerRed){
                            insertRed.canPlayerMoveThis=true;
                            squares[i][c].add(insertRed.picLabel);
                            squares[i][c].piece=insertRed;
                            redPieces.add(squares[i][c]);
                        }else{
                            squares[mirrorNumber(i)][mirrorNumber(c)].add(insertRed.picLabel);
                            squares[mirrorNumber(i)][mirrorNumber(c)].piece=insertRed;
                            redPieces.add(squares[mirrorNumber(i)][mirrorNumber(c)]);
                        }

                    }
                }else{
                    if (c % 2 == 1) {
                        CheckerPiece insertRed = new CheckerPiece(1);
                        if(isPlayerRed){
                            insertRed.canPlayerMoveThis=true;
                            squares[i][c].add(insertRed.picLabel);
                            squares[i][c].piece=insertRed;
                            redPieces.add(squares[i][c]);
                        }else{
                            squares[mirrorNumber(i)][mirrorNumber(c)].add(insertRed.picLabel);
                            squares[mirrorNumber(i)][mirrorNumber(c)].piece=insertRed;
                            redPieces.add(squares[mirrorNumber(i)][mirrorNumber(c)]);
                        }
                    }
                }
            }
        }
        if(!isPlayerRed) machineMove(-1,-1);
    }
    
    boolean canMove(int finalI, int finalR){
        boolean moveAvailable = false;
        if(canCapture(finalI,finalR, false)) return true;
//        if (squares[finalI][finalR].piece.id == 2 || squares[finalI][finalR].piece.id == 4) {
//            if (finalI + 1 < 8 && finalR - 1 > -1) { //black piece
//                if (squares[finalI + 1][finalR - 1].piece.id == 0) {
//                    CheckIfLegal.add(squares[finalI + 1][finalR - 1].identifier);
//                    highlightedSquares.add(squares[finalI + 1][finalR - 1]);
//                    moveAvailable = true;
//                }
//            }
//            if (finalI + 1 < 8 && finalR + 1 < 8) {
//                if (squares[finalI + 1][finalR + 1].piece.id == 0) {
//                    CheckIfLegal.add(squares[finalI + 1][finalR + 1].identifier);
//                    highlightedSquares.add(squares[finalI + 1][finalR + 1]);
//                    moveAvailable = true;
//                }
//            }
//        }
//        if (squares[finalI][finalR].piece.id == 4) { //black king moves
//            if (finalI - 1 > -1 && finalR - 1 > -1) {
//                if (squares[finalI - 1][finalR - 1].piece.id == 0) {
//                    CheckIfLegal.add(squares[finalI - 1][finalR - 1].identifier);
//                    highlightedSquares.add(squares[finalI - 1][finalR - 1]);
//                    moveAvailable = true;
//                }
//            }
//            if (finalI - 1 > -1 && finalR + 1 < 8) {
//                if (squares[finalI - 1][finalR + 1].piece.id == 0) {
//                    CheckIfLegal.add(squares[finalI - 1][finalR + 1].identifier);
//                    highlightedSquares.add(squares[finalI - 1][finalR + 1]);
//                    moveAvailable = true;
//                }
//            }
//        }

            //if(squares[finalI][finalR].piece.id==1 || squares[finalI][finalR].piece.id==3){ //red pieces
                if(finalI-1>-1 && finalR+1 < 8){
                    if(squares[finalI-1][finalR+1].piece.id == 0){
                        CheckIfLegal.add(squares[finalI-1][finalR+1].identifier);
                        highlightedSquares.add(squares[finalI-1][finalR+1]);
                        moveAvailable = true;
                    }
                }
                if(finalI-1>-1 && finalR-1>-1){
                    if(squares[finalI-1][finalR-1].piece.id == 0){
                        CheckIfLegal.add(squares[finalI-1][finalR-1].identifier);
                        highlightedSquares.add(squares[finalI-1][finalR-1]);
                        moveAvailable = true;
                    }
                }
            //}
            if(squares[finalI][finalR].piece.id==3 || squares[finalI][finalR].piece.id==4){ //red king moves
                if(finalI+1<8 && finalR-1 > -1){
                    if(squares[finalI+1][finalR-1].piece.id == 0){
                        CheckIfLegal.add(squares[finalI+1][finalR-1].identifier);
                        highlightedSquares.add(squares[finalI+1][finalR-1]);
                        moveAvailable = true;
                    }
                }
                if(finalI+1 < 8 && finalR+1 < 8){
                    if(squares[finalI+1][finalR+1].piece.id == 0){
                        CheckIfLegal.add(squares[finalI+1][finalR+1].identifier);
                        highlightedSquares.add(squares[finalI+1][finalR+1]);
                        moveAvailable = true;
                    }
                }
            }
            return moveAvailable;
    }

    boolean canMachineMove(int finalI, int finalR){
        boolean moveAvailable = false;
        if(canCapture(finalI,finalR, false)) return true;
            if (finalI + 1 < 8 && finalR - 1 > -1) { //black piece
                if (squares[finalI + 1][finalR - 1].piece.id == 0) {
                    moveAvailable = true;
                }
            }
            if (finalI + 1 < 8 && finalR + 1 < 8) {
                if (squares[finalI + 1][finalR + 1].piece.id == 0) {
                    moveAvailable = true;
                }
            }
        if (squares[finalI][finalR].piece.id == 4 || squares[finalI][finalR].piece.id == 4) { //black king moves
            if (finalI - 1 > -1 && finalR - 1 > -1) {
                if (squares[finalI - 1][finalR - 1].piece.id == 0) {
                    moveAvailable = true;
                }
            }
            if (finalI - 1 > -1 && finalR + 1 < 8) {
                if (squares[finalI - 1][finalR + 1].piece.id == 0) {
                    moveAvailable = true;
                }
            }
        }
        return moveAvailable;
    }

    boolean canCapture(int finalI, int finalR, boolean machineMove){
        boolean captureAvailable = false;
        if(!playersTurn){
        //if (squares[finalI][finalR].piece.id == 2 || squares[finalI][finalR].piece.id == 4) {
            if (finalI + 2 < 8 && finalR - 2 > -1 && squares[finalI + 2][finalR - 2].piece.id == 0) { //B capture left
                if (squares[finalI + 1][finalR - 1].piece.canPlayerMoveThis && squares[finalI + 1][finalR - 1].piece.id!=0) {
                    CheckIfLegal.add(squares[finalI + 2][finalR - 2].identifier);
                    highlightedSquares.add(squares[finalI + 2][finalR - 2]);
                    captureAvailable = true;
                }
            }
            if (finalI + 2 < 8 && finalR + 2 < 8 && squares[finalI + 2][finalR + 2].piece.id == 0) { //B capture right
                if (squares[finalI + 1][finalR + 1].piece.canPlayerMoveThis && squares[finalI + 1][finalR + 1].piece.id!=0) {
                    CheckIfLegal.add(squares[finalI + 2][finalR + 2].identifier);
                    highlightedSquares.add(squares[finalI + 2][finalR + 2]);
                    captureAvailable = true;
                }
            }
            if (squares[finalI][finalR].piece.id == 4 || squares[finalI][finalR].piece.id == 3) {
                if (finalI - 2 > -1 && finalR + 2 < 8 && squares[finalI - 2][finalR + 2].piece.id == 0) { //capture right black king
                    if (squares[finalI - 1][finalR + 1].piece.canPlayerMoveThis && squares[finalI - 1][finalR + 1].piece.id!=0) {
                        CheckIfLegal.add(squares[finalI - 2][finalR + 2].identifier);
                        highlightedSquares.add(squares[finalI - 2][finalR + 2]);
                        captureAvailable = true;
                    }
                }

                if (finalI - 2 > -1 && finalR - 2 > -1 && squares[finalI - 2][finalR - 2].piece.id == 0) { //capture left black
                    // king
                    if (squares[finalI - 1][finalR - 1].piece.canPlayerMoveThis && squares[finalI - 1][finalR - 1].piece.id!=0) {
                        CheckIfLegal.add(squares[finalI - 2][finalR - 2].identifier);
                        highlightedSquares.add(squares[finalI - 2][finalR - 2]);
                        captureAvailable = true;
                    }
                }
            }
        //}
        }else{
            //if (squares[finalI][finalR].piece.id == 1 | squares[finalI][finalR].piece.id==3) {
                if (finalI - 2 > -1 && finalR + 2 < 8 && squares[finalI-2][finalR+2].piece.id == 0) { //finalRapture right
                    if (!squares[finalI - 1][finalR + 1].piece.canPlayerMoveThis && squares[finalI - 1][finalR + 1].piece.id!=0) {
                        CheckIfLegal.add(squares[finalI - 2][finalR + 2].identifier);
                        highlightedSquares.add(squares[finalI - 2][finalR + 2]);
                        captureAvailable = true;
                    }
                }
                if (finalI - 2 > -1 && finalR - 2 > -1 && squares[finalI-2][finalR-2].piece.id == 0) { //finalRapture left
                    if (!squares[finalI - 1][finalR - 1].piece.canPlayerMoveThis&& squares[finalI - 1][finalR - 1].piece.id!=0) {
                        CheckIfLegal.add(squares[finalI - 2][finalR - 2].identifier);
                        highlightedSquares.add(squares[finalI - 2][finalR - 2]);
                        captureAvailable = true;
                    }
                }
                if(squares[finalI][finalR].piece.id==3 || squares[finalI][finalR].piece.id==4){
                    if (finalI + 2 < 8 && finalR - 2 > -1 && squares[finalI+2][finalR-2].piece.id == 0) { //finalRapture left red king
                        if (!squares[finalI + 1][finalR - 1].piece.canPlayerMoveThis&& squares[finalI + 1][finalR - 1].piece.id!=0) {
                            CheckIfLegal.add(squares[finalI + 2][finalR - 2].identifier);
                            highlightedSquares.add(squares[finalI + 2][finalR - 2]);
                            captureAvailable = true;
                        }
                    }
                    if (finalI + 2 < 8 && finalR + 2 < 8 && squares[finalI+2][finalR+2].piece.id == 0) { //finalRapture right red king
                        if (!squares[finalI + 1][finalR + 1].piece.canPlayerMoveThis&& squares[finalI + 1][finalR + 1].piece.id!=0) {
                            CheckIfLegal.add(squares[finalI + 2][finalR + 2].identifier);
                            highlightedSquares.add(squares[finalI + 2][finalR + 2]);
                            captureAvailable = true;
                        }
                    }
                }
            //}
        }
        return captureAvailable;
    }

    void checkForCapture() {
        if (isPlayerRed) {
            for (CheckersSquare i : redPieces) {
                if (canCapture(i.xcoord, i.ycoord, false)) {
                    validPieces.add(i.identifier);
                    captureRequired = true;
                }
            }
        } else {
            for (CheckersSquare i : blackPieces) {
                if (canCapture(i.xcoord, i.ycoord, false)) {
                    validPieces.add(i.identifier);
                    captureRequired = true;
                }
            }
            highlightedSquares.clear();
            CheckIfLegal.clear();
        }
    }

    boolean canPlayerMove() {
        int numMoves = 0;
        if(playersTurn) {
            if (isPlayerRed) {
                for (CheckersSquare i : redPieces) {
                    if (canMove(i.xcoord, i.ycoord)) {
                        numMoves++;
                    }
                }
            } else {
                for (CheckersSquare i : blackPieces) {
                    if (canMove(i.xcoord, i.ycoord)) {
                        numMoves++;
                    }
                }
            }
        }else{
            if (!isPlayerRed) {
                for (CheckersSquare i : redPieces) {
                    if (canMachineMove(i.xcoord, i.ycoord)) {
                        numMoves++;
                    }
                }
            } else {
                for (CheckersSquare i : blackPieces) {
                    if (canMachineMove(i.xcoord, i.ycoord)) {
                        numMoves++;
                    }
                }
            }
        }
        highlightedSquares.clear();
        CheckIfLegal.clear();
        return numMoves>0?true:false;
    }

    void movePiece(int finalI, int finalR) throws IOException {
        CheckerPiece storage = squares[pieceSelected.x][pieceSelected.y].piece;
        if(isPlayerRed){
            redPieces.remove(squares[pieceSelected.x][pieceSelected.y]);
        }else{
            blackPieces.remove(squares[pieceSelected.x][pieceSelected.y]);
        }
        squares[pieceSelected.x][pieceSelected.y].remove(squares[pieceSelected.x][pieceSelected.y].piece.picLabel);
        squares[pieceSelected.x][pieceSelected.y].piece=new CheckerPiece(0);
        boolean captured = false;
        if(Math.abs(finalI-pieceSelected.x)==2){
            captured=true;
            int id = squares[(pieceSelected.x+finalI)/2][(pieceSelected.y+finalR)/2].piece.id;
            if(id==1 || id==3){
                redPieces.remove(squares[(pieceSelected.x+finalI)/2][(pieceSelected.y+finalR)/2]);
                if(id==3) redKings--;
            }else{
                blackPieces.remove(squares[(pieceSelected.x+finalI)/2][(pieceSelected.y+finalR)/2]);
                if(id==4) blackKings--;
            }
            squares[(pieceSelected.x+finalI)/2][(pieceSelected.y+finalR)/2].remove(squares[(pieceSelected.x+finalI)/2][(pieceSelected.y+finalR)/2].piece.picLabel);
            squares[(pieceSelected.x+finalI)/2][(pieceSelected.y+finalR)/2].piece=new CheckerPiece(0);
        }
        if(!promotePiece(finalI,squares[finalI][finalR],storage)){
            squares[finalI][finalR].piece = storage;
            squares[finalI][finalR].add(storage.picLabel);
            if(isPlayerRed) redPieces.add(squares[finalI][finalR]);
            else blackPieces.add(squares[finalI][finalR]);
            revalidate();
            repaint();
        }
        unhighlightPrevSq();
        validPieces.clear();
        if(canCapture(finalI,finalR, false) && captured){
            playersTurn = true;
            captureRequired = true;
            validPieces.add(squares[finalI][finalR].identifier);
        }else{
            playersTurn = false;
            captureRequired = false;
        }
        squares[finalI][finalR].piece.canPlayerMoveThis=true;
    }

    boolean promotePiece(int lastRow, CheckersSquare justMoved, CheckerPiece checkerNum) throws IOException {
        if(lastRow==0 || lastRow==7){
            if(checkerNum.id==1){
                justMoved.piece = new CheckerPiece(3);
                justMoved.add(justMoved.piece.picLabel);
                redPieces.add(justMoved);
                redKings++;
                revalidate();
                repaint();
                return true;
            }else if(checkerNum.id==2){
                justMoved.piece = new CheckerPiece(4);
                justMoved.add(justMoved.piece.picLabel);
                blackPieces.add(justMoved);
                blackKings++;
                revalidate();
                repaint();
                return true;
            }
        }
        return false;
    }

    short[][] constructNboard(){
        short[][] nodeboard = new short[8][8];
//        if(!isPlayerRed) {
//            for (int i = 0; i < 8; i++) {
//                for (int c = 0; c < 8; c++) {
//                    nodeboard[i][c] = (short)squares[i][c].piece.id;
//                }
//            }
//        }else{
            int horizI=0, vertI = 0;
            int horizJ=7,vertJ = 7;
            boolean breaker = true;
            if(isPlayerRed) {
                while (breaker) {
                    if (vertI > 7) {
                        breaker = false;
                    } else {
                        if (squares[vertI][horizI].piece.id == 1) nodeboard[vertJ][horizJ] = 2;
                        if (squares[vertI][horizI].piece.id == 2) nodeboard[vertJ][horizJ] = 1;
                        if (squares[vertI][horizI].piece.id == 3) nodeboard[vertJ][horizJ] = 4;
                        if (squares[vertI][horizI].piece.id == 4) nodeboard[vertJ][horizJ] = 3;
                    }
                    if (horizI == 7) {
                        vertI++;
                        horizI = -1;
                    }
                    if (horizJ == 0) {
                        vertJ--;
                        horizJ = 8;
                    }
                    horizI++;
                    horizJ--;
                }
                //}
            }else{
                while (breaker) {
                    if (vertI > 7) {
                        breaker = false;
                    } else {
                        nodeboard[vertJ][horizJ] = (short)squares[vertI][horizI].piece.id;
                    }
                    if (horizI == 7) {
                        vertI++;
                        horizI = -1;
                    }
                    if (horizJ == 0) {
                        vertJ--;
                        horizJ = 8;
                    }
                    horizI++;
                    horizJ--;
                }
            }
        return nodeboard;
    }

    void printNboard(short[][] board){
        for(int i = 0; i < 8; i++){
            for(int c = 0; c < 8; c++){
                System.out.print(board[i][c] + " ");
            }
            System.out.println("");
        }
    }

    int machineMoveCoordinates(int finalI, int finalR) throws IOException {
        HashMap<Integer,Float> moves = new HashMap<>();

        short[][] Nboard = constructNboard();
        int storedepth = Player.maxdepth;
        if(numMoves<2){
            Player.maxdepth = 2;
        }
        Node currentNode = game.createNode(Nboard, isPlayerRed?redPieces.size():blackPieces.size(), isPlayerRed?
                        blackPieces.size():redPieces.size(), 0,
                false,1,
                isPlayerRed?redKings:blackKings,
                isPlayerRed?blackKings:redKings,false,0);

        moves = player.iterativeDeepening(currentNode, finalI, finalR);
        Player.maxdepth = storedepth; numMoves++;
        int coords = 0;
        float coordsValue = -Float.MAX_VALUE;
        ArrayList<Integer> chooseRandom = new ArrayList<>();
        for(Map.Entry<Integer,Float> ss : moves.entrySet()){
            if(ss.getValue() >= coordsValue){
                coords = ss.getKey();
                coordsValue = ss.getValue();
            }
            System.out.println(ss.getKey() + " " + ss.getValue());
        }
        for(Map.Entry<Integer,Float> ss : moves.entrySet()){
            if(ss.getValue() == coordsValue){
                chooseRandom.add(ss.getKey());
            }
        }
        //chooseRandom.get((int)(Math.random()*chooseRandom.size()))
        return chooseRandom.get((int)(Math.random()*chooseRandom.size()));
    }

    int mirrorNumber(int origin){
        if(origin==0) origin=7;
        else if(origin==1) origin=6;
        else if(origin==2) origin=5;
        else if(origin==3) origin=4;
        else if(origin==4) origin=3;
        else if(origin==5) origin=2;
        else if(origin==6) origin=1;
        else if(origin==7) origin=0;
        return origin;
    }

    void machineMove(int DfinalI, int DfinalR) throws IOException {

        int coords = machineMoveCoordinates(DfinalI,DfinalR);
        int x = coords / 1000;
        int y = (coords % 1000) / 100;
        int finalI = (coords % 100) / 10;
        int finalR = (coords % 10);
        //if(isPlayerRed){
            x=mirrorNumber(x);
            y=mirrorNumber(y);
            finalI=mirrorNumber(finalI);
            finalR=mirrorNumber(finalR);
        //}
        pieceSelected = new Point(x,y);
        CheckerPiece storage = squares[pieceSelected.x][pieceSelected.y].piece;
        if(!isPlayerRed){
            redPieces.remove(squares[pieceSelected.x][pieceSelected.y]);
        }else{
            blackPieces.remove(squares[pieceSelected.x][pieceSelected.y]);
        }
        squares[pieceSelected.x][pieceSelected.y].remove(squares[pieceSelected.x][pieceSelected.y].piece.picLabel);
        squares[pieceSelected.x][pieceSelected.y].piece=new CheckerPiece(0);
        boolean captured = false;
        if(Math.abs(finalI-pieceSelected.x)==2){
            captured = true;
            int id = squares[(pieceSelected.x+finalI)/2][(pieceSelected.y+finalR)/2].piece.id;
            if(id==1 || id==3){
                redPieces.remove(squares[(pieceSelected.x+finalI)/2][(pieceSelected.y+finalR)/2]);
                if(id==3) redKings--;
            }else{
                blackPieces.remove(squares[(pieceSelected.x+finalI)/2][(pieceSelected.y+finalR)/2]);
                if(id==4) blackKings--;
            }
            squares[(pieceSelected.x+finalI)/2][(pieceSelected.y+finalR)/2].remove(squares[(pieceSelected.x+finalI)/2][(pieceSelected.y+finalR)/2].piece.picLabel);
            squares[(pieceSelected.x+finalI)/2][(pieceSelected.y+finalR)/2].piece=new CheckerPiece(0);
        }
        if(!promotePiece(finalI,squares[finalI][finalR],storage)){
            squares[finalI][finalR].piece = storage;
            squares[finalI][finalR].add(storage.picLabel);
            if(!isPlayerRed) redPieces.add(squares[finalI][finalR]);
            else blackPieces.add(squares[finalI][finalR]);
            revalidate();
            repaint();
        }
        if(canCapture(finalI,finalR, true) && captured) {
            CheckIfLegal.clear();
            validPieces.clear();
            machineMove(mirrorNumber(finalI),mirrorNumber(finalR));
        }
        CheckIfLegal.clear();
        validPieces.clear();
        playersTurn=true;
        checkForCapture();
        checkWin();
        }

        boolean checkWin() throws IOException {
            String[] options = { "New Game", "Menu"};
            boolean playerturn = isPlayerRed?true:false;
            boolean playerRed = isPlayerRed;
            if(redPieces.size()==0){
                int ss = JOptionPane.showOptionDialog(null,"Black Wins","Select:",0,3,null,options,options[0]);
                if(ss==0){
                    menu.newGame(playerRed,playerturn);
                    return true;
                }else{
                    menu.backToMenu();
                    return true;
                }
            }else if(blackPieces.size()==0){
                int ss = JOptionPane.showOptionDialog(null,"Red Wins","Select:",0,3,null,options,options[0]);
                if(ss==0){
                    menu.newGame(playerRed,playerturn);
                    return true;
                }else{
                    menu.backToMenu();
                    return true;
                }
            }else if(!canPlayerMove()){
                String message;
                if(playersTurn) message="AI Wins";
                else message="You Win";
                int ss = JOptionPane.showOptionDialog(null,"No Moves Available "+message,"Select:",0,3,null,options,
                        options[0]);
                if(ss==0){
                    menu.newGame(playerRed,playerturn);
                    return true;
                }else{
                    menu.backToMenu();
                    return true;
                }
            }
            return false;
        }

}
