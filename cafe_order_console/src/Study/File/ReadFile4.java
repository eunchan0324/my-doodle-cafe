package Study.File;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;


public class ReadFile4 {
    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(new File("C:\\Users\\eunchan1\\Desktop\\hello.txt"));

        while (scanner.hasNextLine()) {
            String str = scanner.nextLine();
            System.out.println(str);
        }
    }
}
