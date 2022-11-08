package NettyTest.Echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import io.netty.handler.codec.string.StringEncoder;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@Slf4j
public class EchoClient {
    public static void main(String[] args) throws InterruptedException {
        Channel channel = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        //pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                        pipeline.addLast(new StringEncoder());
                        //接受的数据打印
                        pipeline.addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf buffer = (ByteBuf) msg;
                                log.info("get Echo from Server : {}",((ByteBuf) msg).toString(StandardCharsets.UTF_8));
                            }
                        });

                    }
                }).connect("127.0.0.1", 8080).sync().channel();
        new Thread() {
            @Override
            public void run() {
                Scanner scanner = new Scanner(System.in);
                while (true)
                {
                    String next = scanner.next();
                    if ("Q".equals(next))
                    {
                        break;
                    }
                    channel.writeAndFlush(next);
                }
            }
        }.start();



    }
}
