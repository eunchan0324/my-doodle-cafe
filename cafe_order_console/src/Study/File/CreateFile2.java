package Study.File;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class CreateFile2 {
    public static void main(String[] args) {

        File file = new File("C:\\Users\\eunchan1\\Desktop\\example\\file.txt");

        try {
            // true : 기존 유지
//            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            // false : 덮어쓰기
            FileOutputStream fileOutputStream = new FileOutputStream(file, false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
