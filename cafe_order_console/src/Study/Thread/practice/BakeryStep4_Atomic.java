package Study.Thread.practice;

import java.util.concurrent.atomic.AtomicInteger;

public class BakeryStep4_Atomic {
    static AtomicInteger dough = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        Runnable worker = () -> {
            for (int i = 0; i < 10_000; i++) {
                dough.incrementAndGet(); // 안전한 +1
            }
        };

        Thread workerA = new Thread(worker) ;
        Thread workerB = new Thread(worker);

        workerA.start();
        workerB.start();

        long start = System.nanoTime();

        workerA.join();
        workerB.join();

        long end = System.nanoTime();
        System.out.println("실행시간 : " + (end-start)/1_000_000.0 + "ms");

        System.out.println("기대 반죽량 : 20000");
        System.out.println("실제 반죽량 : " + dough.get());

    }
}
