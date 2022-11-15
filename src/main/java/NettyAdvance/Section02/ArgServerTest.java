package NettyAdvance.Section02;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ArgServerTest {
    public static void main(String[] args) {
        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                //给ServerSocketChannel配置参数
                // .option()
                //给SocketChannel配置参数
                .option(ChannelOption.SO_BACKLOG,1000)
                .childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS,2);

    }
}
