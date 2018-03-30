package test;

import GUI.GBC;
import GUI.MyLabel;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by hck on 2018/3/26.
 */
public class TestClient {

    public static void main(String[] args) throws InterruptedException {
        new Thread(new ClientFrame()).start();
        new TestClient().start();
    }

    public void start() throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup work = new NioEventLoopGroup();

        bootstrap.group(work)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new ClientHandle());
                    }
                });

        bootstrap.connect("10.10.110.140", 9999).sync();
    }
}

class ClientHandle extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello".getBytes()));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buffer = (ByteBuf) msg;
        System.err.println("[Client] - 接收到: " + buffer.readableBytes());
        byte[] buf = new byte[buffer.readableBytes()];
        buffer.readBytes(buf);
        ClientBuffer.add(buf);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}

class ClientBuffer {
    static ByteArrayOutputStream bytebuf = new ByteArrayOutputStream();
    static ArrayBlockingQueue<BufferedImage> img_queue = new ArrayBlockingQueue(16);

    public static void add(byte[] bytes) {
        try {
            int len = bytes.length;
            if (bytes[len - 1] == -128 && bytes[len - 2] == -128) {       //  尾包
                bytebuf.write(bytes);
                toImage();
            } else {
                bytebuf.write(bytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void toImage() throws IOException {
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytebuf.toByteArray()));
        img_queue.offer(image);
        bytebuf.reset();
    }
}

class ClientFrame implements Runnable {
    private static JFrame frame;
    private static JPanel basePanel;
    private static JButton startButton;
    private static JButton endButton;
    private static MyLabel video;
    private static JTextField info;
    private static boolean flag = false;

    public ClientFrame() {
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init() throws IOException {
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
        video.setImage(ImageIO.read(ClientFrame.class.getClassLoader().getResourceAsStream("Orbit.png")));
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

        frame.add(basePanel);                                                // 添加Jpanel到Frame
        frame.setVisible(true);                                              // 设置Frame为可见
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (ClientBuffer.img_queue.isEmpty()) {
                    Thread.sleep(200);
                    continue;
                }
                video.setImage(ClientBuffer.img_queue.poll());
                video.repaint();
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}