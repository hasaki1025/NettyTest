package NettyAdvance.Section01;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.StandardCharsets;

public class PackageClient {

    public static void main(String[] args) throws InterruptedException {
        Channel channel = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                            //连接建立完成后触发
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                for (int i = 0; i < 10; i++) {
                                    ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(16);
                                    buffer.writeBytes(new byte[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16});
                                    ctx.writeAndFlush(buffer);
                                }
                            }
                        });
                    }
                }).connect("127.0.0.1",8080).sync().channel();
    }
}
