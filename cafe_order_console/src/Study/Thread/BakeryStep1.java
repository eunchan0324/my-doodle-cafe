package Study.Thread;

public class BakeryStep1 {
    public static void main(String[] args) {
        // 직원 1 : 반죽 담당
        Runnable runnable = () -> {
            for (int i = 1; i <= 5; i++) {
                System.out.println("반죽 중..." + i + "번째 빵");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
            }
        };
        Thread baker = new Thread(runnable);


        // 직원 2 : 포장 담당
        Thread packer = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                System.out.println("포장 중 ..." + i + "번째 빵");
                try {
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                }
            }
        });

        // 두 직원 동시에 시작
        baker.start();
        System.out.println("베이커 스레드 시작");
        packer.start();
        System.out.println("패커 스레드 시작");

        // 메인 스레드가 직원들이 끝날 때까지 기다림
        try {
            baker.join();
//            packer.join();
        } catch (InterruptedException e) {

        }


        System.out.println("모든 빵 완성!");

    }
}

