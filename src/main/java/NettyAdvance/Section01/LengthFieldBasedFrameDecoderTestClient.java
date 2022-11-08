package NettyAdvance.Section01;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@Slf4j
public class LengthFieldBasedFrameDecoderTestClient {
    public static void main(String[] args) throws InterruptedException {
       send();
    }

    private static void send() throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        int sign=0;
        Channel channel = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                        pipeline.addLast(new ChannelOutboundHandlerAdapter(){
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {

                                log.info("this is write method");
                                byte[] bytes = ((String) msg).getBytes(StandardCharsets.UTF_8);
                                log.info("raw message {}",msg);
                                //添加长度字段
                                int length= bytes.length;
                                //添加头部字段
                                int head=1;
                                //拼接
                                ByteBuf buffer = ctx.alloc().buffer(12 + bytes.length);
                                buffer.writeInt(head);
                                buffer.writeInt(length);
                                buffer.writeInt(sign);
                                buffer.writeBytes(bytes);
                                super.write(ctx,buffer,promise);
                            }

                        });

                    }
                }).connect("127.0.0.1", 8080).sync().channel();

        new Thread(){
            @Override
            public void run() {
                while (true)
                {
                    Scanner scanner = new Scanner(System.in);
                    String next = scanner.next();
                    if ("Q".equals(next))
                    {
                        return;
                    }

                    channel.writeAndFlush(next);
                }
            }
        }.start();
        group.shutdownGracefully();
    }
}
