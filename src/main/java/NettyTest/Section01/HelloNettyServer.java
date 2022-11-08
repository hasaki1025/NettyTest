package NettyTest.Section01;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HelloNettyServer {
    public static void main(String[] args) {
        //创建服务器启动器（主要用于添加其他组件，相当于一个容器）
        new ServerBootstrap()
                //添加group,其中包含了线程创建工厂，线程池、selector创建相关等基础类，也即是selector，线程池的设置
                .group(new NioEventLoopGroup())
                //设置服务器ServerSocketChannel的实现类，在ServerBootstrap中创建并设置NioServerSocketChannel的工厂类
                .channel(NioServerSocketChannel.class)
                //添加worker线程的配置（类似于之前的多线程读取）
                .childHandler(
                        //初始化对应的worker
                        new ChannelInitializer<NioSocketChannel>() {
                            @Override
                            protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                                //添加String解码器（字节流转String）
                                nioSocketChannel.pipeline().addLast(new StringDecoder());
                                //添加worker线程的的处理过程
                                nioSocketChannel.pipeline().addLast(
                                        new ChannelInboundHandlerAdapter() {
                                            @Override//打印转换完成的数据信息
                                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                                log.info("get messgae : {}",msg);
                                                super.channelRead(ctx,msg);
                                            }
                                        }
                                );
                            }
                        }
                )//绑定端口
                .bind(8080);
    }
}
