package NettyTest.Section05;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;

import java.nio.charset.StandardCharsets;

public class CompositeTest {
    public static void main(String[] args) {
        ByteBuf b1 = ByteBufAllocator.DEFAULT.buffer();
        b1.writeCharSequence("hello,", StandardCharsets.UTF_8);
        ByteBuf b2 = ByteBufAllocator.DEFAULT.buffer();
        b2.writeCharSequence("netty",StandardCharsets.UTF_8);
        CompositeByteBuf components = ByteBufAllocator.DEFAULT.compositeDirectBuffer();
        //第一个参数为是否添加写入指针（从添加了Components之后的末端作为写入指针的位置），对于缓冲区而言可读区域是从读取指针开始到写入指针为止，如果第一个参数为false（默认为false），则写入指针为0，调用ToString方法将无法输出值
        CompositeByteBuf byteBufs = components.addComponents(true,b1, b2);
        System.out.println(byteBufs.toString());
    }
}
