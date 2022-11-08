package NettyStudent.Section01;



import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
@Slf4j
public class ServerIO {

    public static void main(String[] args) throws IOException {
        ServerSocketChannel socketChannel = ServerSocketChannel.open();
        socketChannel.bind(new InetSocketAddress(8080));
        ArrayList<SocketChannel> channels = new ArrayList<>();
        ByteBuffer buffer = ByteBuffer.allocate(16);
        socketChannel.configureBlocking(false);
        while (true)
        {
            log.info("waiting for connect...");
            SocketChannel channel = socketChannel.accept();
            log.info("connect successfully");
            channels.add(channel);
            for (SocketChannel c : channels) {
                c.read(buffer);
                buffer.flip();
                while (buffer.hasRemaining())
                {
                    System.out.print((char) buffer.get());
                }
                System.out.println();
                buffer.clear();
            }
        }

    }
}
