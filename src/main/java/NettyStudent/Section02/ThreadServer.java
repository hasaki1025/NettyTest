package NettyStudent.Section02;

import NettyStudent.Section01.SelectorServer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.HashSet;
import java.util.Iterator;

@Slf4j
public class ThreadServer {


    public static void main(String[] args) throws IOException {
        ServerSocketChannel channel = ServerSocketChannel.open();
        Selector selector = Selector.open();
        channel.configureBlocking(false);
        channel.bind(new InetSocketAddress(8080));
        channel.register(selector, SelectionKey.OP_ACCEPT,null);
        while (true)
        {
            log.info("ThreadServer wait....");
            selector.select();
            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

            while (keyIterator.hasNext())
            {
                SelectionKey key = keyIterator.next();
                log.info("ThreadServer get Connect");
                if (key.isAcceptable())
                {
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    SocketChannel socketChannel = server.accept();
                    if (socketChannel==null)
                    {
                        key.cancel();
                        continue;
                    }
                    log.info("ThreadServer get Channel : {}",socketChannel);
                    ReadThreader threader = new ReadThreader(socketChannel);
                    threader.setName("Read-worker-01");
                    threader.start();

                }
                keyIterator.remove();
            }
        }
    }
}
