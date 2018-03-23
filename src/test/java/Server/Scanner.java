package Server;

import org.apache.log4j.Logger;
import sun.awt.image.BufferedImageDevice;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by hck on 2018/3/23.
 */
public class Scanner implements Runnable{
    private static Robot robot;
    private static Dimension screen;
    private static boolean flag = false;
    private static Scanner scanner;
    private static Logger logger = Logger.getLogger(Scanner.class);
    private static int count = 0;
    static{
        try {
            robot = new Robot();
            screen = Toolkit.getDefaultToolkit().getScreenSize();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置为单例模式
     */
    private Scanner(){}

    public static Scanner getInstance(){
        return scanner == null ? new Scanner():scanner;
    }

    /**
     * 开始采集
     */
    public static void startCatchScreen(){
        flag = true;
        count = 0;
        logger.info("开始采集图片");
    }

    /**
     * 停止采集
     */
    public static void stopCatchScreen(){
        flag = false;
        logger.info("停止采集图片,此次一共采集 " +count+" 张图片");
    }

    @Override
    public void run() {
        while(true){
            if(!flag){
                try {
                    Thread.sleep(100);
                    continue;
                } catch (InterruptedException e) {
                }
            }else {
                catchScreen();
                count++;
            }
        }
    }

    /**
     *  采集图像
     */
    private static void catchScreen() {
        BufferedImage image = robot.createScreenCapture(new Rectangle(0,0,screen.width,screen.height));
        BufferQueue.add(image);
    }


}
