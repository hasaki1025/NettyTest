package Http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;

@Slf4j
public class HttpTestServer {
    public static void main(String[] args) {

        new ServerBootstrap()
                .group(new NioEventLoopGroup(1),new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nc) throws Exception {
                        ChannelPipeline pipeline = nc.pipeline();
                        pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                        //添加Netty自带的http解码器和编码器(HttpServerCodec是两者的结合)
                        pipeline.addLast(new HttpServerCodec());
                        //SimpleChannelInboundHandler是只专注于指定泛型的处理器
                        pipeline.addLast(new SimpleChannelInboundHandler<HttpRequest>(){
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, HttpRequest httpRequest) throws Exception {
                                log.info("get http request {}",httpRequest);
                                log.info("request URI {}",httpRequest.uri());

                                String uri="src/main/resources"+httpRequest.uri();
                                File file = new File(uri);
                                if (file.exists())
                                {
                                    DefaultFullHttpResponse response =
                                            new DefaultFullHttpResponse(httpRequest.protocolVersion(), HttpResponseStatus.OK);
                                    FileChannel channel = new FileInputStream(file).getChannel();
                                    ByteBuf buffer = ctx.alloc().buffer();
                                    buffer.writeBytes(channel,0, (int) channel.size());
                                    log.info("file content: {}",buffer.toString(StandardCharsets.UTF_8));
                                    response.content().writeBytes(buffer);
                                    //不添加该参数则浏览器会认为请求还没有结束，会持续等待
                                    response.headers().setInt(CONTENT_LENGTH, (int) channel.size());
                                    ctx.writeAndFlush(response);
                                }
                                else {
                                    DefaultFullHttpResponse response =
                                            new DefaultFullHttpResponse(httpRequest.protocolVersion(), HttpResponseStatus.NOT_FOUND);
                                    response.content().writeBytes("<h1>nothings to show....</h1>".getBytes(StandardCharsets.UTF_8));
                                    ctx.writeAndFlush(response);
                                }
                            }
                        });
                    }
                }).bind(80);
    }
}
