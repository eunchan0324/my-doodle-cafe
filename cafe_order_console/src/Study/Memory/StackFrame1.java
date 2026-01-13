package Study.Memory;

public class StackFrame1 {

    public static int sum(int a, int b) { // a,b는 지역 변수 (매개변수)
        int c = a + b; // c는 지역 변수
        return c;
    }

    public static void main(String[] args) {
        int x = 10; // 지역 변수
        int y = 20; // 지역 변수
        int z = sum(x, y); // 지역 변수

        System.out.println(z); // 30
    }
}
