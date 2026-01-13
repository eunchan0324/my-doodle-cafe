package Study.Thread;

public class BakeryStep3_Synchronized_test {
    static int dough1 = 0; // 공유 반죽통
    static int dough2 = 0; // 공유 반죽통

    public static void main(String[] args) throws InterruptedException {

        Thread workerA = new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                dough1++;
            }
        });
        Thread workerB = new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                dough2++;
            }
        });

        long start = System.nanoTime();

        workerA.start();
        workerB.start();

        workerA.join();
        workerB.join();

        long sum = dough1 + dough2;

        long end = System.nanoTime();
        System.out.println("실행시간 : " + (end - start)/1_000_000.0 + "ms");

        System.out.println("기대 반죽량 : 20000");
        System.out.println("실제 반죽량 : " + sum);
    }
}
