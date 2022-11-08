package NettyTest.Section02;

import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.NettyRuntime;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class EventLoopTest {
    public static void main(String[] args) {
        //NioEventLoopGroup可处理IO事件，普通任务，定时任务
        EventLoopGroup group=new NioEventLoopGroup(2);

        group.next().submit(new Runnable() {
            @Override
            public void run() {
                log.info("这是一个普通任务");
            }
        });
        //第一个参数是Runnable,第二个参数是执行延迟（多久之后执行），第三个是周期，第四个是时间单位
        group.next().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                log.info("这是一个普通任务");
            }
        },1,2, TimeUnit.SECONDS);



    }
}
