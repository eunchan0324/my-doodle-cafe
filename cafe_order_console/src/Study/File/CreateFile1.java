package Study.File;

import java.io.File;
import java.io.IOException;

public class CreateFile1 {
    public static void main(String[] args) {

        File file = new File("C:\\Users\\eunchan1\\Desktop\\file.txt");

        try {
            if (file.createNewFile()) {
                System.out.println("File created");
            } else {
                System.out.println("File already exists");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
