package NettyStudent.Util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;

public class BufferUtil {

    public static String bufferToString(ByteBuffer buffer)
    {
        StringBuilder stringBuffer = new StringBuilder();
        while (buffer.hasRemaining())
        {
            stringBuffer.append((char) buffer.get());
        }
        return stringBuffer.toString();
    }

    public static String getMessgaeFromKey(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        int read = channel.read(buffer);
        if (read==-1)
        {
            key.cancel();
            return "";
        }
        buffer.flip();
        StringBuilder s=new StringBuilder();
        while (buffer.hasRemaining())
        {
            char c = (char) buffer.get();
            s.append(c);
            if (c=='\n')
            {
                break;
            }
        }

        if(s.toString().contains("\n"))
        {
            buffer.compact();
        }
        else if (buffer.limit()==buffer.capacity())
        {
            ByteBuffer allocate = ByteBuffer.allocate(buffer.capacity() * 2);
            allocate.put(StandardCharsets.UTF_8.encode(s.toString()));
            key.attach(allocate);
        }
        return s.toString().endsWith("\n") ?  s.toString() : "";
    }
}
