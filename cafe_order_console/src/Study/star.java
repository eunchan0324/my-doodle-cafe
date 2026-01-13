package Study;

import java.util.Scanner;

public class star {
    public static void main(String[] args) {
        // 오른쪽 직각 삼각형 별찍기
//    하드 코딩 : 가로 3 세로 4
//    System.out.println("*");
//    System.out.println("****");
//    System.out.println("*******");
//    System.out.println("**********");

        // 반복문
//    Scanner sc = new Scanner(System.in);
//    System.out.print("직각 삼각형의 오른쪽 가로로 늘어날 길이를 입력하세요 : ");
//    int widthAdd = sc.nextInt();
//    System.out.print("직각 삼각형의 세로 길이를 입력하세요 : ");
//    int length = sc.nextInt();
//
//
//    int totalWidth = 1;
//    for (int i = 1; i <= length; i++) {
//      for (int j = 1; j<= totalWidth; j++) {
//        System.out.print("*");
//      }
//      System.out.println();
//      totalWidth += widthAdd;
//    }


//    // 왼쪽 직각 삼각형 별찍기
        // 하드코딩 : 가로 3 세로 4
//    System.out.println("         *");
//    System.out.println("      ****");
//    System.out.println("   *******");
//    System.out.println("**********");

        // 반복문
//        Scanner sc = new Scanner(System.in);
//        System.out.print("직각 삼각형의 왼쪽 가로로 늘어날 길이를 입력하세요 : ");
//        int widthAdd = sc.nextInt();
//        System.out.print("직각 삼각형의 세로 길이를 입력하세요 : ");
//        int length = sc.nextInt();
//
//
//        int totalGap = (length - 1) * widthAdd;
//        int totalStar = 1;
//        for (int i = 1; i <= length; i++) {
//            for (int j = 1; j <= totalGap; j++) {
//                System.out.print(" ");
//            }
//            totalGap -= widthAdd;
//            for (int k = 1; k <= totalStar; k++) {
//                System.out.print("*");
//            }
//            totalStar += widthAdd;
//            System.out.println();
//        }


        // 직각 삼각형 합치기 = 이등변 삼각형
//    // 하드코딩 가로변화 2 세로변화 4
//    System.out.println("   *   ");
//    System.out.println("  ***  ");
//    System.out.println(" ***** ");
//    System.out.println("*******");

        // 반복문
//    Scanner sc = new Scanner(System.in);
//    System.out.print("삼각형의 가로로 늘어날 길이를 입력하세요 (짝수만) : ");
//    int widthAdd = sc.nextInt();
//    System.out.print("직각 삼각형의 세로 길이를 입력하세요 : ");
//    int length = sc.nextInt();
//
//    // 최종 가로
//    int totalWidth = (length - 1) * widthAdd + 1;
//    // 중앙 별 위치
//    int center = totalWidth / 2 + 1;
//    // 왼쪽 삼각형 공백
//    int totalGap = center - 1;
//    // 초기 중앙 별
//    int totalStar = 1;
//    // 오른쪽 늘어나는 가로 길이
//    int rightAddWidth = 0;
//
//    for (int i = 1; i <= length; i++) {
//      for (int j = 1; j <= totalGap; j++) {
//        System.out.print(" ");
//      }
//      for (int k = 1; k <= totalStar; k++) {
//        System.out.print("*");
//      }
//      for (int l = 1; l <= rightAddWidth; l++) {
//        System.out.print("*");
//      }
//      totalGap -= widthAdd / 2;
//      totalStar += widthAdd / 2;
//      rightAddWidth += widthAdd / 2;
//      System.out.println();
//    }


        // 이등변 삼각형 뒤집기
        // 반복문
//    Scanner sc = new Scanner(System.in);
//    System.out.print("삼각형의 가로로 변화 할 길이를 입력하세요 (짝수만) : ");
//    int widthAdd = sc.nextInt();
//    System.out.print("직각 삼각형의 세로 길이를 입력하세요 : ");
//    int length = sc.nextInt();
//
//
//    // 최종 가로
//    int totalWidth = (length - 1) * widthAdd + 1;
//    // 중앙 별 위치
//    int center = totalWidth / 2 + 1;
//    // 왼쪽 삼각형 공백
//    int totalGap = 0;
//    // 초기 별
//    int totalStar = totalWidth / 2 + 1;
//    // 오른쪽 늘어나는 가로 길이
//    int rightAddWidth = totalWidth / 2;
//
//    for (int i = 1; i <= length; i++) {
//      for (int j = 1; j <= totalGap; j++) {
//        System.out.print(" ");
//      }
//      for (int k = 1; k <= totalStar; k++) {
//        System.out.print("*");
//      }
//      for (int l = 1; l <= rightAddWidth; l++) {
//        System.out.print("*");
//      }
//      totalGap += widthAdd / 2;
//      totalStar -= widthAdd / 2;
//      rightAddWidth -= widthAdd / 2;
//      System.out.println();
//    }


        // 마름모 별찍기
        // 반복문
//        Scanner sc = new Scanner(System.in);
//        System.out.print("마름모의 가로로 변화 할 길이를 입력하세요 (짝수만) : ");
//        int col = sc.nextInt();
//        System.out.print("직각 삼각형의 세로 길이를 입력하세요(홀수만) : ");
//        int length = sc.nextInt();
//
//        // 현재 행 상태
//        int cntCol = 1;
//        // 마름모의 중앙
//        int middleLine = length / 2 + 1;
//        // 최대 가로 길이
//        int middleWidth = (length / 2) * col + 1;
//        // 중앙 별 위치
//        int center = middleWidth / 2 + 1;
//        // 왼쪽 삼각형 공백
//        int totalGap = middleWidth - center;
//        // 왼쪽 삼각형 별 개수 (초기 중앙)
//        int totalStar = 1;
//        // 오른쪽 별 개수
//        int rightAddStar = 0;
//
//        for (int i = 1; i <= length; i++) {
//            // 가로 별찍기 코드
//            for (int j = 1; j <= totalGap; j++) {
//                System.out.print(" ");
//            }
//            for (int j = 1; j <= totalStar; j++) {
//                System.out.print("*");
//            }
//            for (int j = 1; j <= rightAddStar; j++) {
//                System.out.print("*");
//            }
//            // 행 기준 작업 코드
//            if (cntCol < middleLine) {
//                totalGap -= col / 2;
//                totalStar += col / 2;
//                rightAddStar += col / 2;
//                System.out.println();
//            } else {
//                totalGap += col / 2;
//                totalStar -= col / 2;
//                rightAddStar -= col / 2;
//                System.out.println();
//            }
//            cntCol += 1;
//        }


        // 원 별찍기
//        System.out.println("              * * *      ");
//        System.out.println("          * * * * * * *      ");
//        System.out.println("      * * * * * * * * * * *    ");
//        System.out.println("    * * * * * * * * * * * * *  ");
//        System.out.println("  * * * * * * * * * * * * * * *");
//        System.out.println("* * * * * * * * * * * * * * * * *");
//        System.out.println("* * * * * * * * * * * * * * * * *");
//        System.out.println("  * * * * * * * * * * * * * * *");
//        System.out.println("  * * * * * * * * * * * * * * *");
//        System.out.println("    * * * * * * * * * * * * *  ");
//        System.out.println("      * * * * * * * * * * *    ");
//        System.out.println("          * * * * * * *      ");
//        System.out.println("              * * *      ");

        // r = 15 일 때
//        System.out.println("                              *  *  *  *  *  *"); // 가로 5+1(중앙선) 개
//        System.out.println("                        *  *  *  *  *  *  *  *"); // 2 >> 10/5?
//        System.out.println("                  *  *  *  *  *  *  *  *  *  *"); // 3
//        System.out.println("               *  *  *  *  *  *  *  *  *  *  *"); // 4
//        System.out.println("            *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("         *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("      *  *  *  *  *  *  *  *  *  *  *  *  *  *"); // 7
//        System.out.println("      *  *  *  *  *  *  *  *  *  *  *  *  *  *"); // 8
//        System.out.println("   *  *  *  *  *  *  *  *  *  *  *  *  *  *  *"); // 9
//        System.out.println("   *  *  *  *  *  *  *  *  *  *  *  *  *  *  *"); // 10
//        System.out.println("*  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *"); // 11
//        System.out.println("*  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *"); // 12
//        System.out.println("*  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *"); // 13
//        System.out.println("*  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *"); // 14
//        System.out.println("*  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *"); // 15줄 째
//        System.out.println("*  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *"); // 중간 선 /15+1줄
//
//        System.out.println();
//        System.out.println();
//
//
//        System.out.println("                              *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("                        *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("                  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("               *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("            *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("         *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("      *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("      *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("   *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("   *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("*  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("*  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("*  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("*  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("*  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("*  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//
//        System.out.println();
//        System.out.println();
//
//
//        System.out.println("                              *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("                        *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("                  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("               *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("            *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("         *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("      *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("      *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("   *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("   *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("*  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("*  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("*  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("*  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("*  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("*  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("*  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("*  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("*  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("*  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("*  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("   *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("   *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("      *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("      *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("         *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("            *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("               *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("                  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("                        *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("                              *  *  *  *  *  *  *  *  *  *  *");
//
//
//        System.out.println("                              *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("                        *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("                  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("               *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("            *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("         *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("      *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("      *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("   *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("   *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("*  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("*  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("*  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("*  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("*  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("*  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("*  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("*  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("*  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("*  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("*  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("   *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("   *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("      *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("      *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("         *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("            *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("               *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("                  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("                        *  *  *  *  *  *  *  *  *  *  *  *  *  *  *");
//        System.out.println("                              *  *  *  *  *  *  *  *  *  *  *");

        // r = 1
//        System.out.println("   *  ");
//        System.out.println("*  *  *  ");
//        System.out.println("   *  ");
//        System.out.println();
//
//        // r = 2
//        System.out.println("      *  ");
//        System.out.println("   *  *  *  ");
//        System.out.println("*  *  *  *  *  ");
//        System.out.println("   *  *  *  ");
//        System.out.println("      *  ");
//        System.out.println();

        // r = 3 ** 이상부터 입력받자
//        System.out.println("      *  *  *  ");
//        System.out.println("   *  *  *  *  *  ");
//        System.out.println("   *  *  *  *  *  ");
//        System.out.println("*  *  *  *  *  *  *  ");
//        System.out.println("   *  *  *  *  *  ");
//        System.out.println("   *  *  *  *  *  ");
//        System.out.println("      *  *  *  ");
//        System.out.println();

//        System.out.println("      *  *  ");
//        System.out.println("   *  *  *  ");
//        System.out.println("   *  *  *  ");
//        System.out.println("*  *  *  *  ");
//        System.out.println();


        // r = 4
//        System.out.println("         *  *  *  ");
//        System.out.println("      *  *  *  *  *  ");
//        System.out.println("   *  *  *  *  *  *  *  ");
//        System.out.println("   *  *  *  *  *  *  *  ");
//        System.out.println("*  *  *  *  *  *  *  *  *  ");
//        System.out.println("   *  *  *  *  *  *  *  ");
//        System.out.println("   *  *  *  *  *  *  *  ");
//        System.out.println("      *  *  *  *  *  ");
//        System.out.println("         *  *  *  ");
//        System.out.println();

        // r = 5
//        System.out.println("         *  *  *  *  *  ");
//        System.out.println("      *  *  *  *  *  *  *  ");
//        System.out.println("   *  *  *  *  *  *  *  *  *  ");
//        System.out.println("   *  *  *  *  *  *  *  *  *  ");
//        System.out.println("   *  *  *  *  *  *  *  *  *  ");
//        System.out.println("*  *  *  *  *  *  *  *  *  *  *  ");
//        System.out.println("   *  *  *  *  *  *  *  *  *  ");
//        System.out.println("   *  *  *  *  *  *  *  *  *  ");
//        System.out.println("   *  *  *  *  *  *  *  *  *  ");
//        System.out.println("      *  *  *  *  *  *  *  ");
//        System.out.println("         *  *  *  *  *  ");
//        System.out.println();

        // r = 6
//        System.out.println("            *  *  *  *  *  ");
//        System.out.println("         *  *  *  *  *  *  *  ");
//        System.out.println("      *  *  *  *  *  *  *  *  *  ");
//        System.out.println("   *  *  *  *  *  *  *  *  *  *  *  ");
//        System.out.println("   *  *  *  *  *  *  *  *  *  *  *  ");
//        System.out.println("   *  *  *  *  *  *  *  *  *  *  *  ");
//        System.out.println("*  *  *  *  *  *  *  *  *  *  *  *  *  ");
//        System.out.println("   *  *  *  *  *  *  *  *  *  *  *  ");
//        System.out.println("   *  *  *  *  *  *  *  *  *  *  *  ");
//        System.out.println("   *  *  *  *  *  *  *  *  *  *  *  ");
//        System.out.println("      *  *  *  *  *  *  *  *  *  ");
//        System.out.println("         *  *  *  *  *  *  *  ");
//        System.out.println("            *  *  *  *  *  ");
//        System.out.println();

        // r = 7
//        System.out.println("               *  *  *  *  *  ");
//        System.out.println("         *  *  *  *  *  *  *  *  *  ");
//        System.out.println("      *  *  *  *  *  *  *  *  *  *  *  ");
//        System.out.println("      *  *  *  *  *  *  *  *  *  *  *  ");
//        System.out.println("   *  *  *  *  *  *  *  *  *  *  *  *  *  ");
//        System.out.println("   *  *  *  *  *  *  *  *  *  *  *  *  *  ");
//        System.out.println("   *  *  *  *  *  *  *  *  *  *  *  *  *  ");
//        System.out.println("*  *  *  *  *  *  *  *  *  *  *  *  *  *  *  ");
//        System.out.println("   *  *  *  *  *  *  *  *  *  *  *  *  *  ");
//        System.out.println("   *  *  *  *  *  *  *  *  *  *  *  *  *  ");
//        System.out.println("   *  *  *  *  *  *  *  *  *  *  *  *  *  ");
//        System.out.println("      *  *  *  *  *  *  *  *  *  *  *  ");
//        System.out.println("      *  *  *  *  *  *  *  *  *  *  *  ");
//        System.out.println("         *  *  *  *  *  *  *  *  *  ");
//        System.out.println("               *  *  *  *  *  ");
//        System.out.println();

        // r = 8
//        System.out.println("               *  *  *  *  *  *  *  ");
//        System.out.println("            *  *  *  *  *  *  *  *  *  ");
//        System.out.println("         *  *  *  *  *  *  *  *  *  *  *  ");
//        System.out.println("      *  *  *  *  *  *  *  *  *  *  *  *  *  ");
//        System.out.println("   *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  ");
//        System.out.println("   *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  ");
//        System.out.println("   *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  ");
//        System.out.println("   *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  ");
//        System.out.println("*  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  ");
//        System.out.println("   *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  ");
//        System.out.println("   *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  ");
//        System.out.println("   *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  ");
//        System.out.println("   *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  ");
//        System.out.println("      *  *  *  *  *  *  *  *  *  *  *  *  *  ");
//        System.out.println("         *  *  *  *  *  *  *  *  *  *  *  ");
//        System.out.println("            *  *  *  *  *  *  *  *  *  ");
//        System.out.println("               *  *  *  *  *  *  *  ");
//        System.out.println();

//        // 상좌
//        System.out.println("      *  *  ");
//        System.out.println("   *  *  *  ");
//        System.out.println("   *  *  *  ");
//        System.out.println("*  *  *  *  ");
//
//        // 상우
//        System.out.println("*  ");
//        System.out.println("*  *  ");
//        System.out.println("*  *  ");
//        System.out.println("*  *  *  ");
//
//        // 하좌
//        System.out.println("   *  *  *  ");
//        System.out.println("   *  *  *  ");
//        System.out.println("      *  *  ");
//
//        // 하우
//        System.out.println("*  *  ");
//        System.out.println("*  *  ");
//        System.out.println("*  ");


        Scanner sc = new Scanner(System.in);
        System.out.print("반지름을 입력하세요 : ");
        int r = sc.nextInt();

        int fixLength = 1;

        // r이 3일 때
        int gap34 = 2;
        int star34 = 1;
        int rstar = 1;
        int bgap34 = 1;
        int bstar34 = 2;
        int brstar34 = 2;


        if (r == 3) {
            // 상
            for (int i = 1; i <= r + 1; i++) {
                for (int j = 1; j <= gap34; j++) {
                    System.out.print("   ");
                }
                for (int j = 1; j <= star34; j++) {
                    System.out.print("*  ");
                }
                if (i == 1 || i == 3) {
                    gap34 -= 1;
                    star34 += 1;
                }
                for (int j = 1; j <= fixLength; j++) {
                    System.out.print("*  ");
                }
                for (int j = 1; j <= rstar; j++) {
                    System.out.print("*  ");
                }
                if (i == 1 || i == 3) {
                    rstar += 1;
                }
                System.out.println();
            }

            // 하
            for (int i = 1; i <= r; i++) {
                for (int j = 1; j <= bgap34; j++) {
                    System.out.print("   ");
                }
                for (int j = 1; j <= bstar34; j++) {
                    System.out.print("*  ");
                }

                if (i == 2) {
                    bgap34 += 1;
                    bstar34 -= 1;
                }

                for (int j = 1; j <= fixLength; j++) {
                    System.out.print("*  ");
                }

                for (int j = 1; j <= brstar34; j++) {
                    System.out.print("*  ");
                }

                if (i == 2) {
                    brstar34 -= 1;
                }

                System.out.println();
            }
        }


//        int Stars = r / 3; // 5
//        int Gaps = r - (r / 3); // 10
//        int centerStar = 1;
//        int addStars = Stars;
//
//
//        for (int i = 1; i <= r; i++) {
//            for (int j = 1; j <= Gaps; j++) {
//                System.out.print("   ");
//            }
//            for (int j = 1; j <= Stars; j++) {
//                System.out.print("*  ");
//            }
//
//            if (i <= r / Stars) {
//                Gaps -= 2;
//                Stars += 2;
//            } else if (i < r / 2) {
//                Gaps -= 1;
//                Stars += 1;
//            } else if (i < r / 2 + 1) {
//                // 아무일도 없음
//            } else if (i < r / 2 + 2) {
//                Gaps -= 1;
//                Stars += 1;
//            } else if (i < r/2 + 3) {
//                // 아무일도 없음
//            } else if (i < r/2 + 4) {
//                Gaps -= 1;
//                Stars += 1;
//
//            }
//
//            for (int j = 1; j <= centerStar; j++) {
//                System.out.print("*  ");
//            }
//
//            for (int j = 1; j <= addStars; j++) {
//                System.out.print("*  ");
//            }
//
//            System.out.println();
//        }


//
//
//
//
// 별 별찍기 (오각형)
// 하드 코딩
//        System.out.println("                         *"); // 공백 25개
//        System.out.println("                        ***");
//        System.out.println("                       *****");
//        System.out.println("                      *******");
//        System.out.println("                     *********");
//        System.out.println("                    ***********"); // 별 11개
//        System.out.println("***************************************************"); // 51개
//        System.out.println("    *******************************************"); // 43개
//        System.out.println("        ***********************************"); // 35개
//        System.out.println("            ***************************"); // 27개
//        System.out.println("              ***********************"); // 23개
//        System.out.println("                *******************"); // 별 19개
//        System.out.println("               *********************"); // 하단 // 21개
//        System.out.println("              ***********************");
//        System.out.println("             ***********   ***********");
//        System.out.println("            *******             *******");
//        System.out.println("           ***                       ***");
//        System.out.println("          **                           **");
//
//        System.out.println("                     *"); // 공백 21개
//        System.out.println("                    ***");
//        System.out.println("                   *****");
//        System.out.println("                  *******");
//        System.out.println("                 *********");
//        System.out.println("*******************************************"); // 43개
//        System.out.println("    ***********************************");
//        System.out.println("        ***************************");
//        System.out.println("            *******************");
//        System.out.println("              ***************");
//        System.out.println("             *****************");
//        System.out.println("            *******************");
//        System.out.println("           ********** **********");
//        System.out.println("          *******         *******");
//        System.out.println("         ***                   ***");
//
//        System.out.println("                *"); // 공백 16개이지만 15개가 되어야 함
//        System.out.println("               ***");
//        System.out.println("              *****");
//        System.out.println("             *******");
//        System.out.println("*********************************"); // 33개
//        System.out.println("    *************************");
//        System.out.println("        *****************");
//        System.out.println("          *************");
//        System.out.println("         ***************");
//        System.out.println("        ******** ********");
//        System.out.println("       ******       ******");
//        System.out.println("      **                 **");


//        System.out.println("             *"); // 공백 13
//        System.out.println("            ***");
//        System.out.println("           *****"); // 별 5개
//        System.out.println("***************************"); // 27개
//        System.out.println("    *******************");
//        System.out.println("       *************");   // 13개
//        System.out.println("      ******* *******"); // 왼쪽 공백 7 왼쪽 별 6 중앙 공백 1 오른쪽 별 6
//        System.out.println("     ****         ****");
//        System.out.println("    **               **");


// 별찍기 반복문
//        Scanner sc = new Scanner(System.in);
//        System.out.print("(세로)길이를 입력하세요 (4이상, 짝수만) : ");
//        int length = sc.nextInt();
//
//        int gaps = length * 4 + 1;
//        int stars = 1;
//        int addStars = 0;
//
//        // 상단
//        for (int i = 1; i <= length; i++) {
//            for (int j = 1; j <= gaps; j++) {
//                System.out.print(" ");
//            }
//            for (int j = 1; j <= stars; j++) {
//                System.out.print("*");
//            }
//            for (int j = 1; j <= addStars; j++) {
//                System.out.print("*");
//            }
//            gaps -= 1;
//            stars += 1;
//            addStars += 1;
//            System.out.println();
//
//        }
//
//        // 중단
//        int midLeftStars = length * 4 + 2;
//        int midGaps = 0;
//        int midAddStars = midLeftStars - 1;
//
//
//        for (int i = 1; i <= length; i++) {
//            for (int j = 1; j <= midGaps; j++) {
//                System.out.print(" ");
//            }
//            for (int j = 1; j <= midLeftStars; j++) {
//                System.out.print("*");
//            }
//            for (int j = 1; j <= midAddStars; j++) {
//                System.out.print("*");
//            }
//            if (i >= length / 2 + 1) {
//                midGaps += 2;
//                midLeftStars -= 2;
//                midAddStars -= 2;
//            } else {
//                midGaps += 4;
//                midLeftStars -= 4;
//                midAddStars -= 4;
//            }
//            System.out.println();
//        }
//
//        // 하단
//        int botLeftStars = ((midLeftStars + 4) + (midAddStars + 4)) / 2 - 1;
//        int botGaps = midGaps - 3;
//        int botMidGaps = 1;
//        int botAddGaps = 0;
//        int botAddStars = botLeftStars;
//
//        for (int i = 1; i <= length; i++) {
//            for (int j = 1; j <= botGaps; j++) {
//                System.out.print(" ");
//            }
//            for (int j = 1; j <= botLeftStars; j++) {
//                System.out.print("*");
//            }
//            for (int j = 1; j <= botMidGaps; j++) {
//                System.out.print(" ");
//            }
//            for (int j = 1; j <= botAddGaps; j++) {
//                System.out.print(" ");
//            }
//            for (int j = 1; j <= botAddStars; j++) {
//                System.out.print("*");
//            }
//
//            if (i > length / 2 + 1) {
//                botLeftStars -= 1;
//                botAddStars -= 1;
//                botAddGaps += 1;
//            } else {
//                botLeftStars -= 2;
//                botAddStars -= 2;
//                botAddGaps += 3;
//            }
//            botGaps -= 1;
//            botMidGaps += 3;
//
//
//            System.out.println();
//        }


    }
}
