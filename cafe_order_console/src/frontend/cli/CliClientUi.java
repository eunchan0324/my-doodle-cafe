package frontend.cli;

import backend.menu.*;
import backend.order.*;
import backend.store.*;
import backend.user.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * CLI 클라이언트 UI
 *
 * 콘솔 기반 사용자 인터페이스
 * Scanner를 통한 텍스트 입출력 방식
 */
public class CliClientUi {
    // 백엔드 객체 (의존성 주입)
    private StoreList storeList;
    private UserList userList;
    private MenuStatusList menuStatusList;
    private MenuList menuList;
    private OrderList orderList;
    private MyMenu myMenu;

    /**
     * 생성자 - 백엔드 객체 주입
     */
    public CliClientUi(StoreList storeList, UserList userList, MenuStatusList menuStatusList, MenuList menuList, OrderList orderList, MyMenu myMenu) {
        this.storeList = storeList;
        this.userList = userList;
        this.menuStatusList = menuStatusList;
        this.menuList = menuList;
        this.orderList = orderList;
        this.myMenu = myMenu;
    }

    /**
     * CLI 모드 시작
     */
    public void start() throws IOException {
        Scanner sc = new Scanner(System.in);

        // 프로그램 시작
        while (true) {
            System.out.println();
            System.out.println("안녕하세요, 카페 주문 서비스입니다.");
            System.out.println(UserRole.ADMIN.getValue() + ". " + UserRole.ADMIN.getRole());
            System.out.println(UserRole.SELLER.getValue() + ". " + UserRole.SELLER.getRole());
            System.out.println(UserRole.CUSTOMER.getValue() + ". " + UserRole.CUSTOMER.getRole());
            System.out.println("4. 프로그램 종료");
            System.out.print("역할을 선택해주세요 : ");
            int roleChoice = sc.nextInt();
            sc.nextLine();

            UserRole role = UserRole.fromValue(roleChoice);
            switch (role) {
                // 관리자 모드
                case ADMIN -> {
                    if (this.userList.adminLogin() == null) {
                    }
                    while (true) {
                        System.out.println();
                        System.out.println("안녕하세요 관리자님, 메뉴를 선택해주세요");
                        System.out.println("1. 전체 메뉴 CRUD");
                        System.out.println("2. 판매자 계정 관리");
                        System.out.println("3. 매출 관리");
                        System.out.println("4. 지점 관리");
                        System.out.println("5. 뒤로가기");
                        System.out.print(" : ");
                        int menuChoice = sc.nextInt();
                        sc.nextLine();

                        // 1. 전체 메뉴 CRUD
                        if (menuChoice == 1) {
                            System.out.println();
                            System.out.println("[메뉴 등록 및 관리]");
                            System.out.println("1. 메뉴 등록");
                            System.out.println("2. 등록 메뉴 확인");
                            System.out.println("3. 메뉴 수정");
                            System.out.println("4. 메뉴 삭제");
                            System.out.println("5. 뒤로가기");
                            System.out.print("할 일을 선택해주세요 : ");

                            int choice = sc.nextInt();
                            sc.nextLine();
                            System.out.println();


                            // 1-1 메뉴 등록 (Create)
                            if (choice == 1) {
                                this.menuList.menuCreate();
                            }

                            // 1-2. 등록 메뉴 확인 (Read)
                            else if (choice == 2) {
                                this.menuList.showAllMenus();
                            }

                            // 1-3. 메뉴 수정
                            else if (choice == 3) {
                                System.out.println("\n[메뉴 수정]");
                                ArrayList<Menu> allMenus = this.menuList.showAndGetAllMenus();
                                if (allMenus.isEmpty()) {
                                    continue;
                                }

                                Menu targetMenu = null;
                                while (true) {
                                    System.out.print("수정할 메뉴의 번호를 입력하세요 (취소: 0) : ");
                                    int selection = sc.nextInt();
                                    sc.nextLine();
                                    if (selection == 0) break;
                                    if (selection >= 1 && selection <= allMenus.size()) {
                                        // 번호로 targetMenu 찾기
                                        targetMenu = allMenus.get(selection - 1);
                                        break;
                                    }
                                    System.out.println("잘못된 번호입니다.");
                                }

                                if (targetMenu != null) {
                                    // 찾은 메뉴 객체를 menuEdit 메서드에 직접 넘겨줌
                                    this.menuList.menuEdit(targetMenu);
                                }
                            }

                            // 1-4. 메뉴 삭제
                            else if (choice == 4) {
                                System.out.println("\n[메뉴 삭제]");
                                ArrayList<Menu> allMenus = this.menuList.showAndGetAllMenus();
                                if (allMenus.isEmpty()) continue;

                                Menu targetMenu = null;
                                while (true) {
                                    System.out.print("수정할 메뉴의 번호를 입력하세요 (취소: 0) : ");
                                    int selection = sc.nextInt();
                                    sc.nextLine();
                                    if (selection == 0) break;
                                    if (selection >= 1 && selection <= allMenus.size()) {
                                        // 번호로 targetMenu 찾기
                                        targetMenu = allMenus.get(selection - 1);
                                        break;
                                    }
                                    System.out.println("잘못된 번호입니다.");
                                }

                                if (targetMenu != null) {
                                    this.menuList.menuDelete(targetMenu);
                                }
                            }

                            // 1-5. 뒤로가기
                            else if (choice == 5) {
                            }


                        }

                        // 2. 판매자 계정 관리
                        else if (menuChoice == 2) {
                            System.out.println("[판매자 계정 관리]");
                            System.out.println("1. 판매자 계정 생성");
                            System.out.println("2. 판매자 계정 조회");
                            System.out.println("3. 판매자 계정 수정");
                            System.out.println("4. 판매자 계정 삭제");
                            System.out.println("5. 뒤로가기");
                            System.out.print(" : ");
                            int sellerMenuChoice = sc.nextInt();
                            sc.nextLine();

                            // 판매자 계정 create
                            if (sellerMenuChoice == 1) {
                                this.userList.registerUser(UserRole.SELLER, this.userList.getSellerList(), "Seller.txt");
                            }

                            // 판매자 계정 read
                            else if (sellerMenuChoice == 2) {
                                this.userList.sellerAccountRead();
                            }

                            // 판매자 계정 update
                            else if (sellerMenuChoice == 3) {
                                this.userList.sellerAccountUpdate();
                            }

                            // 판매자 계정 delete
                            else if (sellerMenuChoice == 4) {
                                this.userList.sellerAccountDelete();

                            }

                            // 뒤로가기
                            else if (sellerMenuChoice == 5) {
                            }
                        }

                        // 3. 매출 관리
                        else if (menuChoice == 3) {
                            System.out.println("[매출 관리]");
                            System.out.println("1. 전체 매출 조회");
                            System.out.println("2. 지점별 매출 조회");
                            System.out.print(" : ");
                            int SalesChoice = sc.nextInt();
                            sc.nextLine();

                            // 3-1. 전체 매출 조회
                            if (SalesChoice == 1) {
                                this.orderList.showAllSales();
                            }

                            // 3-2. 지점별 매출 조회
                            else if (SalesChoice == 2) {
                                this.orderList.showSalesBySeller();
                            }

                        }

                        // 4. 지점 관리
                        else if (menuChoice == 4) {
                            System.out.println("\n[지점 관리]");
                            System.out.println("1. 전체 지점 목록 조회");
                            System.out.println("2. 신규 지점 등록");
                            System.out.print(" : ");
                            int storeMenuChoice = sc.nextInt();
                            sc.nextLine();


                            // 4-1. 전체 지점 목록 조회
                            if (storeMenuChoice == 1) {
                                this.storeList.showAllStores();
                            }

                            // 4-2. 신규 지점 등록
                            else if (storeMenuChoice == 2) {
                                System.out.println("[신규 지점 등록]");
                                while (true) {
                                    System.out.print("신규 등록할 지점명을 입력해주세요 : ");
                                    String newStoreName = sc.nextLine();

                                    boolean success = this.storeList.registerNewStore(newStoreName);

                                    if (success) {
                                        break;
                                    } else {
                                        System.out.println("이미 존재하는 지점명입니다. 다른 이름을 입력해주세요.");
                                    }
                                }
                            }

                        }

                        // 5. 관리자 모드 나가기 (뒤로 가기)
                        else if (menuChoice == 5) {
                            break;
                        }
                    }
                }

                // 판매자 모드
                case SELLER -> {
                    System.out.println();
                    System.out.println("메뉴를 선택해주세요.");
                    System.out.println("1. 로그인");
                    System.out.println("2. 뒤로가기");
                    System.out.print(" : ");
                    int sellerFirstChoice = sc.nextInt();
                    sc.nextLine();

                    // 로그인
                    if (sellerFirstChoice == 1) {
                        User loggendinSeller = this.userList.sellerLogin();
                        if (loggendinSeller != null) {

                            int storeId = loggendinSeller.getStoreId();
                            String storeName = this.storeList.findStoreById(storeId).getStoreName();

                            while (true) {
                                System.out.println();
                                System.out.println("안녕하세요, " + storeName + " 지점 판매자님.");
                                System.out.println("1. 주문 관리");
                                System.out.println("2. 추천 메뉴 관리");
                                System.out.println("3. 재고 관리");
                                System.out.println("4. 매출 조회");
                                System.out.println("5. 판매 메뉴 관리");
                                System.out.println("6. 로그아웃");
                                System.out.print("할 일을 선택해주세요 : ");

                                int menuSelect = sc.nextInt();
                                sc.nextLine();
                                System.out.println();

                                // 1. 주문 관리
                                if (menuSelect == 1) {
                                    System.out.println("[주문 관리]");
                                    System.out.println("1. 주문 목록 확인");
                                    System.out.println("2. 주문 상태 변경");
                                    System.out.println("3. 뒤로가기");
                                    System.out.print(" : ");
                                    int orderMenuChoice = sc.nextInt();
                                    sc.nextLine();

                                    // 1-1. 주문 목록 확인
                                    if (orderMenuChoice == 1) {
                                        this.orderList.checkOrders();
                                    }

                                    // 1-2. 주문 상태 변경
                                    else if (orderMenuChoice == 2) {
                                        this.orderList.updateOrderStatus();
                                    }

                                    // 1-3. 뒤로가기
                                    else if (orderMenuChoice == 3) {

                                    }

                                }

                                // 2. 추천 메뉴 등록 및 관리
                                else if (menuSelect == 2) {
                                    this.menuList.menuRecommend();
                                }

                                // 3. 재고 관리
                                else if (menuSelect == 3) {
                                    while (true) {
                                        System.out.println();
                                        System.out.println("[재고 관리]");
                                        System.out.println("1. 재고 현황 보기");
                                        System.out.println("2. 재고 수량 변경");
                                        System.out.println("3. 판매 상태 변경");
                                        System.out.println("4. 뒤로 가기");
                                        System.out.print(" : ");

                                        int choice = sc.nextInt();
                                        sc.nextLine();

                                        // 3-1. 재고 현황 보기
                                        if (choice == 1) {
                                            this.menuList.showStockStatusForSeller(storeId);
                                        }

                                        // 3-2. 재고 수량 변경
                                        else if (choice == 2) {
                                            System.out.println("\n[재고 수량 변경]");

                                            // 1. 새 메서드를 호출하여 메뉴 목록을 보여주고, 리스트를 받아옴
                                            ArrayList<Menu> sellableMenus = this.menuList.showAndGetSellableMenus(storeId);

                                            // 판매 가능한 메뉴가 없으면 로직 종료
                                            if (sellableMenus.isEmpty()) {
                                                continue;
                                            }

                                            Menu targetMenu = null;
                                            while (true) {
                                                System.out.print("수정할 메뉴의 ID를 입력해주세요 (취소 : 0) : ");
                                                int selection = sc.nextInt();
                                                sc.nextLine();

                                                if (selection == 0) {
                                                    targetMenu = null; // 취소를 명시적으로 표시
                                                    break;
                                                }

                                                if (selection >= 1 && selection <= sellableMenus.size()) {
                                                    // 2. 입력받은 '번호'로 리스트에서 실제 Menu 객체를 찾음
                                                    targetMenu = sellableMenus.get(selection - 1);
                                                    break;
                                                } else {
                                                    System.out.println("잘못된 번호입니다. 목록에 있는 번호를 다시 입력해주세요.");
                                                }
                                            }

                                            // 사용자가 취소를 선택한 경우
                                            if (targetMenu == null) {
                                                System.out.println("재고 수량 변경을 취소했습니다.");
                                                continue;
                                            }


                                            System.out.print("수정할 재고의 수량을 입력해주세요 : ");
                                            int newStock = sc.nextInt();
                                            sc.nextLine();

                                            // 3. 찾은 Menu 객체에서 UUID를 꺼내서 사용
                                            this.menuStatusList.updateStock(storeId, targetMenu.getId(), newStock);
                                        }

                                        // 3-3. 판매 상태 변경
                                        else if (choice == 3) {
                                            System.out.println("\n[판매 상태 변경]");

                                            // 1. 메뉴 목록을 보여주고, 리스트를 받아옴
                                            ArrayList<Menu> sellableMenus = this.menuList.showAndGetSellableMenus(storeId);

                                            // 비어있다면,
                                            if (sellableMenus.isEmpty()) {
                                                continue;
                                            }

                                            Menu targetMenu = null;
                                            // 2. 임시 번호를 입력받아 targetMenu를 찾는 로직
                                            while (true) {
                                                System.out.print("수정할 메뉴의 번호를 입력해주세요 (취소: 0) : ");
                                                int selection = sc.nextInt();
                                                sc.nextLine();

                                                if (selection == 0) {
                                                    targetMenu = null;
                                                    break;
                                                }

                                                if (selection >= 1 && selection <= sellableMenus.size()) {
                                                    targetMenu = sellableMenus.get(selection - 1);
                                                    break;
                                                } else {
                                                    System.out.println("잘못된 번호입니다. 목록에 있는 번호를 다시 입력해주세요.");
                                                }
                                            }

                                            if (targetMenu == null) {
                                                System.out.println("판매 상태 변경을 취소했습니다.");
                                                continue;
                                            }

                                            System.out.println("\n['" + targetMenu.getName() + "' 메뉴의 상태를 변경합니다.");
                                            System.out.println("1. 판매 가능 (AVAILABLE)");
                                            System.out.println("2. 판매 중지 (SOLD_OUT)");
                                            System.out.print(" : ");
                                            int statusChoice = sc.nextInt();
                                            sc.nextLine();

                                            EMenuSaleStatus newstatus = null;
                                            if (statusChoice == 1) {
                                                newstatus = EMenuSaleStatus.AVAILABLE;
                                            } else if (statusChoice == 2) {
                                                newstatus = EMenuSaleStatus.SOLD_OUT;
                                            }

                                            if (newstatus != null) {
                                                // 찾은 Menu 객체에서 UUID를 꺼내 사용
                                                this.menuStatusList.updateStatus(storeId, targetMenu.getId(), newstatus);
                                                System.out.println("판매 상태가 '" + newstatus.getDisplayStatus() + "' (으)로 변경되었습니다.");
                                            } else {
                                                System.out.println("번호를 잘못 입력하셨습니다. 이전 메뉴로 돌아갑니다.");
                                            }

                                        }

                                        // 3-4. 재고 관리 종료
                                        else if (choice == 4) {
                                            break;
                                        } else {
                                            System.out.println("잘못된 번호 입력");
                                        }

                                    }
                                }

                                // 4. 매출 관리
                                else if (menuSelect == 4) {
                                    this.orderList.showMySales(storeId);
                                }

                                // 5. 판매 메뉴 관리
                                else if (menuSelect == 5) {
                                    ArrayList<Menu> allMenus = this.menuList.showAndGetManageableMenus(storeId);

                                    // 메뉴가 비어있다면
                                    if (allMenus.isEmpty()) {
                                        continue;
                                    }

                                    System.out.println();
                                    System.out.println("1. 판매 메뉴 등록하기");
                                    System.out.println("2. 판매 메뉴 삭제하기");
                                    System.out.println("3. 뒤로가기");
                                    System.out.print(" : ");
                                    int salesChoice = sc.nextInt();
                                    sc.nextLine();

                                    // 5-3. 뒤로가기
                                    if (salesChoice == 3) {
                                        continue;
                                    }

                                    // 5-1 or 5-2
                                    else if (salesChoice == 1 || salesChoice == 2) {
                                        // 등록,삭제 메뉴 선택에 필요한 입력 한 번에 받기
                                        Menu targetMenu = null;
                                        while (true) {
                                            System.out.print("작업할 메뉴의 번호를 입력해주세요 (취소 : 0) : ");
                                            int selection = sc.nextInt();
                                            sc.nextLine();

                                            if (selection == 0) {
                                                targetMenu = null;
                                                break;
                                            }

                                            if (selection >= 1 && selection <= allMenus.size()) {
                                                targetMenu = allMenus.get(selection - 1);
                                                break;
                                            } else {
                                                System.out.println("잘못된 번호입니다. 목록에 있는 번호를 다시 입력해주세요.");
                                            }
                                        }

                                        if (targetMenu == null) {
                                            System.out.println("작업을 취소했습니다.");
                                            continue;
                                        }

                                        // 5-1. 판매 메뉴 등록하기
                                        if (salesChoice == 1) {
                                            this.menuStatusList.registerMenuForSale(storeId, targetMenu.getId());
                                        }

                                        // 5-2. 판매 메뉴 삭제하기
                                        else if (salesChoice == 2) {
                                            this.menuStatusList.removeMenuForSale(storeId, targetMenu.getId());
                                        }
                                    }
                                }

                                // 6. 로그아웃
                                else if (menuSelect == 6) {
                                    System.out.println("로그아웃이 완료되었습니다.");
                                    break;
                                }
                            }
                        }
                    }

                    // 뒤로가기
                    else if (sellerFirstChoice == 2) {
                    }


                }

                // 구매자 모드
                case CUSTOMER -> {
                    System.out.println();
                    System.out.println("메뉴를 선택해주세요.");
                    System.out.println("1. 회원가입");
                    System.out.println("2. 로그인");
                    System.out.println("3. 뒤로가기");
                    System.out.print(" : ");
                    int customerFirstChoice = sc.nextInt();
                    sc.nextLine();

                    // 회원가입
                    if (customerFirstChoice == 1) {
                        this.userList.registerUser(UserRole.CUSTOMER, this.userList.getCustomerList(), "Customer.txt");
                    }

                    // 로그인
                    else if (customerFirstChoice == 2) {
                        User loggedInCustomer = this.userList.customerLogin();
                        // 로그인 성공
                        if (loggedInCustomer != null) {
                            while (true) {
                                // todo : 하드코딩 리팩토링
                                int storeId = 1;

                                System.out.println();
                                System.out.println("안녕하세요 손님, 카페 주문 서비스입니다.");
                                System.out.println("할 일을 선택해주세요.");
                                System.out.println("1. 메뉴 보기 및 주문");
                                System.out.println("2. 주문 내역 확인 ");
                                System.out.println("3. 오늘의 메뉴 확인");
                                System.out.println("4. 나만의 메뉴 (찜)");
                                System.out.println("5. 로그아웃");
                                System.out.print(" : ");

                                int cusChoice = sc.nextInt();
                                sc.nextLine();

                                // 2-1. 메뉴 보기 및 주문
                                if (cusChoice == 1) {
                                    ArrayList<Menu> orderableMenus = this.menuList.showAndGetOrderableMenus(storeId);

                                    // 주문할 메뉴가 없다면
                                    if (orderableMenus.isEmpty()) {
                                        continue;
                                    }

                                    // 1. 장바구니
                                    ArrayList<OrderItem> cart = new ArrayList<>();
                                    // 2. 메뉴 담기
                                    while (true) {
                                        System.out.print("주문할 메뉴의 번호를 입력해주세요 (주문 완료는 : 0 입력) : ");
                                        int menuSelection = sc.nextInt();
                                        sc.nextLine();

                                        if (menuSelection == 0) {
                                            break; // 주문 완료
                                        }

                                        if (menuSelection < 1 || menuSelection > orderableMenus.size()) {
                                            System.out.println("잘못된 번호입니다. 다시 입력해주세요.");
                                            continue; // 잘못된 번호면 다시 입력받기
                                        }

                                        // 유효한 번호임이 보장
                                        Menu targetMenu = orderableMenus.get(menuSelection - 1);

                                        // 재고 있을 때
                                        if (this.menuStatusList.isAvailable(storeId, targetMenu.getId())) {
                                            OrderItem newItem = createOrderItemWithOptions(targetMenu, menuList);
                                            cart.add(newItem);
                                            System.out.println("-> '" + targetMenu.getName() + "' 메뉴가 장바구니에 담겼습니다.");
                                        }

                                        // 재고 없을 때
                                        else {
                                            System.out.println("죄송합니다. '" + targetMenu.getName() + "' 메뉴는 현재 품절입니다.");
                                        }

                                        System.out.println("--- 현재 장바구니 : " + cart.size() + "개 ---");

                                    }

                                    // 3. 주문 처리 (루프 끝난 후)
                                    if (!cart.isEmpty()) {
                                        // 3-1. 총액 계산 및 backend.order.Order 객체 생성
                                        int totalPirce = 0;
                                        for (OrderItem item : cart) {
                                            totalPirce += item.getFinalPrice(); //
                                        }

                                        Order finalOrder = new Order(loggedInCustomer.getId(), storeId, totalPirce, OrderStatus.ORDER_PLACED, cart);

                                        // 3-2. 주문 내역에 '최종 주문서 추가'
                                        this.orderList.addOrder(finalOrder);

                                        // 3-3. 재고 감소
                                        for (OrderItem item : cart) {
                                            this.menuStatusList.decreaseStock(storeId, item.getMenu().getId());
                                        }

                                        System.out.println("주문이 완료되었습니다. 대기 번호 : " + finalOrder.getWaitingNumber());
                                    }
                                }

                                // 2-2. 주문 메뉴 확인
                                if (cusChoice == 2) {
                                    this.orderList.checkOrders(loggedInCustomer.getId());
                                }

                                // 2-3. 오늘의 추천 메뉴 확인
                                if (cusChoice == 3) {
                                    this.menuList.menuRecommendRead();
                                }

                                // 2-4. 나만의 메뉴
                                if (cusChoice == 4) {
                                    System.out.println("[나만의 메뉴]");
                                    System.out.println("1. 나만의 메뉴 등록");
                                    System.out.println("2. 나만의 메뉴 확인");
                                    System.out.println("3. 나만의 메뉴 삭제");
                                    System.out.print("원하는 기능을 선택해주세요 : ");
                                    int choice = sc.nextInt();

                                    if (choice == 1) {
                                        this.myMenu.CreateMyManu(menuList);
                                    } else if (choice == 2) {
                                        this.myMenu.ReadMyMenu();
                                    } else if (choice == 3) {
                                        this.myMenu.DeleteMyMenu();
                                    }
                                }

                                // 2-5. 로그아웃
                                if (cusChoice == 5) {
                                    System.out.println("로그아웃이 완료되었습니다.");
                                    break;
                                }
                            }
                        }

                        // 로그인 실패
                        else {
                            System.out.println("로그인에 실패했습니다.");
                        }
                    } else if (customerFirstChoice == 3) {
                    }


                }

                // Case UNROLE
                case UNROLE -> {
                    if (roleChoice == 4) {
                        System.out.println("프로그램을 종료합니다.");
                        return;
                    } else {
                        System.out.println("정확한 번호를 입력해주세요.");
                    }
                }
            }


        }
    }

