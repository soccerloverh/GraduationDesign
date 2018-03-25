package Server;

import org.apache.log4j.Logger;

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
public class Frame{
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
        frame = new JFrame("hck");
        frame.setSize(500, 400);                               // 设置窗口大小
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);                                  // 把窗口位置设置到屏幕中心
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);      // 当点击窗口的关闭按钮时退出程序（没有这一句，程序不会退出）

        basePanel = new JPanel();                                           // 设置底层容器
        basePanel.setLayout(new GridBagLayout());                           // 设置GirdBag布局管理器

        video = new MyLabel("video");                                    // 初始化视频显示区域
        video.repaint();
        basePanel.add(video,new GBC(0,0,6,1).setFill(GBC.BOTH).setIpad(500,350).setWeight(100,100));

        startButton = new JButton("Start");                             // 初始化开始按钮
        basePanel.add(startButton,new GBC(0,1,1,1).setFill(GBC.CENTER).setIpad(50,20).setWeight(0,0));

        endButton = new JButton("end");                                 //  初始化结束按钮
        basePanel.add(endButton,new GBC(1,1,1,1).setFill(GBC.CENTER).setIpad(50,20).setWeight(0,0));

        info = new JTextField();
        info.setEditable(false);
        basePanel.add(info,new GBC(2,1,6,1).setFill(GBC.HORIZONTAL).setWeight(0,0));

        initActionListener();


        frame.add(basePanel);
        frame.setVisible(true);
        startRepaint();

    }

    private static void startRepaint() {
        for(;;){
            if(flag){
                video.setImage(BufferQueue.peek());
                video.repaint();
            }
            try {
                Thread.sleep(100);
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
                Scanner.startCatchScreen();
                flag = false;
            }
        });
    }


    private static class MyLabel extends JLabel {
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

    private static class GBC extends GridBagConstraints
    {
        //初始化左上角位置
        public GBC(int gridx, int gridy)
        {
            this.gridx = gridx;
            this.gridy = gridy;
        }

        //初始化左上角位置和所占行数和列数
        public GBC(int gridx, int gridy, int gridwidth, int gridheight)
        {
            this.gridx = gridx;
            this.gridy = gridy;
            this.gridwidth = gridwidth;
            this.gridheight = gridheight;
        }

        //对齐方式
        public GBC setAnchor(int anchor)
        {
            this.anchor = anchor;
            return this;
        }

        //是否拉伸及拉伸方向
        public GBC setFill(int fill)
        {
            this.fill = fill;
            return this;
        }

        //x和y方向上的增量
        public GBC setWeight(double weightx, double weighty)
        {
            this.weightx = weightx;
            this.weighty = weighty;
            return this;
        }

        //外部填充
        public GBC setInsets(int distance)
        {
            this.insets = new Insets(distance, distance, distance, distance);
            return this;
        }

        //外填充
        public GBC setInsets(int top, int left, int bottom, int right)
        {
            this.insets = new Insets(top, left, bottom, right);
            return this;
        }

        //内填充
        public GBC setIpad(int ipadx, int ipady)
        {
            this.ipadx = ipadx;
            this.ipady = ipady;
            return this;
        }
    }
}

