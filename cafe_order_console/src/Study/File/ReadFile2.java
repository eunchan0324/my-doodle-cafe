package Study.File;

import java.io.*;
import java.nio.charset.Charset;

public class ReadFile2 {
    public static void main(String[] args) throws IOException{

        // Java 11 이후
        BufferedReader reader = new BufferedReader(
                new FileReader("C:\\Users\\eunchan1\\Desktop\\hello.txt", Charset.forName("UTF-8"))
        );

        // Java 11 이전
//        BufferedReader reader = new BufferedReader(
//                new InputStreamReader(new FileInputStream("C:\\Users\\eunchan1\\Desktop\\hello.txt"), "UTF-8")
//        );

        String str;
        while ((str = reader.readLine()) != null) {
            System.out.println(str);
        }

        reader.close();
    }
}
