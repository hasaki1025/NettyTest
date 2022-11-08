package NettyStudent.Section01;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;

import static java.lang.Thread.sleep;

public class Client {
    public static void main(String[] args) throws IOException {
        SocketChannel channel = SocketChannel.open();
        ByteBuffer buffer = ByteBuffer.allocate(16);
        try (FileChannel fileChannel = new FileInputStream("src/main/resources/123.txt").getChannel()) {
            int read = fileChannel.read(buffer);
            System.out.println("waiting connect....");
            if (channel.connect(new InetSocketAddress("127.0.0.1",8080))) {
                System.out.println("connect successfully");
                buffer.put((byte) '\n');
                buffer.flip();
                sleep(1000);
                channel.write(buffer);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }


    }
}
