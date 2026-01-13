package Study.File;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CreateFile3 {
    public static void main(String[] args) {

        Path filePath = Paths.get("C:\\Users\\eunchan1\\Desktop\\example1\\file.txt");

        try {
            Path newFilePath = Files.createFile(filePath);
            System.out.println(newFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
