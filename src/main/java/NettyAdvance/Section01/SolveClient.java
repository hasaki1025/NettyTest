package NettyAdvance.Section01;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SolveClient {
    public static void main(String[] args) {
        for (int i = 0; i < 6; i++) {
            send();
        }
        log.info("send ending....");
    }

    private static void send() {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Channel channel = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                                //连接建立完成后触发
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {

                                        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(16);
                                        buffer.writeBytes(new byte[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16});
                                        ctx.writeAndFlush(buffer);
                                        ctx.channel().close();
                                }
                            });
                        }
                    }).connect("127.0.0.1",8080).sync().channel();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        finally {
            group.shutdownGracefully();
        }
    }


}
