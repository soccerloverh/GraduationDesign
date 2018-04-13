package test;

import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by hck on 2018/4/12.
 */
public class TestCompressionImage {
    public static void main(String[] args) throws AWTException {

        Robot robot = new Robot();
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BufferedImage image = robot.createScreenCapture(new Rectangle(0,0,dimension.width,dimension.height));
        try {
            BufferedImage image1 = Thumbnails.of(image).scale(1f).outputQuality(0.5f).asBufferedImage();
            ImageIO.write(image,"jpg",out);
            ImageIO.write(image,"jpg",new File("D:\\GraduationDesign\\src\\test\\resource\\src.jpg"));
            System.out.println("Src size :"+out.toByteArray().length);
            out.reset();
            ImageIO.write(image1,"jpg",out);
            ImageIO.write(image1,"jpg",new File("D:\\GraduationDesign\\src\\test\\resource\\des.jpg"));
            System.out.println("Des size :"+out.toByteArray().length);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