    // ===== 헬퍼 메서드 =====
    /**
     * 주문 아이템 옵션 선택
     */
    private OrderItem createOrderItemWithOptions(Menu menu, MenuList menuList) {
        Scanner sc = new Scanner(System.in);
        String finalOptions = "선택안함";
        int finalPrice = menu.getPrice();
        String finalTemp = "ICE";    // 기본값
        String finalCup = "일회용컵"; // 기본값

        // 1. 추가 옵션 선택 (COFFEE, LATTE, TEA 등)
        if (menu.getOption() == MenuCategory.COFFEE) {
            System.out.println("샷 옵션을 선택해주세요");
            System.out.println("1.기본(2샷) | 2.연하게(1샷) | 3.샷추가(+500원) | 4.디카페인(+1000원)");
            System.out.print(" -> 선택: ");
            int choice = sc.nextInt();
            sc.nextLine();
            if (choice == 3) {
                finalPrice += 500;
                finalOptions = "샷추가(3샷)";
            } else if (choice == 4) {
                finalPrice += 1000;
                finalOptions = "디카페인";
            } else if (choice == 2) {
                finalOptions = "연하게(1샷)";
            } else {
                finalOptions = "기본(2샷)";
            }
        } else if (menu.getOption() == MenuCategory.LATTE) {
            System.out.println("우유 옵션을 선택해주세요");
            System.out.println("1. 일반 우유 | 2. 오트(귀리) (+1000원)");
            System.out.print(" -> 선택: ");
            int choice = sc.nextInt();
            sc.nextLine();
            if (choice == 2) {
                finalPrice += 1000;
                finalOptions = "오트(귀리)";
            } else {
                finalOptions = "일반 우유";
            }
        } else if (menu.getOption() == MenuCategory.TEA) {
            System.out.println("물 양을 선택해주세요");
            System.out.println("1. 보통 | 2. 적게");
            System.out.print(" -> 선택: ");
            int choice = sc.nextInt();
            sc.nextLine();
            if (choice == 2) {
                finalOptions = "물 양 적게";
            } else {
                finalOptions = "물 양 보통";
            }
        }

        // 2. 온도 선택 (COFFEE 또는 TEA 카테고리일 경우)
        if (menu.getOption() == MenuCategory.COFFEE || menu.getOption() == MenuCategory.TEA) {
            while (true) {
                System.out.println("1. HOT | 2. ICE");
                System.out.print(" -> 온도를 선택해주세요 : ");
                int choice = sc.nextInt();
                sc.nextLine();
                if (choice == 1) {
                    finalTemp = "HOT";
                    break;
                } else if (choice == 2) {
                    finalTemp = "ICE";
                    break;
                } else {
                    System.out.println("잘못된 번호입니다.");
                }
            }
        }

        // 3. 컵 선택
        while (true) {
            System.out.println("1. 매장컵 | 2. 개인컵 (-300원) | 3. 일회용컵");
            System.out.print(" -> 컵을 선택해주세요 : ");
            int choice = sc.nextInt();
            sc.nextLine();
            if (choice == 1) {
                finalCup = "매장컵";
                break;
            } else if (choice == 2) {
                finalCup = "개인컵";
                finalPrice -= 300;
                break;
            } else if (choice == 3) {
                finalCup = "일회용컵";
                break;
            } else {
                System.out.println("잘못된 번호입니다.");
            }
        }

        System.out.println("\n-> [선택 완료] " + menu.getName() + " (온도: " + finalTemp + ", 컵: " + finalCup + ", 옵션: " + finalOptions + ")");

        return new OrderItem(menu, finalPrice, finalOptions, finalCup, finalTemp);
    }
}
