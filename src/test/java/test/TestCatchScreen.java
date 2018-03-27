package test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by hck on 2018/3/27.
 */
public class TestCatchScreen {
    public static void main(String[] args) throws AWTException {
        Robot robot = new Robot();
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        BufferedImage image = robot.createScreenCapture(new Rectangle(0,0,screen.width,screen.height));
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            ImageIO.write(image,"jpg",out);
            System.out.println("JPG size:"+out.toByteArray().length);

            out.reset();
            ImageIO.write(image,"png",out);
            System.out.println("PNG size:"+out.toByteArray().length);

            out.reset();
            ImageIO.write(image,"bmp",out);
            System.out.println("BMP size:"+out.toByteArray().length);

            out.reset();
            ImageIO.write(image,"gif",out);
            System.out.println("GIF size:"+out.toByteArray().length);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
