package Study.Memory;

public class MethodPrac1 {
    public static int s = 10; // 클래스 변수

    public static void main(String[] args) {
        int a = 5; // 지역 변수
        int b = 5; // 지역 변수
        int result1 = a + b + MethodPrac1.s; // 지역 변수
        System.out.println(result1); // 20

        Counter sub = new Counter(); // 지역 변수
        twice(sub);
        int result2 = sub.get(); // 지역 변수
        System.out.println(result2);
    }
    public static void twice(Counter c) { // 매개변수 c도 지역 변수
        c.plus(10);
        c.plus(20);
    }
}

class Counter {
    public int state = 50; // 인스턴스 변수
    public final int count = 20; // 인스턴스 변수 (상수형)

    public int get() {
        return state + count; // state, count는 인스턴스 변수
    }

    public void plus(int n) { // 매개변수 n은 지역 변수
        state += n;
    }
}