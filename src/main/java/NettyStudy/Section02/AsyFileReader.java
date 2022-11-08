package NettyStudy.Section02;

import NettyStudy.Util.BufferUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static java.lang.Thread.sleep;

@Slf4j
public class AsyFileReader {
    private static boolean finished=false;
    public static void main(String[] args) {
        Path path = Paths.get("src/main/resources/123.txt");
        try (AsynchronousFileChannel channel = AsynchronousFileChannel.open(path, StandardOpenOption.READ)) {
            ByteBuffer buffer = ByteBuffer.allocate(256);
            log.info("start reading...");
            channel.read(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override//读取完成后的回调方法
                public void completed(Integer result, ByteBuffer attachment) {
                    log.info("read completed....");
                    attachment.flip();
                    log.info("get message : {}",BufferUtil.bufferToString(buffer));
                    buffer.compact();
                    try {
                        sleep(100000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    finished=true;
                }

                @Override//读取失败后的回调方法
                public void failed(Throwable exc, ByteBuffer attachment) {
                    exc.printStackTrace();
                }
            });

            while (!finished)
            {
                log.info("waiting IO Result...");
                sleep(1000);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
