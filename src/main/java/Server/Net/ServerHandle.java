package Server.Net;

import Server.Buffer.BufferQueue;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by hck on 2018/3/28.
 */
public class ServerHandle extends ChannelInboundHandlerAdapter implements Runnable{
    ChannelHandlerContext ctx;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
        System.out.println(ctx.channel().toString()+" 已连接...");
        super.channelRegistered(ctx);
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


    private ByteBuf wrapPackage(byte[] res){
        ByteBuf buf = Unpooled.buffer(res.length+2);
        buf.writeBytes(res);
        buf.writeByte(-128);
        buf.writeByte(-128);
        return buf;
    }

    @Override
    public void run() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for(; ; ){
            BufferedImage image = null;
            try {
                image = BufferQueue.peek();
                if(image != null){
                    ImageIO.write(image,"png",out);
                    ByteBuf buffer = wrapPackage(out.toByteArray());
                    System.out.println("[Server] -发送数据包,大小:"+buffer.array().length);
                    try{
                        ctx.writeAndFlush(buffer);
                    }catch (RuntimeException e){
                        break;
                    }
                    out.reset();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}