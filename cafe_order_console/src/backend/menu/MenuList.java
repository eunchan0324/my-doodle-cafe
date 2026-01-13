package backend.menu;

import backend.constant.Constants;
import backend.store.StoreList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

// todo : 1. 각 기능별 role에 따라 인가 가능한 것 체크하기 + 일단 User에 있는 맴버 변수를 가져와서 비교해보기
// todo : 2. 인가 기능을 따로 뺄 수 있는지 확인해보기
public class MenuList {
    private final ArrayList<Menu> menus = new ArrayList<>();
    private MenuStatusList menuStatusList;
    private StoreList storeList;

    // menus getter
    public ArrayList<Menu> getMenus() {
        return menus;
    }

    public int getMenuCount() {
        return menus.size();
    }

    // 생성자
    public MenuList(MenuStatusList menuStatusList, StoreList storeList) throws IOException {
        this.menuStatusList = menuStatusList;
        this.storeList = storeList;
        loadFile();
    }

    // Menu Create (CLI)
    public void menuCreate() throws IOException {
        Scanner sc = new Scanner(System.in);

        System.out.print("메뉴 이름을 작성해주세요 : ");
        String menuName = sc.nextLine();

        System.out.print("메뉴의 가격을 입력해주세요 : ");
        int menuPrice = sc.nextInt();
        sc.nextLine();

        System.out.println("메뉴의 종류를 선택해주세요");
        System.out.println("  1. 커피류(coffee)");
        System.out.println("  2. 라떼류(latte)");
        System.out.println("  3. 차류(tea)");
        System.out.print(" : ");
        int menuOptionNum = sc.nextInt();
        sc.nextLine();

        MenuCategory menuOption = MenuCategory.fromValue(menuOptionNum);
        if (menuOption == null) {
            System.out.println("잘못된 번호입니다.");
            return;
        }
        Menu menu = new Menu(menuName, menuPrice, menuOption);
        menus.add(menu);

        saveFile();

        System.out.println("등록이 완료되었습니다.");
        System.out.println();
    }

    // Menu Read - 관리자용
    public void showAllMenus() {
        // 메뉴가 아무것도 등록되지 않았다면
        if (menus.isEmpty()) {
            System.out.println("등록된 메뉴가 없습니다. 메인 메뉴로 돌아갑니다.");
            System.out.println();
        }

        // 메뉴가 1개 이상 등록되어있다면
        System.out.println("[\n 현재 등록된 전체 메뉴 목록]");
        for (Menu menu : menus) {
            String menuinfo = "- 이름 : " + menu.getName() +
                    " | 가격 : " + menu.getPrice() + "원" +
                    " | 카테고리 : " + menu.getOption().name() +
                    " | ID : " + menu.getId().toString();
            System.out.println(menuinfo);
        }
        System.out.println("---------------------------------");
    }

    // Menu Read - 구매자용
    public ArrayList<Menu> showAndGetOrderableMenus(int storeId) {
        ArrayList<Menu> orderableMenus = new ArrayList<>();
        for (Menu menu : menus) {
            if (menuStatusList.isAvailable(storeId, menu.getId())) {
                orderableMenus.add(menu);
            }
        }

        // 메뉴가 아무것도 등록되지 않았다면
        if (orderableMenus.isEmpty()) {
            System.out.println("\n현재 주문 가능한 메뉴가 없습니다.");
            return orderableMenus; // 비어있는 리스트 반환
        }

        // 메뉴가 1개 이상 등록되어있다면
        System.out.println("\n--- 주문 가능한 메뉴 목록 ---");
        for (int i = 0; i < orderableMenus.size(); i++) {
            Menu menu = orderableMenus.get(i);
            System.out.println((i + 1) + ". " + menu.getName() + " | " + menu.getPrice() + "원");
        }
        System.out.println("----------------------------");

        // 주문 가능한 메뉴만 담긴 리스트를 반환
        return orderableMenus;
    }

