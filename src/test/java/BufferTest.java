import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

public class BufferTest {
    @Test
    public void testBuffer() throws IOException {

        ByteBuffer buffer = ByteBuffer.allocate(100);
        buffer.put(StandardCharsets.UTF_8.encode("123123"));
        buffer.flip();
        while (buffer.hasRemaining())
        {
            System.out.print((char) buffer.get());
        }

        channel.read(buffer.nioBuffer());
        System.out.println(buffer.toString(StandardCharsets.UTF_8));
    }

    @Test
    public void name() throws IOException {
        FileChannel channel = new FileInputStream("src/main/resources/123.txt").getChannel();
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(1000);
        long a=0x11000000;
        System.out.println(a);
    }
}
