package Study.File;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

// 여러 줄 생성,읽기
public class Practice3 {
  public static void main(String[] args) throws IOException {

    // 저장
    FileWriter writer = new FileWriter("C:\\Users\\dmsck\\OneDrive\\바탕 화면\\test\\test1.txt");
    writer.write("hello \nnice to meet you"); // writer라는 스택 메모리 주소에 .을 통하여 접근해서 힙 메모리의 위치로 넘어감
    writer.close(); // 객체를 정리해주는 작업

    // 읽기
    // new 키워드로 힙 메모리에 BufferedReader 인스턴스를 생성했고
    // BufferedReader라는 생성자로 주소를 반환하여 bufferedReader 변수명에 대입
    BufferedReader reader = new BufferedReader(
        (new FileReader("C:\\Users\\dmsck\\OneDrive\\바탕 화면\\test\\test1.txt"))
    );

    // 스택에 있는 reader에 '.' 접근 키워드로 접근하여 힙 메모리 주소 (BufferedReader 객체 속성 사용 가능) 내부로 가서 readLine() 이라는 메서드 사용하여 line String 변수에 주소?? 대입
    String line;
    // readLine()이 한 줄 씩 읽어오는 거니까 line 읽어오기
    // while문 조건에서 reader.readLine()을 매번 호출하며 line에 다음 줄 할당
    while ((line = reader.readLine()) != null) {
      System.out.println(line);
    }
    reader.close();
  }
}
