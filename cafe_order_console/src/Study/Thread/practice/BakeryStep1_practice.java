package Study.Thread.practice;

public class BakeryStep1_practice {
    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = () -> {
            for (int i = 1; i <= 5; i++) {
                System.out.println("반죽중..." + i + "번째 빵");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
            }
        };

        Thread baker = new Thread(runnable);

        Thread packer = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                System.out.println("포장중..." + i + "번째 빵");
                try {
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                }
            }
        });

        baker.start();
        System.out.println("베이커 스레드 시작");
        packer.start();
        System.out.println("페커 스레드 시작");

        baker.join();
        packer.join();

        System.out.println("모든 빵 완성");
    }
}
