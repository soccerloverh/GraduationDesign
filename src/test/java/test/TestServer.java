package test;


import io.netty.bootstrap.ServerBootstrap;
    import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by hck on 2018/3/26.
 */
public class TestServer {

    public static void main(String[] args) {
        new TestServer().start();
    }

    public void start(){
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup work = new NioEventLoopGroup();

        serverBootstrap.group(boss,work)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,24)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new ServerHandler());
                    }
                });
        try {
            serverBootstrap.bind(9999).sync();
            System.out.println("Server 开启....");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class ServerHandler extends ChannelInboundHandlerAdapter{
    ChannelHandlerContext ctx;
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
        System.out.println(ctx.channel().toString()+" 已连接...");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        String request = new String(bytes);
        System.out.println("客户端发来请求:"+request);
        sendImage();
    }

    private void sendImage(){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Toolkit kit = Toolkit.getDefaultToolkit();
        for(; ; ){
            BufferedImage image = null;
            try {
                image = new Robot().createScreenCapture(new Rectangle(0, 0, kit.getScreenSize().width, kit.getScreenSize().height));
                ImageIO.write(image,"png",out);
                writeBack(out.toByteArray());
                out.reset();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    public void writeBack(byte[] res){
        ByteBuf buffer = wrapPackage(res);
        System.out.println("[Server] -发送数据包,大小:"+buffer.array().length);
        ctx.writeAndFlush(buffer);
    }

    private ByteBuf wrapPackage(byte[] res){
        ByteBuf buf = Unpooled.buffer(res.length+2);
        buf.writeBytes(res);
        buf.writeByte(-128);
        buf.writeByte(-128);
        return buf;
    }
}
