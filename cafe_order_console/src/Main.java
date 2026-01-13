import backend.menu.*;
import backend.order.*;
import backend.store.*;
import backend.user.*;

import frontend.cli.CliClientUi;
import frontend.gui.GuiClientUi;

import java.io.*;

/**
 * 카페 주문 프로그램 진입점
 *
 * 프로그램 인자(args)를 통해 CLI/GUI 모드 선택
 * - CLI 모드: java Main cli
 * - GUI 모드: java Main gui (기본값)
 */
public class Main {
    public static void main(String[] args) throws IOException {
        // 벡앤드 초기화
        StoreList storeList = new StoreList();
        UserList userList = new UserList(storeList);
        MenuStatusList menuStatusList = new MenuStatusList();
        MenuList menuList = new MenuList(menuStatusList, storeList);
        OrderList orderList = new OrderList(menuList, storeList);
        MyMenu myMenu = new MyMenu();

        // 프로그램 모드 선택
        String mode = (args.length > 0) ? args[0].toLowerCase() : "gui";

        switch (mode) {
            case "cli":
                System.out.println("=== CLI 모드로 실행합니다 ===");
                CliClientUi cliClient = new CliClientUi(storeList, userList, menuStatusList, menuList, orderList, myMenu);
                cliClient.start();
                break;

            case "gui":
                System.out.println("=== GUI 모드로 실행합니다 ===");
                GuiClientUi guiClient = new GuiClientUi(storeList, userList, menuStatusList, menuList, orderList, myMenu);
                guiClient.start();
                break;

            default:
                System.out.println("사용법: java Main [cli|gui]");
                System.out.println("기본값(GUI)으로 실행합니다.");
                GuiClientUi defaultGuiClient = new GuiClientUi(storeList, userList, menuStatusList, menuList, orderList, myMenu);
                defaultGuiClient.start();
        }
    }
}

