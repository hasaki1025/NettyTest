package NettyTest.Section01;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import static java.lang.Thread.sleep;

@Slf4j
public class HelloNettyClient {
    public static boolean isConnected=false;

    public static void main(String[] args) throws InterruptedException {

        //创建启动器
        ChannelFuture connect = new Bootstrap()
                //添加group,其中包含了线程创建工厂，线程池、selector创建相关等基础类，也即是selector，线程池的设置
                .group(new NioEventLoopGroup())
                //选择信道实现类，设置
                .channel(NioSocketChannel.class)
                //添加客户端处理器(在连接创建时调用方法)，相当于缓冲区的处理器设置
                .handler(new ChannelInitializer<NioSocketChannel>() {

                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        log.info("connect successfully....");
                        HelloNettyClient.isConnected=true;
                        //字符串编码器添加
                        nioSocketChannel.pipeline().addLast(new StringEncoder());
                    }
                })
                //连接到服务器
                .connect("127.0.0.1", 8080);

       while (!isConnected)
       {
           log.info("wait for NIO thread connect....");
           sleep(1000);
       }
       connect.channel().writeAndFlush("你好,Netty");
       /* ChannelFuture future = connect.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                log.info("Client connect build successfully...");
                future.channel().writeAndFlush("你好,Netty");
            }
        });
       log.info("keep going...");*/

    }
}
