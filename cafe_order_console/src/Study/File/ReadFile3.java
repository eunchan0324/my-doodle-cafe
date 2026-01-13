package Study.File;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class ReadFile3 {
    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(new File("C:\\Users\\eunchan1\\Desktop\\hello.txt"));

        while (scanner.hasNext()) {
            String str = scanner.next();
            System.out.println(str);
        }
    }
}
