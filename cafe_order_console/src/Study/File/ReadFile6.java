package Study.File;

import java.io.IOException;
import java.nio.file.Files;

import java.nio.file.Paths;

public class ReadFile6 {
    public static void main(String[] args) throws IOException {

        byte[] bytes = Files.readAllBytes(Paths.get("C:\\Users\\eunchan1\\Desktop\\hello.txt"));

        System.out.println(new String(bytes));

    }
}
