package Study.File;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

// 구분자로 나눠서 읽기
public class Practice4 {
  public static void main(String[] args) throws IOException {

    // 저장
    FileWriter writer = new FileWriter("C:\\Users\\dmsck\\OneDrive\\바탕 화면\\test\\test4.txt");
    writer.write("홍길동,25,학생 \n김철수,24,회사원");
    writer.close(); // 객체를 정리해주는 작업

    // 읽기
    BufferedReader reader = new BufferedReader(
        (new FileReader("C:\\Users\\dmsck\\OneDrive\\바탕 화면\\test\\test4.txt"))
    );

    String line;
    while ((line = reader.readLine()) != null) {
      String[] parts = line.split(",");

      // 길이만큼
//      for (int i = 0; i < parts.length; i++) {
//        System.out.println(parts[i]);
//      }

      // 하드코딩
      System.out.println("이름 : " + parts[0]);
      System.out.println("나이 : " + parts[1]);
      System.out.println("직업 : " + parts[2]);
      System.out.println("------");
    }
    reader.close();
  }
}
