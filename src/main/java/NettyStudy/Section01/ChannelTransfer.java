package NettyStudy.Section01;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class ChannelTransfer {
    public static void main(String[] args) {
        try (
                FileChannel in = new FileInputStream("src/main/resources/123.txt").getChannel();
                FileChannel out = new FileOutputStream("src/main/resources/321.txt").getChannel();
        ) {
            in.transferTo(0,in.size(),out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
