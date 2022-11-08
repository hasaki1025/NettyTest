package NettyTest.Section03;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;
@Slf4j
public class loggingClient {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        Channel channel = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                .connect("127.0.0.1", 8080)
                .sync()
                .channel();


        new Thread(()->{
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext())
            {
                String s = scanner.next();
                if ("Q".equals(s))
                {
                    break;
                }
                channel.writeAndFlush(s);
            }
            channel.close();
        }).start();
       /* ChannelFuture closeFuture = channel.closeFuture();
        closeFuture.sync();*/
        ChannelFuture channelFuture = channel.closeFuture();
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                log.info("资源释放...");
                //正常关闭，先拒绝新任务，再处理没有处理完成的任务
                group.shutdownGracefully();
            }
        });

    }
}
