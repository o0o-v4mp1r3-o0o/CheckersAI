package BoardAndGame;

import java.awt.*;
import java.util.ArrayList;

public class Piece {
    public int id;
    public ArrayList<Point> moves = new ArrayList<>();
    boolean canPlayerMoveThis = false;
}
