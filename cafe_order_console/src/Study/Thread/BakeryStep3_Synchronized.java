package Study.Thread;

public class BakeryStep3_Synchronized {
    static int dough = 0; // 공유 반죽통

    // 반죽을 추가하는 동기화된 메서드
    public static synchronized void addDough() {
        dough++; // 이 블록은 한 번에 한 스레드만 실행 가능
    }

    public static void main(String[] args) throws InterruptedException {
        Runnable worker = () -> {
            for (int i = 0; i < 10_000; i++) {
                addDough(); // 안전하게 증가
            }
        };

        Thread workerA = new Thread(worker);
        Thread workerB = new Thread(worker);

        long start = System.nanoTime();

        workerA.start();
        workerB.start();

        workerA.join();
        workerB.join();

        long end = System.nanoTime();
        System.out.println("실행시간 : " + (end - start)/1_000_000.0 + "ms");

        System.out.println("기대 반죽량 : 20000");
        System.out.println("실제 반죽량 : " + dough);
    }
}
