package Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

/**
 * Created by hck on 2018/3/23.
 */
public class Frame{
    private static JFrame frame;
    private static JPanel basePanel;
    private static JButton startButton;
    private static JButton endButton;
    private static MyLabel video;
    public Frame() {

    }

    public static void init(){
        frame = new JFrame("hck");
        frame.setSize(500, 500);                               // 设置窗口大小
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);                                  // 把窗口位置设置到屏幕中心
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);      // 当点击窗口的关闭按钮时退出程序（没有这一句，程序不会退出）

        basePanel = new JPanel();
        basePanel.setLayout(new GridBagLayout());

        video = new MyLabel();
        video.setText("Video");
        basePanel.add(video,new GBC(0,0,2,1).setFill(GBC.BOTH).setIpad(500,350).setWeight(100,100));

        startButton = new JButton("Start");
        basePanel.add(startButton,new GBC(0,1,1,1).setFill(GBC.BOTH).setWeight(0,0));

        endButton = new JButton("end");
        basePanel.add(endButton,new GBC(1,1,1,1).setFill(GBC.BOTH).setWeight(0,0));

        frame.add(basePanel);
    }



    private static class MyLabel extends JLabel {
        private BufferedImage image;

        public void setImage(BufferedImage img) {
            this.image = img;
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (image != null) {
                g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
            }
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

