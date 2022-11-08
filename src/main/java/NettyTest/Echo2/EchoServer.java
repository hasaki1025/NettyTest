package NettyTest.Echo2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EchoServer {
    public static void main(String[] args) {
        StringDecoder decoder = new StringDecoder();
        StringEncoder encoder=new StringEncoder();

        ChannelInboundHandlerAdapter read = new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                log.info("get message {}", msg);
            }
        };

        ChannelOutboundHandlerAdapter write = new ChannelOutboundHandlerAdapter() {
            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                log.info("write message {}", msg);
            }
        };

        EmbeddedChannel channel = new EmbeddedChannel(decoder, read, write, encoder);


    }
}
