
package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by hck on 2018/3/27.
 */
public class MyLabel extends JLabel {

    private BufferedImage image;

    public MyLabel(String s) {
        super(s);
    }

    public void setImage(BufferedImage img) {
        this.image = img;
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);

    }
}