package NettyTest.Section04;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

@Slf4j
public class NettyPromise {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        EventLoop eventLoop = new NioEventLoopGroup().next();
        DefaultPromise<Integer> promise = new DefaultPromise<>(eventLoop);
        new Thread(){
            @Override
            public void run() {
                log.info("执行计算....");
                try {
                   // int a=1/0;
                    sleep(1000);
                    promise.setSuccess(100);
                } catch (InterruptedException e) {
                    //计算失败则设置错误结果（将异常作为参数传入）
                    promise.setFailure(e);
                    throw new RuntimeException(e);
                }

            }

        }.start();

        log.info("wait for result....");
        log.info("get result {}",promise.get());
    }

}
