package Server.Net;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by hck on 2018/4/2.
 */
public class ImageSender implements Runnable{
    private ChannelHandlerContext ctx;



    @Override
    public void run() {
        if(ctx.isRemoved()){

        }
    }
}
