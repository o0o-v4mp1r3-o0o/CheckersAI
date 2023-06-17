package BoardAndGame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class CheckerPiece extends Piece{
    String checkersImage;
    BufferedImage myPicture;
    JLabel picLabel;
    public JLabel redKing;
    URL redurl = Menu.class.getResource("/Images/redPiece.png");
    URL blackurl = Menu.class.getResource("/Images/blackPiece.png");
    URL redkingurl = Menu.class.getResource("/Images/redKing.png");
    URL blackkingurl = Menu.class.getResource("/Images/blackKing.png");
    URL wildCard;
    public CheckerPiece(int id) throws IOException {
        this.id=id;
        //loadImages();
        if(id==1){ //red piece
            moves.add(new Point(-1,1));
            moves.add(new Point(-1,-1));
            updateImage(id);
        }else if(id==2){ //black piece
            moves.add(new Point(1,-1));
            moves.add(new Point(1,1));
            updateImage(id);
        }else if(id==3){ //red king
            moves.add(new Point(-1,1));
            moves.add(new Point(-1,-1));
            moves.add(new Point(1,-1));
            moves.add(new Point(1,1));
            updateImage(id);
        }else if(id==4){ //black king
            moves.add(new Point(-1,1));
            moves.add(new Point(-1,-1));
            moves.add(new Point(1,-1));
            moves.add(new Point(1,1));
            updateImage(id);
        }
    }

    void updateImage(int id) throws IOException {
        if(id==1) wildCard=redurl;
        if(id==2) wildCard=blackurl;
        if(id==3) wildCard=redkingurl;
        if(id==4) wildCard=blackkingurl;
        myPicture = ImageIO.read(wildCard);
        Image sizedImage = myPicture.getScaledInstance(65, 65,
                Image.SCALE_SMOOTH);
        picLabel = new JLabel(new ImageIcon(sizedImage));
    }

//    void loadImages() throws IOException {
//        String checkersImage="/Images/redKing.png";
//        Image myPicture = ImageIO.read(new File(checkersImage));
//        Image sizedImage = myPicture.getScaledInstance(65, 65,
//                Image.SCALE_SMOOTH);
//        redKing = new JLabel(new ImageIcon(sizedImage));
//    }

}
