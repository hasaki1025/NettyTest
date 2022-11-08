package NettyAdvance.Section01;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import static java.lang.Thread.sleep;

@Slf4j
public class FixedLengthFrameDecoderTestClient {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        Channel channel = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
                    }
                }).connect("127.0.0.1", 8080).sync().channel();
        byte[] bytes = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        buffer.writeBytes(bytes);
        channel.writeAndFlush(buffer);
        sleep(1000);

        byte[] b2 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ByteBuf buf2 = ByteBufAllocator.DEFAULT.buffer();
        buffer.writeBytes(b2);
        channel.writeAndFlush(buf2);
        group.shutdownGracefully();
    }
}
