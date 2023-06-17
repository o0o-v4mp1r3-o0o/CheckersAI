import AI.Game;
import BoardAndGame.Menu;
import BoardAndGame.board;

import java.io.IOException;

public class Main {

    public void initMenu() throws IOException {
        Menu menu = new Menu();
        menu.display();
    }
    public void initGame() {
        Game game = new Game();
        //game.start();
    }
    public void initBoard() throws IOException {
        board board = new board(new Menu());
        board.display();
    }
    public static void main(String[] args) throws IOException {
        Main main = new Main();
        //main.initGame();
//        java.awt.EventQueue.invokeLater(() -> {
//            try {
//                main.initBoard();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
        main.initMenu();

    }
}
