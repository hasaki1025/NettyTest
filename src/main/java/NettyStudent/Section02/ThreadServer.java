package NettyStudent.Section02;

import NettyStudent.Section01.SelectorServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;

public class ThreadServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel channel = ServerSocketChannel.open();
        Selector selector = Selector.open();
        channel.configureBlocking(false);
        channel.bind(new InetSocketAddress(8080));
        channel.register(selector, SelectionKey.OP_ACCEPT,null);
        while (true)
        {
            selector.select();
            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
            while (keyIterator.hasNext())
            {
                SelectionKey key = keyIterator.next();
                if (key.isAcceptable())
                {
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    SocketChannel socketChannel = server.accept();
                    if (socketChannel==null)
                    {
                        key.cancel();
                    }
                }

                keyIterator.remove();
            }
        }
    }
}
