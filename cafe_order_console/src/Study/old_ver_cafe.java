package Study;

import java.util.Scanner;
import java.time.LocalDate;
import java.time.DayOfWeek;



public class old_ver_cafe {
    public static void main(String[] args) {
        // 시뮬레이터 시작, 역할 선택
        System.out.println("안녕하세요 웨일카페 시뮬레이터입니다. \n" +
                "1. 가게" + System.lineSeparator() +
                "2. 손님");

        // 역할 입력 받기
        Scanner sc = new Scanner((System.in));
        System.out.print("원하는 역할의 번호를 선택해주세요 : ");

        // 입력받은 역할의 번호를 역할 변수에 저장하기
        int role = sc.nextInt();


        // 역할에 따라서 가게, 손님 if 나누기
        // 사장님이라면?
        if (role == 1) {

            // 가게라면, 사장과 알바의 역할이 나뉘어지고, 선택
            System.out.println("가게를 선택하였습니다." + System.lineSeparator() +
                    "1. 사장" + System.lineSeparator() +
                    "2. 알바");

            Scanner sc1 = new Scanner((System.in));
            System.out.print("추가 역할의 번호를 입력해주세요 : ");

            int role1 = sc1.nextInt();

            // 오늘의 날짜 가져오기
            LocalDate today = LocalDate.now();
            DayOfWeek dayOfWeek = today.getDayOfWeek();

            // 사장일 때
            if (role1 == 1) {

                System.out.println("안녕하세요 사장님! 오늘은 " + today + "(" + dayOfWeek + ") 입니다.");


                // 하는 일 (매일)
                System.out.println("[오늘의 할 일]" + System.lineSeparator() +
                        "1. 온라인 스토어 원두 주문 배송" + System.lineSeparator() +
                        "2. 주문 들어온 메뉴 만들기");

                // 하는 일 (주간) * 임시로 화요일로 설정
                if (dayOfWeek == DayOfWeek.TUESDAY) {
                    System.out.println("3. 원두 로스팅");
                }

                // 알바일 떄
            } else if (role1 == 2) {
                System.out.println("안녕하세요 알바님! 오늘은 " + today + "(" + dayOfWeek + ") 입니다.");

                System.out.println("[오늘의 할 일]" + System.lineSeparator() +
                        "1. 주문 들어온 메뉴 만들기");

                if (dayOfWeek == DayOfWeek.TUESDAY) {
                    System.out.println("2. 매장, 창문 청소하기");
                }

            }


        }

        // 손님이라면?
        else if (role == 2) {
            // 메뉴,가격 변수 저장
            String menu1 = "아메리카노";
            int price1 = 4000;

            String menu2 = "라떼";
            int price2 = 4500;

            String menu3 = "녹차";
            int price3 = 5000;

            // 온도 변수 저장
            String temp1 = "HOT";
            String temp2 = "ICE";
            String finalTemp = "";

            // 최종 메뉴, 옵션, 가격 저장
            String finalMenu = "";
            int finalPrice = 0;
            String finalOption = "";

            // 메뉴 선택 문구 출력
            System.out.println("안녕하세요 손님, 웨일 Cafe 입니다.");
            System.out.println("1. " + menu1 + " - " + price1 + "원");
            System.out.println("2. " + menu2 + " - " + price2 + "원");
            System.out.println("3. " + menu3 + " - " + price3 + "원");

            // 오늘의 추천 메뉴
            printTodaySpecial();

            // 메뉴 입력 받기
            Scanner sc1 = new Scanner((System.in));
            System.out.print("원하는 메뉴의 번호를 입력하세요 : ");

            // 입력한 메뉴를 최종 메뉴, 가격 변수로 저장하기
            int menuInput = sc1.nextInt(); // 숫자

            if (menuInput == 1) {
                finalMenu = menu1;
                finalPrice = price1;

                System.out.println("샷 옵션을 선택해주세요");
                System.out.println("1.기본(2샷) " + System.lineSeparator() +
                        "2.연하게(1샷)" + System.lineSeparator() +
                        "3.샷추가(3샷) + 500원" + System.lineSeparator() +
                        "4.디카페인 + 1000원");

                Scanner sc2 = new Scanner(System.in);
                System.out.print("원하는 옵션을 선택해주세요 : ");
                int optionInput = sc2.nextInt();
                if (optionInput == 1) {
                    finalOption = "기본(2샷)";
                } else if (optionInput == 2) {
                    finalOption = "연하게(1샷)";
                } else if (optionInput == 3) {
                    finalOption = "샷추가(3샷)";
                    finalPrice += 500;
                } else if (optionInput == 4) {
                    finalOption = "디카페인";
                    finalPrice += 1000;
                }


            } else if (menuInput == 2) {
                finalMenu = menu2;
                finalPrice = price2;

                System.out.println("우유 종류를 선택해주세요");
                System.out.println("1. 일반 우유" + System.lineSeparator() +
                        "2. 두유 +500원" + System.lineSeparator() +
                        "3. 무지방 우유 +300원");

                Scanner sc3 = new Scanner(System.in);
                System.out.print("원하는 옵션을 선택해주세요 : ");
                int optionInput = sc3.nextInt();
                if (optionInput == 1) {
                    finalOption = "일반 우유";
                } else if (optionInput == 2) {
                    finalOption = "두유";
                    finalPrice += 500;
                } else if (optionInput == 3) {
                    finalOption = "무지방 우유";
                    finalPrice += 300;
                }


            } else if (menuInput == 3) {
                finalMenu = menu3;
                finalPrice = price3;

                System.out.println("밀크티로 변경하시겠습니까?");
                System.out.println("1. 아니요 (기본 녹차)" + System.lineSeparator() +
                        "2. 네 (밀크티로 변경 + 500원)");

                Scanner sc4 = new Scanner(System.in);
                System.out.print("원하는 옵션을 선택해주세요 : ");
                int optionInput = sc4.nextInt();

                if (optionInput == 1) {
                    finalOption = "기본 녹차";
                } else if (optionInput == 2) {
                    finalOption = "녹차 밀크티";
                    finalPrice += 500;
                }

            } else {
                System.out.println("메뉴에 존재하는 않는 번호입니다. 알맞는 메뉴를 입력해주세요. (ex:1~3)");
                System.out.println("프로그램을 종료합니다.");
                return;
            }

            System.out.println("온도를 선택해주세요");
            System.out.println("1. " + temp1);
            System.out.println("2. " + temp2);

            Scanner sc5 = new Scanner((System.in));
            System.out.print("원하는 온도를 선택해주세요 : ");
            int tempInput = sc5.nextInt();

            if (tempInput == 1) {
                finalTemp = temp1;
            } else if (tempInput == 2) {
                finalTemp = temp2;
            }

            // 최중 주문 출력
            System.out.println("[최종 주문]" + System.lineSeparator() +
                    "메뉴 : " + finalMenu + " - " + finalOption + System.lineSeparator() +
                    "온도 : " + finalTemp + System.lineSeparator() +
                    "최종 가격 : " + finalPrice + "원 입니다.");

        }


    }
    public static void printTodaySpecial() {
        DayOfWeek day = LocalDate.now().getDayOfWeek();

        switch (day) {
            case MONDAY -> System.out.println("월요일 추천 메뉴 : 따뜻한 아메리카노");
            case TUESDAY -> System.out.println("화요일 추천 메뉴 : 달달한 라떼");
            case WEDNESDAY -> System.out.println("수요일 추천 메뉴 : 상큼한 아이스티");
            case THURSDAY -> System.out.println("목요일 추천 메뉴 : 디카페인 커피");
            case FRIDAY -> System.out.println("금요일 추천 메뉴 : 초코 라떼");
            default -> System.out.println("주말 추천 : 시그니처 메뉴 전부 할인!");
        }
    }
}
