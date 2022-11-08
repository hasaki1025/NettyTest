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

    }

    @Test
    public void name() {
        StringBuilder builder = new StringBuilder("123\n");
        System.out.println(builder.toString().replace("\n", ""));

    }
}
