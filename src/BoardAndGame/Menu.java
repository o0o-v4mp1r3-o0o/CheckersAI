package BoardAndGame;

import AI.Player;
import BoardAndGame.board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;

public class Menu extends JFrame {
    public board board = new board(this);
    public static JPanel chooseDifficulty = new JPanel();
    public static JPanel chooseColor = new JPanel();
    URL redurl = Menu.class.getResource("/Images/redPiece.png");
    URL blackurl = Menu.class.getResource("/Images/blackPiece.png");
    URL easyurl = Menu.class.getResource("/Images/easy.png");
    URL mediumurl = Menu.class.getResource("/Images/medium.png");
    URL hardurl = Menu.class.getResource("/Images/hard.png");

    ImageIcon red = new ImageIcon(redurl);
    ImageIcon black = new ImageIcon(blackurl);
    ImageIcon easy = new ImageIcon(easyurl);
    ImageIcon medium = new ImageIcon(mediumurl);
    ImageIcon hard = new ImageIcon(hardurl);
    public void display() throws IOException {
        setTitle("Checkers");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        GridLayout gridLayout = new GridLayout(3, 1);
        GridLayout gridLayout2 = new GridLayout(2,1);
        JButton redButton = new JButton(red);
        JButton blackButton = new JButton(black);
        JButton easyButton = new JButton(easy);
        JButton mediumButton = new JButton(medium);
        JButton hardButton = new JButton(hard);
        redButton.setText("Red");
        blackButton.setText("Black");
        redButton.setVerticalTextPosition(AbstractButton.NORTH);
        redButton.setHorizontalTextPosition(AbstractButton.CENTER);
        blackButton.setVerticalTextPosition(AbstractButton.NORTH);
        blackButton.setHorizontalTextPosition(AbstractButton.CENTER);
        easyButton.setText("Easy");
        mediumButton.setText("Medium");
        hardButton.setText("Hard");
        chooseDifficulty.setLayout(gridLayout);
        chooseColor.setLayout(gridLayout2);
        //contentPane.setLayout(gridLayout);
        easyButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("hi");
                Player.maxdepth = 1;
                remove(chooseDifficulty);
                add(chooseColor);
                repaint(); revalidate();
                setVisible(true);
            }
        });
        mediumButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("hi");
                Player.maxdepth=3;
                remove(chooseDifficulty);
                add(chooseColor);
                repaint(); revalidate();
                setVisible(true);
            }
        });
        hardButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("hi");
                Player.maxdepth=10;
                remove(chooseDifficulty);
                add(chooseColor);
                repaint(); revalidate();
                setVisible(true);
            }
        });
        redButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("hi");
                board.playersTurn=true;
                board.isPlayerRed=true;
                remove(chooseColor);
                add(board);
                repaint(); revalidate();
                try {
                    board.display();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                setVisible(true);
            }
        });
        blackButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("hi");
                board.playersTurn=false;
                board.isPlayerRed=false;
                remove(chooseColor);
                add(board);
                repaint(); revalidate();
                try {
                    board.display();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                setVisible(true);
            }
        });
        chooseDifficulty.add(easyButton);
        chooseDifficulty.add(mediumButton);
        chooseDifficulty.add(hardButton);
        chooseColor.add(redButton);
        chooseColor.add(blackButton);
        add(chooseDifficulty);
        setSize(600, 600);
        setLocationRelativeTo(null);
        java.awt.EventQueue.invokeLater(() -> {
            setVisible(true);
        });
    }
    public void backToMenu(){
        remove(board);
        repaint(); revalidate();
        add(chooseDifficulty);
        board = new board(this);
        repaint(); revalidate();
    }

    public void newGame(boolean isplayerred, boolean playerturn) throws IOException {
        remove(board);
        //repaint(); revalidate();
        board = new board(this);
        board.isPlayerRed = isplayerred;
        board.playersTurn = playerturn;
        add(board);
        board.display();
        repaint(); revalidate();

    }
}
