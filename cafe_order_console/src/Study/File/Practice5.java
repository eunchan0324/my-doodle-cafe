package Study.File;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Practice5 {
    public static void main(String[] args) throws IOException {
        // 생성
        // new 키워드로 인스턴스를 힙 메모리에 생성, 이후 FileWriter라는 생성자를 통해 해당 filename(경로)의 주소를 fileWriter라는 객체 변수에 할당
        FileWriter writer = new FileWriter("C:\\Users\\eunchan1\\Desktop\\filePrac\\test5.txt");

        // 해당 주소를 가진 스택에 있는 writer라는 변수에서 . 키워드를 통해 힙 메모리로 접근 후, FileWriter 객체가 가진 속성인 write를 사용하여 문자 데이터 생성
        writer.write("hello \nnice to meet you \nthank you");
        writer.close();

        // 읽기
        // FileReader를 통해 지정한 경로의 파일을 열고,
        // BufferedReader로 감싸서 줄 단위로 효율적으로 읽을 수 있도록 함
        BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\eunchan1\\Desktop\\filePrac\\test5.txt"));

        // 한 줄씩 읽은 문자열을 저장할 String 변수 선언
        String line;
        // read.readLine()을 호출해 한 줄씩 읽어 line 변수에 저장
        // 더 이상 읽을 줄이 없다면 readLine()은 null을 반환하여 while문 종료
        while ((line = reader.readLine()) != null) {
            System.out.println((line)); // 읽은 한 줄을 그대로 출력
        }
    }
}
