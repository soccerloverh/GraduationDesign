package Server;

import org.apache.log4j.Logger;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by hck on 2018/3/23.
 */
public class Frame {
    private static JFrame frame;
    private static JPanel basePanel;
    private static JButton startButton;
    private static JButton endButton;
    private static MyLabel video;
    private static JTextField info;
    private static Logger logger = Logger.getLogger(Frame.class);
    private static boolean flag = false;

    public Frame() {
    }

    public static void init() throws IOException {
        try {
            UIManager.put("RootPane.setupButtonVisible", false);
            org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
        } catch (Exception e) {
            e.printStackTrace();
        }
        frame = new JFrame("hck");
        frame.setSize(500, 400);                               // 设置窗口大小
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);                                  // 把窗口位置设置到屏幕中心
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);      // 当点击窗口的关闭按钮时退出程序（没有这一句，程序不会退出）

        basePanel = new JPanel();                                           // 设置底层容器
        basePanel.setLayout(new GridBagLayout());                           // 设置GirdBag布局管理器

        video = new MyLabel("video");                                    // 初始化视频显示区域
        video.setImage(ImageIO.read(Frame.class.getClassLoader().getResourceAsStream("Orbit.png")));
        video.repaint();
        basePanel.add(video, new GBC(0, 0, 6, 1).setFill(GBC.BOTH).setIpad(500, 350).setWeight(100, 100));

        startButton = new JButton("Start");                             // 初始化开始按钮
        startButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.green));
        basePanel.add(startButton, new GBC(0, 1, 1, 1).setWeight(0, 0));

        endButton = new JButton("Stop");                                 //  初始化结束按钮
        endButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));
        basePanel.add(endButton, new GBC(1, 1, 1, 1).setWeight(0, 0));

        info = new JTextField();
        info.setEditable(false);                                             // 初始化Info
        basePanel.add(info, new GBC(2, 1, 6, 1).setFill(GBC.HORIZONTAL).setWeight(0, 0));

        initActionListener();                                                // 初始化开始结束按钮的时间监听

        frame.add(basePanel);                                                // 添加Jpanel到Frame
        frame.setVisible(true);                                              // 设置Frame为可见
        startRepaint();                                                      // 开始循环遍历标志位,是否需要开始渲染界面
    }

    /**
     * 循环判断标识位,如果为true 就间隔20毫秒刷新一次界面。否则再100毫秒后再去轮询标志位
     */
    private static void startRepaint() {
        for (; ; ) {
            try {
                if (flag) {
                    video.setImage(BufferQueue.peek());     //  从缓存队列中获取一张图片
                    video.repaint();                        //  重绘Lable
                    Thread.sleep(10);
                } else {
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void initActionListener() {
        //  添加开始按钮监听事件
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.info("开始播放");
                Scanner.startCatchScreen();
                flag = true;
            }
        });
        //  添加结束按钮监听事件
        endButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.info("结束播放");
                Scanner.stopCatchScreen();
                flag = false;
                try {
                    video.setImage(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("Orbit.png")));
                    video.repaint();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }finally {
                    System.gc();
                }
            }
        });
    }



}

