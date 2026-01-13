package backend.menu;

import java.util.ArrayList;
import java.util.Scanner;

// 나만의 메뉴 (찜하기) 기능
public class MyMenu {
    public ArrayList<Menu> myMenu = new ArrayList<>();
    Scanner sc = new Scanner(System.in);

    // 나만의 메뉴 등록하기
    public void CreateMyManu(MenuList menuList) {
        boolean hasMyMenu = false;
        System.out.println();
        System.out.println("[나만의 메뉴 등록하기]");
        menuList.showAllMenus();
        System.out.print("메뉴 목록 중, 나만의 메뉴로 등록할 메뉴 명을 입력해주세요 : ");
        String 입력한메뉴명 = sc.nextLine();
        Menu 찾은메뉴 = menuList.findMenu(입력한메뉴명);

        // 중복체크
        // todo : 메뉴의 동등성 비교 학습 (이 코드를 다르게 하는 방법)
        for (int i = 0; i < myMenu.size(); i++) {
            if (입력한메뉴명.equals(myMenu.get(i).getName())) {
                hasMyMenu = true;
            }
        }

        // 유효성 체크 - 실제로 메뉴가 존재하는가?
        if (찾은메뉴 != null && hasMyMenu == false) {
            myMenu.add(찾은메뉴);
            System.out.println("등록이 완료되었습니다.");
            System.out.println();
        } else {
            System.out.println("메뉴명이 정확하지 않습니다. 다시 입력해주세요.");
        }

        if (hasMyMenu == true) {
            System.out.println("이미 존재하는 나만의 메뉴입니다.");
            System.out.println();
        }
    }

    // 찜한 메뉴 보기
    public void ReadMyMenu() {
        System.out.println();
        System.out.println("[나만의 메뉴 목록]");

        if (myMenu.isEmpty()) {
            System.out.println("등록된 나만의 메뉴가 없습니다.");
        } else {
            for (int i = 0; i < myMenu.size(); i++) {
                System.out.println(myMenu.get(i));
            }
        }
        System.out.println();
    }

    // 찜 취소하기
    public void DeleteMyMenu() {
        System.out.println();
        System.out.println("[나만의 메뉴 삭제]");
        if (myMenu.isEmpty()) {
            System.out.println("등록된 나만의 메뉴가 없습니다.");
        } else {
            for (int i = 0; i < myMenu.size(); i++) {
                System.out.println(myMenu.get(i));
            }

            System.out.print("삭제할 나만의 메뉴명을 입력해주세요 : ");
            String 삭제할메뉴명 = sc.nextLine();

            for (int i = 0; i < myMenu.size(); i++) {
                if (삭제할메뉴명.equals(myMenu.get(i).getName())) {
                    myMenu.remove(i);
                    System.out.println("입력한 나만의 메뉴가 삭제되었습니다.");
                    System.out.println();
                } else {
                    System.out.println("삭제할 메뉴명이 정확하지 않습니다. 다시 입력해주세요.");
                    System.out.println();
                }
            }

        }
    }
}
