package NettyStudent.Section01;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

import static java.lang.Thread.sleep;

public class Client02 {
    public static void main(String[] args) throws IOException, InterruptedException {
        SocketChannel channel = SocketChannel.open();
        String[] message=new String[]{"1234","hello","fuck you"};

        if (channel.connect(new InetSocketAddress("127.0.0.1",8080))) {
            StringBuilder sb=new StringBuilder();
            for (String s : message) {
               sb.append(s+'\n');
            }
            ByteBuffer buffer = StandardCharsets.UTF_8.encode(sb.toString());
            sleep(10000);
            channel.write(buffer);
            channel.shutdownOutput();
        }
    }
}
