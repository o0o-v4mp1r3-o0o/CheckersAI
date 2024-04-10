package AI;

public class Tests {
    short[][] testboard(){
        short[][] newboard = new short[8][8];
        newboard[5][5] = 1;
        newboard[2][2] = 2;
        newboard[4][4] = 2;
        return newboard;
    }
    short[][] testkingboard(){
        short[][] newboard = new short[8][8];
        newboard[1][1]=2;
        newboard[1][3] = 2;
        newboard[3][1] = 2;
        newboard[3][3] = 2;
        newboard[2][2] = 3;
        newboard[5][5] = 1;
        return newboard;
    }
    short[][] testdfs(){
        short[][] newboard = new short[8][8];
        newboard[6][7]=1;
        newboard[4][7] = 1;
        newboard[6][5] = 2;
        newboard[4][5] = 2;
        return newboard;
    }

    short[][] testdfs1(){
        short[][] newboard = new short[8][8];
        newboard[7][6]=1;
        newboard[7][4] = 1;
        newboard[7][2] = 1;
        newboard[7][0] = 1;
        newboard[5][2] = 2;
        newboard[5][4] = 2;
        newboard[0][1] = 2;
        newboard[0][3] = 2;
        return newboard;
    }

    short[][] testxor(){
        short[][] newboard = new short[8][8];
        newboard[7][6]=1;
        newboard[6][5] = 2;
        newboard[1][2] = 2;
        return newboard;
    }

    short[][] testxor2(){
        short[][] newboard = new short[8][8];
        newboard[5][4]=1;
        newboard[1][2] = 2;
        return newboard;
    }

    short[][] testxor3(){
        short[][] newboard = new short[8][8];
        newboard[2][5]=3;
        newboard[2][7]=2;
        newboard[3][2]=2;
        newboard[4][3]=3;
        newboard[6][3]=2;
        newboard[7][6]=4;
        newboard[6][7]=1;
        return newboard;
    }

    short[][] testcoords(){
        short[][] newboard = new short[8][8];
        newboard[2][1]=3;
        newboard[1][6] = 4;
        return newboard;
    }

    short[][] puzzleTest1(){
        short[][] newboard = new short[8][8];
        newboard[0][5]=2;
        newboard[1][2] = 2;
        newboard[2][1] = 2;
        newboard[3][0] = 2;
        newboard[3][2] = 2;
        newboard[4][3] = 2;
        newboard[4][7] = 2;
        newboard[6][7] = 2;
        newboard[1][6] = 1;
        newboard[2][7] = 1;
        newboard[5][0] = 1;
        newboard[5][4] = 1;
        newboard[7][2] = 1;
        newboard[7][4] = 1;
        newboard[7][6] = 1;
        return newboard;
    }
    short[][] puzzleTest1sub1(){
        short[][] newboard = new short[8][8];
        newboard[0][5]=2;
        newboard[1][2] = 2;
        newboard[2][1] = 2;
        newboard[5][2] = 2;
        newboard[3][2] = 2;
        newboard[4][3] = 2;
        newboard[4][7] = 2;
        newboard[6][7] = 2;
        newboard[1][6] = 1;
        newboard[2][7] = 1;
        newboard[5][4] = 1;
        newboard[7][2] = 1;
        newboard[7][4] = 1;
        newboard[7][6] = 1;
        return newboard;
    }

    short[][] puzzleTest2(){
        short[][] newboard = new short[8][8];
        newboard[1][0]=2;
        newboard[2][7] = 2;
        newboard[4][5] = 2;
        newboard[7][6] = 4;
        newboard[3][2] = 1;
        newboard[4][3] = 3;
        newboard[2][5] = 3;
        newboard[6][7] = 1;
        return newboard;
    }

    short[][] puzzleTest2simple(){
        short[][] newboard = new short[8][8];
        newboard[4][5] = 2;
        newboard[7][6] = 4;
        newboard[4][3] = 3;
        newboard[6][7] = 1;
        return newboard;
    }

    short[][] puzzleKings(){
        short[][] newboard = new short[8][8];
        newboard[1][6] = 4;
        newboard[4][5] = 3;
        return newboard;
    }

    short[][] onewaytowin(){
        short[][] newboard = new short[8][8];
        newboard[1][6] = 4;
        newboard[6][7] = 3;
        return newboard;
    }
}
