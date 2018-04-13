package Server.Net;

import Server.Buffer.BufferQueue;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by hck on 2018/3/28.
 */
public class ServerHandle extends ChannelInboundHandlerAdapter implements Runnable {
    ChannelHandlerContext ctx;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
        System.out.println(ctx.channel().toString() + " 已连接...");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        String request = new String(bytes);
        System.out.println("客户端发来请求:" + request);
        sendImage();
    }

    private void sendImage() {
        new Thread(this).start();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.err.println("ExceptionCaught:" + cause.getMessage());
        super.exceptionCaught(ctx, cause);
    }


    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.err.println("ChannelUnregistered");
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.err.println("ChannelInactive");
        super.channelInactive(ctx);
    }


    @Override
    public void run() {
        for (; ; ) {
            if (!ctx.channel().isActive()) {
                break;
            }
            if (!BufferQueue.isEmpty()) {
                byte[] bytes = BufferQueue.peek();
                ByteBuf buf = Unpooled.buffer(bytes.length);
                buf.writeBytes(bytes);
                ctx.writeAndFlush(buf);
                continue;
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}