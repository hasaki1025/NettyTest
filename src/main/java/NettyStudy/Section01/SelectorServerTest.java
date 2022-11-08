package NettyStudy.Section01;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

public class SelectorServerTest {
    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        //创建SocketChannel,设置缓冲区，设置非堵塞
        ServerSocketChannel server1 = ServerSocketChannel.open();
        ServerSocketChannel server2 = ServerSocketChannel.open();
        server1.configureBlocking(false);
        server2.configureBlocking(false);
        //ServerSocketChannel向selector注册,并获取selectionkey
        SelectionKey selectionKey1 = server1.register(selector, 0, null);
        SelectionKey selectionKey2 = server2.register(selector, 0, null);
        //绑定accept事件
        selectionKey1.interestOps(SelectionKey.OP_ACCEPT);
        selectionKey2.interestOps(SelectionKey.OP_ACCEPT);
        server1.bind(new InetSocketAddress(8080));
        server2.bind(new InetSocketAddress(8081));

        while (true)
        {
            selector.select();
            System.out.println(selector.selectedKeys().size());
            for (SelectionKey key : selector.selectedKeys()) {
                System.out.println(key);
                key.cancel();
            }
        }
    }
}
