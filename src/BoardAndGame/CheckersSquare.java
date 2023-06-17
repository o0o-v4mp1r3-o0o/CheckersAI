package BoardAndGame;

import javax.swing.*;
import java.awt.*;

public class CheckersSquare extends JPanel{
    Font font     = new Font("DejaVu Sans", Font.PLAIN, 65);
    Color bgLight = new Color(222, 184, 135);
    Color bgDark  = new Color(139, 69, 19);
    int identifier;
    int xcoord, ycoord;
    CheckerPiece piece;
    void set(int idx, int row)
    {
        setFont(font);
        setOpaque(true);
        setBackground((idx+row)%2 == 0 ? bgDark : bgLight);

    }



}
