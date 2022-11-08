package NettyTest.Section04;

import java.util.concurrent.*;

import static java.lang.Thread.sleep;

public class JDKFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                12, 24, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<>(), new ThreadPoolExecutor.AbortPolicy()
        );
        Future<Integer> future = pool.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                sleep(1000);
                return 50;
            }
        });
        System.out.println("waiting for result....");

        System.out.println(future.get());

        System.out.println("get result.....");
    }

}
