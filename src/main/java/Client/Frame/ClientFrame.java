package Client.Frame;

import Client.Buffer.ClientBufferQueue;
import Client.Net.Client;
import GUIModule.GBC;
import GUIModule.MyLabel;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;

/**
 * Created by hck on 2018/3/28.
 */
public class ClientFrame implements Runnable{

    private static JFrame frame;
    private static JPanel basePanel;
    private static JButton startButton;
    private static JButton endButton;
    private static MyLabel video;
    private static JTextField info;
    private static boolean flag = false;

    public ClientFrame() {
        init();
    }

    public void init() {
        try {
            UIManager.put("RootPane.setupButtonVisible", false);
            org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
        } catch (Exception e) {
            e.printStackTrace();
        }
        frame = new JFrame("hck");
        frame.setSize(700, 500);                               // 设置窗口大小
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);                                  // 把窗口位置设置到屏幕中心
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);      // 当点击窗口的关闭按钮时退出程序（没有这一句，程序不会退出）

        basePanel = new JPanel();                                           // 设置底层容器
        basePanel.setLayout(new GridBagLayout());                           // 设置GirdBag布局管理器

        video = new MyLabel("video");                                    // 初始化视频显示区域
        try {
            video.setImage(ImageIO.read(ClientFrame.class.getClassLoader().getResourceAsStream("Orbit.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        video.repaint();
        basePanel.add(video, new GBC(0, 0, 8, 1).setFill(GBC.BOTH).setIpad(500, 350).setWeight(100, 100));

        info = new JTextField();                                            // 初始化Info
        basePanel.add(info, new GBC(0, 1, 6, 1).setFill(GBC.HORIZONTAL).setInsets(0,0,0,0).setIpad(350,6).setWeight(100, 0));

        startButton = new JButton("Start");                             // 初始化开始按钮
        startButton.setUI(new org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI().setNormalColor(org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI.NormalColor.green));
        basePanel.add(startButton, new GBC(6, 1, 1, 1).setInsets(0,0,0,0).setWeight(0, 0));

        endButton = new JButton("Stop");                                 //  初始化结束按钮
        endButton.setUI(new org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI().setNormalColor(org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI.NormalColor.red));
        basePanel.add(endButton, new GBC(7, 1, 1, 1).setInsets(0,0,0,0).setWeight(0, 0));

        initListener();

        frame.add(basePanel);                                                // 添加Jpanel到Frame
        frame.setVisible(true);                                              // 设置Frame为可见
    }

    private void initListener(){
        //  注册输入框监听器
        final String defaultStr = "目标主机地址.";
        info.setForeground(Color.GRAY);                 //  默认直接提示
        info.setText("目标主机地址.");
        info.addFocusListener((new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                String current_str = info.getText();
                if(defaultStr.equals(current_str)){
                    info.setText("");
                    info.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                String current_str = info.getText();
                if( current_str == null || "".equals(current_str)){
                    info.setForeground(Color.GRAY);
                    info.setText(defaultStr);
                }
            }
        }));
        // ------------------------------------

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String str = info.getText();
                System.out.println("开始:"+str);
                if(defaultStr.equals(str)){
                    popUpErroInfo("不能为空");
                    return;
                }
                if(!isIpAddress(str)){
                    popUpErroInfo("IP格式输入有误");
                    return;
                }else {
                    try {
                        /**
                         * ****************启动Client服务*************
                         */
                        Client.start(str);
                    } catch (InterruptedException e1) {
                        popUpErroInfo("连接失败");
                    }
                }
            }
        });

        endButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client.disConnect();
            }
        });



    }

    @Override
    public void run() {
        while (true) {
            try {
                if (ClientBufferQueue.img_queue.isEmpty()) {
                    Thread.sleep(500);
                    continue;
                }
//                if(ClientBufferQueue.img_queue.size() < 16){
//                    Thread.sleep(500);
//                    System.out.println("缓冲中....");
//                    continue;
//                }
                video.setImage(ClientBufferQueue.img_queue.poll());
                video.repaint();
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private boolean isIpAddress(String ip){
        String[] ips = ip.split("\\.");
        try{
            if(ips.length == 0 || ips.length > 4)
                return false;
            for(String str : ips){
                int tem = Integer.parseInt(str);
                if(tem < 0 || tem > 255){
                    return false;
                }
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }

    private void popUpErroInfo(String msg){
        JOptionPane.showMessageDialog(frame, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
