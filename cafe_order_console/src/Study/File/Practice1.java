package Study.File;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

// 문자열 하나 저장/읽기
public class Practice1 {
    public static void main(String[] args) throws IOException {

        // 저장
        FileWriter writer = new FileWriter("C:\\Users\\eunchan1\\Desktop\\filePrac\\file.dat");
        writer.write("안녕하세요"); // writer라는 스택 메모리 주소에 .을 통하여 접근해서 힙 메모리의 위치로 넘어감
//        System.out.println(writer);
        writer.close(); // 객체를 정리해주는 작업

        // 읽기
        FileReader reader = new FileReader("C:\\Users\\eunchan1\\Desktop\\filePrac\\file.dat");
        int ch;
        while ((ch = reader.read()) != -1) {
            System.out.print(ch + " ");
        }
        reader.close();

    }
}
