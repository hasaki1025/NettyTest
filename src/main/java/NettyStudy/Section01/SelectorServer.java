package NettyStudy.Section01;

import NettyStudy.Util.BufferUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
@Slf4j
public class SelectorServer {
    public static void main(String[] args) throws IOException {
        //创建selector用于管理channel
        Selector selector = Selector.open();
        //创建SocketChannel,设置缓冲区，设置非堵塞
        ServerSocketChannel server = ServerSocketChannel.open();
        server.configureBlocking(false);
        //ServerSocketChannel向selector注册,并获取selectionkey
        SelectionKey selectionKey = server.register(selector, 0, null);
        //绑定accept事件
        selectionKey.interestOps(SelectionKey.OP_ACCEPT);
        server.bind(new InetSocketAddress(8080));
        while (true)
        {
            //调用select方法监听事件发生,堵塞运行
            log.info("selector waiting....");
            selector.select();
            log.info("selector get Event");
            //发生事件，获取事件对应的selectionkey
            Set<SelectionKey> keys = selector.selectedKeys();
            log.info("keys size {}",keys.size());
            Iterator<SelectionKey> keyIterator = keys.iterator();
            while (keyIterator.hasNext())
            {
                SelectionKey key = keyIterator.next();
                log.info("Event Type:{}",key.interestOps());
                //根据事件类型进行不同的处理
               if(key.isAcceptable())
               {
                   try{
                       //连接类型获取信道
                       ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                       log.info("{} Accept...",channel);
                       //监听
                       SocketChannel socketChannel = channel.accept();
                       //如果连接建立失败则取消执行该任务
                       if (socketChannel==null)
                       {
                           key.cancel();
                       }
                       else {
                           //创建附件
                           ByteBuffer buffer = ByteBuffer.allocate(4);
                           log.info("buffer size : {}",buffer.capacity());
                           //设置监听后获取的连接的堵塞类型
                           socketChannel.configureBlocking(false);
                           //注册到selector中
                           socketChannel.register(selector,SelectionKey.OP_READ,buffer);
                       }
                   }catch (Exception e)
                   {
                       e.printStackTrace();
                       //发生异常则取消执行
                       key.cancel();
                   }
               }
               else if (key.isReadable()){
                    try
                    {
                        String messgae = BufferUtil.getMessgaeFromKey(key);
                        if (!"".equals(messgae))
                        {
                            log.info("get message {} from {}",messgae.replace("\\n",""),key.channel());
                        }
                    }catch (IOException e)
                    {
                        e.printStackTrace();
                        key.cancel();
                    }
               }
               //每次解决之后删除本次活跃的事件
                keyIterator.remove();
            }
        }
    }
}
