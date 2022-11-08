package NettyStudy.Section02;

import NettyStudy.Util.BufferUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
@Slf4j
public class ReadThreader extends Thread {


    public final Selector selector;


    public ReadThreader(SocketChannel socketChannel) throws IOException {
        selector=Selector.open();
        socketChannel.configureBlocking(false);
        ByteBuffer buffer=ByteBuffer.allocate(256);
        socketChannel.register(selector, SelectionKey.OP_READ,buffer);
    }

    @Override
    public void run() {
        log.info("ReaderThread start accept......");
        while (true)
        {
            try {
                selector.select();
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                while (keyIterator.hasNext())
                {
                    SelectionKey key = keyIterator.next();
                    log.info("ReaderThread get Event......");
                    if (key.isReadable())
                    {
                        try{
                            String messgae = BufferUtil.getMessgaeFromKey(key);
                            if (!"".equals(messgae))
                            {
                                log.info("get message : {} from {}",messgae,key.channel());
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            key.cancel();
                        }
                    }
                    keyIterator.remove();
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
