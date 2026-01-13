package Study;

class Updater {
    void update(Counter counter) {
        counter.count++;
    }
}

class Counter {
    int count = 0; // 객체 변수
}

public class Callbyvalue_VS_CallbyReference {
    public static void main(String[] args) {
        Counter myCounter = new Counter(); // 객체 생성
        System.out.println("before update : " + myCounter.count);
        Updater myUpdater = new Updater(); // 객체 생성
        myUpdater.update(myCounter);
        System.out.println("after update : " + myCounter.count);
    }
}
