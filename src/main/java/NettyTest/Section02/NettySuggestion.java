package NettyTest.Section02;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
@Slf4j
public class NettySuggestion {
    public static void main(String[] args) {
        EventLoopGroup group= new DefaultEventLoopGroup();
        new ServerBootstrap()
                //第一个参数为父Group，第二个为子group
                .group(new NioEventLoopGroup(1),new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        log.info("get Connection : {}",ch);
                        ch.pipeline().addLast("handler1",new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf buffer= (ByteBuf) msg;
                                log.info("this is handler 1");
                                log.info("context : {}",ctx);
                                //将消息传递给下一个Handler
                                ctx.fireChannelRead(((ByteBuf) msg).toString(StandardCharsets.UTF_8));
                            }
                        }).addLast(group,"handler2",new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    log.info("this is handler2...");
                                    log.info("context : {}",ctx);
                                    log.info("a long time....");
                                    log.info("get message : {}",msg);
                            }
                        });
                    }
                }).bind(8080);
    }
}
