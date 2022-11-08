package NettyStudy.Section01;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class PathAndPaths {
    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main");
        Files.walkFileTree(path, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                System.out.println(file.getFileName());
                return super.visitFile(file, attrs);
            }
        });

    }
}

