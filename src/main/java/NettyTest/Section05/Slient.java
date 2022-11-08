package NettyTest.Section05;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
@Slf4j
public class Slient {
    public static void main(String[] args) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        buffer.writeCharSequence("abcdefghijklmn", StandardCharsets.UTF_8);
        log.info("buffer before slice buffer {}",buffer.toString(StandardCharsets.UTF_8));
        ByteBuf buf = buffer.slice(4, 4);
        log.info("buffer after slice buffer {}",buf.toString(StandardCharsets.UTF_8));
        buf.setByte(1,100);
        log.info("buffer after change {}",buffer.toString(StandardCharsets.UTF_8));
    }
}
