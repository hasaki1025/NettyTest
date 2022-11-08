package NettyAdvance.Section01;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.nio.charset.StandardCharsets;

public class LineBasedFrameDecoderTestServer {
    public static void main(String[] args) {
        new ServerBootstrap()
                .group(new NioEventLoopGroup(1),new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        //创建LineBasedFrameDecoder需要指定消息最大长度，一旦超过这个长度时还没有找到换行符则抛出异常
                        //pipeline.addLast(new LineBasedFrameDecoder(10));
                        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
                        buffer.writeBytes("$".getBytes(StandardCharsets.UTF_8));
                        pipeline.addLast(new DelimiterBasedFrameDecoder(10,buffer));
                        pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                    }
                }).bind(8080);
    }
}
