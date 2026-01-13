package Study.Thread.practice;

public class BakeryStep2_RaceCondition {
    static int dough = 0;

    public static void main(String[] args) throws InterruptedException {
        Runnable worker = () -> {
            for (int i = 0; i < 10_000; i++) {
                dough++;
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
        System.out.println("실행시간 : " + (end-start)/1_000_000.0 + "ms");

        System.out.println("예상 반죽량 : 20000");
        System.out.println("실제 반죽량 : " + dough);
    }
}