    // 판매 가능한 메뉴 목록을 보여주고, 그 목록을 반환
    public ArrayList<Menu> showAndGetSellableMenus(int storeId) {
        String storeName = storeList.findStoreById(storeId).getStoreName();
        ArrayList<Menu> sellableMenus = new ArrayList<>();

        System.out.println("\n[" + storeName + "] 지점 판매 메뉴 목록");

        // 1. 판매 가능한 메뉴만 필터링해서 sellableMenus리스트에 추가
        for (Menu menu : menus) {
            MenuStatus status = menuStatusList.findMenuStatus(storeId, menu.getId());
            // 판매중(AVAILABLE) 상태인 메뉴만 대상으로 함
            if (status != null && status.getStatus() == EMenuSaleStatus.AVAILABLE) {
                sellableMenus.add((menu));
            }
        }

        // *판매중인 메뉴가 없을 때
        if (sellableMenus.isEmpty()) {
            System.out.println("현재 판매중인 메뉴가 없습니다.");
            return sellableMenus; // 비어있는 리스트 반환
        }

        // 2. 필터링된 메뉴 목록을 '임시 번호'와 함께 출력
        for (int i = 0; i < sellableMenus.size(); i++) {
            Menu menu = sellableMenus.get(i);
            MenuStatus status = menuStatusList.findMenuStatus(storeId, menu.getId()); // 재고를 가져오기 위해 다시 찾음
            System.out.println((i + 1) + ". " + menu.getName() +
                    " | 재고 : " + status.getStock() +
                    " | 상태 : " + status.getStatus().getDisplayStatus());
        }
        System.out.println("---------------------------------");

        // 3. 내용이 채워진 리스트 반환
        return sellableMenus;


    }

    // Menu Stock Read - 재고 확인 판매자용
    public void showStockStatusForSeller(int storeId) {
        showAndGetSellableMenus((storeId));
    }

    // Menu Update (CLI)
    public void menuEdit(Menu 수정할메뉴) throws IOException {
        Scanner sc = new Scanner(System.in);
        boolean checker = false;

        for (int i = 0; i < menus.size(); i++) {
            if (menus.get(i).getName().equals(수정할메뉴)) {
                System.out.println("수정할 메뉴는 \"" + menus.get(i) + "\" 입니다.");
                System.out.println();

                System.out.print("수정할 항목의 번호를 입력해주세요 (1.메뉴명 2.가격 3.옵션) : ");
                int 수정할항목 = sc.nextInt();


                if (수정할항목 == 1) {
                    sc.nextLine();
                    System.out.print("수정할 메뉴명을 입력해주세요 : ");
                    String 수정할메뉴명 = sc.nextLine();

                    menus.get(i).setName(수정할메뉴명);
                    System.out.println("메뉴명이 수정되었습니다.");
                    System.out.println();
                }

                if (수정할항목 == 2) {
                    sc.nextLine();
                    System.out.print("수정할 가격을 입력해주세요 (숫자만) : ");
                    int 수정할가격 = sc.nextInt();

                    menus.get(i).setPrice(수정할가격);
                    System.out.println("메뉴 가격이 수정되었습니다.");
                    System.out.println();
                }

                if (수정할항목 == 3) {
                    sc.nextLine();
                    System.out.println("옵션은 다음과 같습니다");
                    System.out.println("  1. 커피류(coffee)\n" + "  2. 라떼류(latte)\n" + "  3. 차류(tea)");
                    System.out.print("수정할 옵션을 입력해주세요 (숫자만) : ");
                    int 수정할옵션 = sc.nextInt();
                    sc.nextLine();

                    MenuCategory menuOption = MenuCategory.fromValue(수정할옵션);
                    menus.get(i).setOption(menuOption);
                    System.out.println("메뉴 옵션이 수정되었습니다.");
                    System.out.println();
                }


                checker = true;
                if (checker) {
                    saveFile();
                }
                break;
            }
        }

        if (checker == false) {
            System.out.println("입력한 메뉴명이 정확하지 않습니다. 다시 입력해주세요.");
        }
    }

    // Menu Delete (CLI)
    public void menuDelete(Menu 삭제할메뉴) throws IOException {
        boolean checker = false;

        for (int i = 0; i < menus.size(); i++) {
            if (menus.get(i).getName().equals(삭제할메뉴)) {
                menus.remove(i);
                System.out.println("선택한 메뉴가 삭제되었습니다.");
                checker = true;
                break;
            }
        }

        if (checker) {
            saveFile();
        }

        if (!checker) {
            System.out.println("입력한 메뉴명이 정확하지 않습니다. 다시 입력해주세요.");
        }

    }

