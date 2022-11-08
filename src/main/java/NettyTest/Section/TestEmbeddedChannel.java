package NettyTest.Section;

import io.netty.channel.*;
import io.netty.channel.embedded.EmbeddedChannel;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
public class TestEmbeddedChannel {

    public static void main(String[] args) {
        ChannelInboundHandlerAdapter h1 = new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                log.info("read.......");
                super.channelRead(ctx, msg);
            }
        };


        ChannelOutboundHandlerAdapter h2 = new ChannelOutboundHandlerAdapter() {
            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                log.info("write......");
                super.write(ctx, msg, promise);
            }
        };

        EmbeddedChannel channel = new EmbeddedChannel(h1, h2);
        channel.writeInbound(StandardCharsets.UTF_8.encode("hello"));
        channel.writeOutbound(StandardCharsets.UTF_8.encode("hi"));
    }
}
