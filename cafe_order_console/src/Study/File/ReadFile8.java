package Study.File;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ReadFile8 {
    public static void main(String[] args) throws IOException {

        String str = Files.readString(Paths.get("C:\\Users\\eunchan1\\Desktop\\hello.txt"));

        System.out.println(str);
    }
}