    // Menu Empty Check
    public boolean menuIsEmpty() {
        if (menus.isEmpty()) {
            return true;
        }
        return false;
    }

    // find Menu - 매개변수와 같은 이름의 _객체 반환
    public Menu findMenu(String 주문할메뉴) {
        for (Menu menu : menus) {
            if (menu.getName().equals(주문할메뉴)) {
                return menu;
            }
        }
        return null;
    }

    // Menu 파일 save (Menus.txt)
    public void saveFile() throws IOException {
        Path menuFilePath = Constants.BASE_PATH.resolve("Menus.txt");
        FileWriter writer = new FileWriter(menuFilePath.toFile());

        for (Menu menu : menus) {
            // recommend가 null이면 빈 문자열로 저장
            String recommendStr = (menu.getRecommend() != null) ? menu.getRecommend() : "";

            writer.write(menu.getId().toString() + "," +
                    menu.getName() + "," +
                    menu.getPrice() + "," +
                    menu.getOption().name() + "," +
                    recommendStr + "\n");
        }
        writer.close();
    }

    // Menu 파일 load (Menus.txt)
    public void loadFile() throws IOException {
        Path menuFilePath = Constants.BASE_PATH.resolve("Menus.txt");
        BufferedReader reader = new BufferedReader(new FileReader(menuFilePath.toFile()));

        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            UUID id = UUID.fromString(parts[0]);
            String name = parts[1];
            int price = Integer.parseInt(parts[2]);
            MenuCategory option = MenuCategory.valueOf(parts[3]);
            String recommend = null;

            if (parts.length > 4 && !parts[4].isEmpty()) {
                recommend = parts[4];
            }

            Menu menu = new Menu(id, name, price, option);
            menu.setRecommend(recommend);
            menus.add(menu);

        }
        reader.close();
    }

    // menuRecommend create
    public void menuRecommend() throws IOException {
        System.out.println();
        System.out.println("[추천 메뉴 등록 및 관리]");

        showAllMenus();

        System.out.print("등록된 메뉴 중, 추천 메뉴를 입력해주세요 : ");
        Scanner sc = new Scanner(System.in);

        String 추천메뉴명 = sc.nextLine();
        if (findMenu(추천메뉴명) != null) {
            System.out.println(findMenu(추천메뉴명).getName() + "를 선택하였습니다. 추천 이유를 번호로 선택해주세요");
            System.out.println("1. Best 메뉴 선정");
            System.out.println("2. New 메뉴 선정");
            System.out.print(" : ");

            int 추천이유 = sc.nextInt();
            sc.nextLine();

            Menu 찾은메뉴 = findMenu(추천메뉴명);
            // 추천 이유를 현 객체의 Recommend 필드에 추가
            // todo : Enum으로 리팩토링
            if (추천이유 == 1) {
                찾은메뉴.setRecommend("Best");
            } else if (추천이유 == 2) {
                찾은메뉴.setRecommend("New");
            }


        } else {
            System.out.println("메뉴명이 정확하지 않습니다. 메뉴명을 정확하게 입력해주세요");
        }
    }

    // menuRecommend read
    public void menuRecommendRead() {
        System.out.println("[오늘의 추천 메뉴]");
        boolean hasBestMenu = false;
        boolean hasNewMenu = false;
        boolean checker = false;

        for (int i = 0; i < menus.size(); i++) {
            if ("Best".equals(menus.get(i).getRecommend())) {
                hasBestMenu = true;
                checker = true;
            }
        }

        if (hasBestMenu == true) {
            System.out.println("[Best backend.menu.Menu]");
            for (int i = 0; i < menus.size(); i++) {
                if ("Best".equals(menus.get(i).getRecommend())) {
                    System.out.println("- " + menus.get(i).getName());
                }
            }
            System.out.println();
        }

        for (int i = 0; i < menus.size(); i++) {
            if ("New".equals(menus.get(i).getRecommend())) {
                hasNewMenu = true;
                checker = true;
            }
        }

        if (hasNewMenu == true) {
            System.out.println("[New backend.menu.Menu]");
            for (int i = 0; i < menus.size(); i++) {
                if ("New".equals(menus.get(i).getRecommend())) {
                    System.out.println("- " + menus.get(i).getName());
                }
            }
            System.out.println();
        }

        if (checker == false) {
            System.out.println("- 오늘의 추천 메뉴가 없습니다.");
            System.out.println();
        }
    }

    // menuId로 해당 객체 반환하기
    public Menu getMenuById(UUID menuId) {
        for (Menu menu : menus) {
            if (menu.getId().equals(menuId)) {
                return menu;
            }
        }
        return null;
    }

    // 지점별 메뉴 목록 관리
    public ArrayList<Menu> showAndGetManageableMenus(int storeId) {
        String storeName = storeList.findStoreById(storeId).getStoreName();
        System.out.println("\n[" + storeName + "] 지점 전체 메뉴 목록");

        // 메뉴가 하나도 없다면
        if (menus.isEmpty()) {
            System.out.println("현재 관리할 수 있는 메뉴가 없습니다.");
            return new ArrayList<Menu>();
        }

        for (int i = 0; i < menus.size(); i++) {
            Menu menu = menus.get(i);
            MenuStatus status = menuStatusList.findMenuStatus(storeId, menu.getId());
            String saleStatus = (status != null && status.getStatus() == EMenuSaleStatus.AVAILABLE) ? "[판매중]" : "[미판매중]";

            System.out.println((i + 1) + ". " + menu.getName() + " " + saleStatus);
        }
        System.out.println("---------------------------------");
        return menus;
    }

    // 판매자 : 판매 메뉴 전체 read (보여주는 용도) / 추후 사용 가능성
    public void showManageableMenuList(int storeId) {
        showAndGetManageableMenus(storeId);
    }

    // 관리자 : 임시 번호 사용하여 메뉴 선택하여 반환
    public ArrayList<Menu> showAndGetAllMenus() {
        System.out.println("\n --- 현재 등록된 전체 메뉴 목록 ---");
        if (menuIsEmpty()) {
            System.out.println("등록된 메뉴가 없습니다.");
            return new ArrayList<Menu>();
        }

        for (int i = 0; i < menus.size(); i++) {
            Menu menu = menus.get(i);
            System.out.println((i + 1) + ". " + menu.getName() + " | " + menu.getPrice() + "원");
        }
        System.out.println("---------------------------------");
        return menus;
    }

    /**
     * GUI
     */
    public ArrayList<Menu> getManageableMenus() {
        return new ArrayList<>(menus);
    }

    // Menu 생성 (GUI)
    public void menuCreate(String name, int price, MenuCategory category) throws IOException {
        // 새 Menu 객체 생성 (UUID는 자동 생성됨)
        Menu newMenu = new Menu(name, price, category);

        // 리스트에 추가
        menus.add(newMenu);

        // 파일 저장
        saveFile();
    }

    // Menu Update (GUI)
    public void menuEdit(UUID menuId, String newName, int newPrice, MenuCategory newCategory) throws IOException {
        // 1. 수정할 메뉴를 UUID로 찾기
        Menu targetMenu = null;
        for (Menu menu : menus) {
            if (menu.getId().equals(menuId)) {
                targetMenu = menu;
                break;
            }
        }

        // 2. 찾았으면 정보 업데이트 *못찾는 경우는 JTable에서 선택했기 때문에 거의 발생하지 않음
        if (targetMenu != null) {
            targetMenu.setName(newName);
            targetMenu.setPrice(newPrice);
            targetMenu.setOption(newCategory);

            // 3. 파일에 저장
            saveFile();
        }
    }

    // Menu Delete (GUI)
    public void menuDelete(UUID menuId) throws IOException {
        // 1. menus 리스트에서 해당 UUID를 가진 Menu 객체를 찾아 제거
        Menu menuToRemove = null;
        for (Menu menu : menus) {
            if (menu.getId().equals(menuId)) {
                menuToRemove = menu;
                break;
            }
        }

        if (menuToRemove != null) {
            menus.remove(menuToRemove);
        }

        // 2. 파일 저장
        saveFile();
    }


}
