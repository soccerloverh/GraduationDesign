package Client.Net;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ResourceLeakDetector;

/**
 * Created by hck on 2018/3/28.
 */
public class Client {
    private static ChannelFuture f;
    public static void start(String host) throws InterruptedException {
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
        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.ADVANCED);
        f = bootstrap.connect(host, 9999).sync();
        f.channel().closeFuture().sync();
    }

    public static void disConnect(){
        if(ClientHandle.ctx != null){
            ClientHandle.ctx.channel().close();
            System.out.println("关闭连接");
        }
    }
}
//Main-Class: Client.ClientBoot
