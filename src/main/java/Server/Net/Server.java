package Server.Net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.log4j.Logger;


/**
 * Created by hck on 2018/3/22.
 */
public class Server {
    private static Logger log = Logger.getLogger(Server.class);

    public static void start() {
        //  引导
        ServerBootstrap bootstrap = new ServerBootstrap();
        //  工作线程Group
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup work = new NioEventLoopGroup();

        bootstrap.group(boss, work)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ServerHandle());
                    }
                });
        ChannelFuture f = null;
        try {
            f = bootstrap.bind(9999).sync();
            log.info("》》》》服务器已在9999端口开启");
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("》》》》服务器在9999端口开启失败.失败原因:"+e.getMessage());
        }
    }
}
