package Study.File;

import java.io.FileReader;
import java.io.IOException;

public class ReadFile {
    public static void main(String[] args) throws IOException {

        FileReader reader = new FileReader("C:\\Users\\eunchan1\\Desktop\\hello.txt");

        int ch;
        while((ch = reader.read()) != -1) {
            System.out.print((char) ch);
        }
    }
}
