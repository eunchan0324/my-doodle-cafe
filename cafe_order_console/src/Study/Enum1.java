//package Study;
//
//import java.util.Scanner;
//
//enum Test {
//    SIGNUP(1, "회원가입"), LOGIN(2, "로그인"), EXIT(3, "프로그램종료");
//
//    private final int value;
//    private final String message;
//
//    Test(int value, String message) {
//        this.value = value;
//        this.message = message;
//    }
//
//    public int getValue() {
//        return value;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public static Test fromValue(int value) {
//        for (Test test : Test.values()) {
//            if (test.getValue() == value) {
//                return test;
//            }
//        }
//        return null;
//    }
//}
//
//public class Enum1 {
//    public static void main(String[] args) {
//        Scanner sc = new Scanner(System.in);
//
//
//        System.out.println(Test.SIGNUP.getValue() + ". " + Test.SIGNUP.getMessage());
//        System.out.println("2. 로그인");
//        System.out.println("3. 프로그램 종료");
//        System.out.print("메뉴를 선택해주세요 : ");
//        int loginChoice = sc.nextInt();
//        sc.nextLine();
//
//        // switch 문으로 리팩토링 하기 동등비교( ==1,2,3)
//        Test test = Test.fromValue(loginChoice);
//        switch (test) {
//            case Test.SIGNUP:
//                userList.registerUser();
//                continue;
//            case Test.LOGIN:
//                User 로그인한사용자 = userList.login();
//
//                role = switch (로그인한사용자.getRole()) {
//                    case ADMIN -> 0;
//                    case SELLER -> 1;
//                    case CUSTOMER -> 2;
//                };
//
//                switch (로그인한사용자.getRole()) {
//                    case UserRole.STORE_MANAGER:
//                        role = 1;
//                        break;
//                    case UserRole.CUSTOMER:
//                        role = 2;
//                        break;
//                }
//
//                if (UserRole.STORE_MANAGER == 로그인한사용자.getRole()) { // 조건문이 서로 변경되어야 가독성이 증가함
//                    role = 1;
//                } else if (UserRole.CUSTOMER == 로그인한사용자.getRole()) {
//                    role = 2;
//                }
//                break;
//            case Test.EXIT:
//                System.out.println("프로그램이 종료되었습니다.");
//                break;
//            default:
//                assert true : "잘못된 입력";
//        }
//    }
//}
