package RedisTest;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@Slf4j
public class RedisTestClient {
    public  static  final byte[] LINE={13,10};

    public static void main(String[] args) {
        String host="192.168.189.10";
        int port=6379;

        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Channel channel = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                            ChannelPipeline pipeline = nioSocketChannel.pipeline();
                            //pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                            pipeline.addLast(new ChannelOutboundHandlerAdapter(){
                                @Override
                                public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                    String redis= (String) msg;
                                    String[] s = redis.split(" ");
                                    ByteBuf buffer = ctx.alloc().buffer();
                                    //长度设置
                                    String length="*"+String.valueOf(s.length);
                                    buffer.writeBytes(length.getBytes());
                                    buffer.writeBytes(LINE);
                                    //指令设置
                                    for (String str : s) {
                                        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
                                        String len="$"+String.valueOf(str.length());
                                        buffer.writeBytes(len.getBytes());
                                        buffer.writeBytes(LINE);
                                        buffer.writeBytes(bytes);
                                        buffer.writeBytes(LINE);
                                    }
                                    super.write(ctx,buffer,promise);
                                }
                            });
                            pipeline.addLast(new ChannelInboundHandlerAdapter(){
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    ByteBuf buf = (ByteBuf) msg;
                                    log.info("{}",buf.toString(StandardCharsets.UTF_8));
                                }
                            });
                        }
                    }).connect(host,port).sync().channel();

            new Thread(()->{
                    while (true)
                    {
                        Scanner scanner = new Scanner(System.in);
                        String next = scanner.nextLine();
                        if ("Q".equals(next))
                        {
                            break;
                        }
                        channel.writeAndFlush(next);
                    }
            }).start();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
