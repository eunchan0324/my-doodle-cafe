package Study.File;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


public class ReadFile5 {
    public static void main(String[] args) throws IOException {

        List<String> lines = Files.readAllLines(Paths.get("C:\\Users\\eunchan1\\Desktop\\hello.txt"));

        System.out.println(lines);
    }
}
