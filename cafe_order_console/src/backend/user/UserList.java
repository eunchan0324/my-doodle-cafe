package backend.user;

import backend.constant.Constants;
import backend.store.Store;
import backend.store.StoreList;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class UserList {
    private ArrayList<User> adminList = new ArrayList<>();
    private ArrayList<User> sellerList = new ArrayList<>();
    private ArrayList<User> customerList = new ArrayList<>();
    private StoreList storeList;

    public ArrayList<User> getAdminList() {
        return adminList;
    }

    public ArrayList<User> getSellerList() {
        return sellerList;
    }

    public ArrayList<User> getCustomerList() {
        return customerList;
    }

    Scanner sc = new Scanner(System.in);

    // 모든 backend.user.User Load + load backend.store.Store
    public UserList(StoreList storeList) {
        this.storeList = storeList;

        try {
            loadSellerFile();
            loadAdminFile();
            loadCustomerFile();
        } catch (IOException e) {
            System.out.println("사용자 파일 로딩 중 오류가 발생했습니다.");
        }
    }

    // 판매자 회원가입 (GUI)
    public boolean registerSeller(String id, String password, int storeId) throws IOException {
        // ID 중복 검사
        if (findUser(id, sellerList) != null) {
            return false; // 중복
        }

        // storeId 존재 여부 검사
        Store store = storeList.findStoreById(storeId);
        if (store == null) {
            throw new IllegalArgumentException("존재하지 않는 지점 ID입니다.");
        }

        // storeId 중복 검사
        for (User seller : sellerList) {
            if (seller.getStoreId() == storeId) {
                throw new IllegalArgumentException("이미 배정된 지점입니다.");
            }
        }


        // 사용자 객체 생성 및 추가
        User user = new User(id, password, UserRole.SELLER, storeId);
        sellerList.add(user);

        // 파일 저장
        saveSellerFile();

        return true;
    }

    // 구매자 회원가입 (GUI)
    public boolean registerCustomer(String id, String password) throws IOException {
        // ID 중복 검사
        if (findUser(id, customerList) != null) {
            return false; // 중복
        }

        // 사용자 객체 생성 및 추가
        User user = new User(id, password, UserRole.CUSTOMER);
        customerList.add(user);

        // 파일 저장
        saveCustomerFile();

        return true;
    }

    // 회원가입 - All role
    public void registerUser(UserRole role, ArrayList<User> targetList, String fileName) throws IOException {
        System.out.println();
        System.out.println("[" + role + " 회원가입]");

        String thisId;
        String thisPassword;

        // id
        while (true) {
            System.out.print("ID를 입력해주세요 (4~12자 사이 / 영문,숫자만 허용) : ");
            String id = sc.nextLine();

            // id check 실패
            if (!idDuplicateCheck(id, targetList)) {
                System.out.println("중복된 ID입니다. 다시 입력해주세요");
                continue;
            }

            // 유효성 검사
            if (!idInvalidCheck(id)) {
                System.out.println("유효하지 않은 ID입니다. 4~12자 사이로 입력해주세요.");
                continue;
            }

            // id check 성공
            System.out.println("사용가능한 ID입니다.");
            thisId = id;
            break;
        }

        // password
        while (true) {
            System.out.print("Password를 입력해주세요 (길이 8자 이상 / 영문 + 숫자 조합만 허용) : ");
            String password = sc.nextLine();

            // pw check 실패
            if (!passwordInvalidCheck(password)) {
                System.out.println("유효하지 않은 Password입니다. 8자 이상, 영문+숫자 조합으로 입력해주세요.");
                continue;
            }

            // pw check 성공
            System.out.println("사용가능한 Password입니다.");
            thisPassword = password;
            break;

        }

        // role
        while (true) {
            // role이 seller 일 때
            if (role == UserRole.SELLER) {

                // 1. 이미 배정된 지점 ID 목록을 미리 확보 - Set 사용
                java.util.Set<Integer> assignedStoreIds = new java.util.HashSet<>();
                for (User seller : sellerList) {
                    assignedStoreIds.add(seller.getStoreId());
                }

                // 2. 사용자에게 전체 지점 목록과 현재 배정 상태를 보여준다.
                System.out.println("\n--- 전체 지점 목록 ---");
                for (Store store : storeList.getStores()) {
                    String status = (assignedStoreIds.contains(store.getStoreId())) ? "[배정됨]" : "[사용가능]";
                    System.out.println("ID : " + store.getStoreId() + ", 지점명 : " + store.getStoreName() + " " + status);
                }
                System.out.println("--------------------");

                while (true) {
                    System.out.print("소속시킬 지점 ID를 선택해주세요 (중복 배정 불가) : ");
                    int storeId = sc.nextInt();
                    sc.nextLine();

                    // 3-1. 입력한 storeId가 실제로 존재하는지 검사
                    if (storeList.findStoreById(storeId) == null) {
                        System.out.println("존재하지 않는 지점 ID입니다. 다시 입력해주세요.");
                        continue;
                    }

                    // 3-2 입력한 ID가 이미 배정된 지점인지 검사
                    if (assignedStoreIds.contains(storeId)) {
                        System.out.println("이미 다른 판매자에게 배정된 지점입니다. 중복 배정은 불가하니 다시 입력해주세요.");
                        continue;
                    }

                    // 모든 검증이 통과되었으므로 사용자 생성 후 while문 탈출
                    User user = new User(thisId, thisPassword, role, storeId);
                    targetList.add(user);
                    saveSellerFile();
                    System.out.println("'" + storeList.findStoreById(storeId).getStoreName() + "' 지점에 판매자 계정 생성이 완료되었습니다.");
                    break;
                }
                break; // 바깥 while문 탈출
            }

            // role이 customer일 때
            else if (role == UserRole.CUSTOMER) {
                User user = new User(thisId, thisPassword, role);
                targetList.add(user);

                saveCustomerFile();
                break;
            }

        }

        System.out.println(role.getRole() + " 회원가입이 완료되었습니다.");
    }

    // id 중복 확인
    public Boolean idDuplicateCheck(String id, ArrayList<User> targetList) {
        boolean idchecker = true;

        // 중복 확인
        for (int i = 0; i < targetList.size(); i++) {
            if (id.equals(targetList.get(i).getId())) {
                System.out.println();
                idchecker = false;
            }
        }

        return idchecker;
    }

    // id 유효성 검사
    // todo : 정규식 리팩토링
    // todo :  return으로 리팩토링
    public Boolean idInvalidCheck(String id) {
        boolean idchecker = true;
        if (id.length() < 4 || id.length() > 12) {
            idchecker = false;
        }

        boolean 영문있음 = false;
        boolean 숫자있음 = false;
        for (int i = 0; i < id.length(); i++) {
            char c = id.charAt(i);

            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                영문있음 = true;
            }

            if (c >= '0' && c <= '9') {
                숫자있음 = true;
            }
        }

        if (영문있음 && 숫자있음) {
            return idchecker;
        } else {
            return false;
        }
    }

    // password 유효성 검사
    public Boolean passwordInvalidCheck(String password) {
        boolean passwordCehcker = true;

        if (password.length() < 8) {
            passwordCehcker = false;
        }

        boolean 영문있음 = false;
        boolean 숫자있음 = false;
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);

            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                영문있음 = true;
            }

            if (c >= '0' && c <= '9') {
                숫자있음 = true;
            }
        }

        if (영문있음 && 숫자있음) {
            return passwordCehcker;
        } else {
            return false;
        }
    }

    // 로그인 로직 (CLI)
    public User performLogin(ArrayList<User> targetList, String systemName) {
        System.out.println();
        System.out.println("[" + systemName + " 로그인]");
        while (true) {
            System.out.println("1. 로그인");
            System.out.println("2. 뒤로가기");
            System.out.print(" : ");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                boolean loginSuccess = false;
                boolean idChecker = false;
                boolean pwChecker = false;

                System.out.println();
                System.out.print("id를 입력해주세요 : ");
                String id = sc.nextLine();

                System.out.print("password를 입력해주세요 : ");
                String password = sc.nextLine();

                if (targetList.isEmpty()) {
                    System.out.println("유효하지 않은 id/pw입니다.");
                    System.out.println("회원가입을 먼저 진행해주세요.");
                }

                User 찾은사용자 = findUser(id, targetList);

                if (찾은사용자 != null) {
                    // 입력한 pw가 없다면?
                    if (!password.equals(찾은사용자.getPassword())) {
                        pwChecker = true;
                    } else {
                        pwChecker = false;
                    }
                } else {
                    System.out.println("유효하지 않은 id입니다. 다시 입력해주세요.");
                    idChecker = true;
                }

                if (pwChecker) {
                    System.out.println("유효하지 않은 password입니다. 다시 입력해주세요.");
                }

                if (!idChecker && !pwChecker) {
                    loginSuccess = true;
                }

                if (loginSuccess) {
                    System.out.println("로그인이 완료되었습니다!");
                    return 찾은사용자;
                }
            } else if (choice == 2) {
                return null;
            } else {
                System.out.println("올바른 번호를 입력해주세요.");
            }
        }
    }

    // 로그인 로직 (GUI)
    private User performLoginWithCredentials(ArrayList<User> targetList, String id, String password) {
        // 리스트가 비어있으면
        if (targetList.isEmpty()) {
            return null;
        }

        // 사용자 찾기
        User 찾은사용자 = findUser(id, targetList);

        // ID가 없으면
        if (찾은사용자 == null) {
            return null;
        }

        // 비밀번호 확인
        if (!password.equals(찾은사용자.getPassword())) {
            return null;
        }

        // 로그인 성공
        return 찾은사용자;

    }

    // 관리자 로그인 (CLI)
    public User adminLogin() {
        return performLogin(adminList, "관리자");
    }

    // 관리자 로그인 (GUI)
    public User adminLogin(String id, String password) {
        return performLoginWithCredentials(adminList, id, password);
    }

    // 판매자 로그인 (CLI)
    public User sellerLogin() {
        return performLogin(sellerList, "판매자");
    }

    // 판매자 로그인 (GUI)
    public User sellerLogin(String id, String password) {
        return performLoginWithCredentials(sellerList, id, password);
    }

    // 구매자 로그인 (CLI)
    public User customerLogin() {
        return performLogin(customerList, "구매자");
    }

    // 구매자 로그인 (GUI)
    public User customerLogin(String id, String password) {
        return performLoginWithCredentials(customerList, id, password);
    }

    // 판매자 계정 조회(read)
    public void sellerAccountRead() {

        if (!sellerList.isEmpty()) {
            for (int i = 0; i < sellerList.size(); i++) {
                System.out.println(sellerList.get(i));
            }
        } else {
            System.out.println("판매자 계정이 존재하지 않습니다.");
        }
    }

    // [관리자] 판매자 계정 수정 UPDATE (GUI)
    public void sellerAccountUpdate(String id, String password, int storeId) throws IOException {
        // 수정 대상 판매자 찾기
        User targetSeller = findUser(id, sellerList);
        if (targetSeller == null) {
            throw new IllegalArgumentException("존재하지 않는 판매자 ID입니다.");
        }

        // storeId 존재 여부 검사
        Store store = storeList.findStoreById(storeId);
        if (store == null) {
            throw new IllegalArgumentException("존재하지 않는 지점 ID입니다.");
        }

        // storeId 중복 검사 (자기 자신이 원래 가진 지점은 제외)
        for (User seller : sellerList) {
            // 자기 자신은 건너뛰기
            if (seller.getId().equals(id)) {
                continue;
            }

            // 다른 판매자가 이미 사용중인 지점인지 확인
            if (seller.getStoreId() == storeId) {
                throw new IllegalArgumentException("중복된 지점 ID입니다.");
            }
        }

        // 검증 완료 후, 수정
        targetSeller.setPassword(password);
        targetSeller.setStoreId(storeId);

        // 파일 저장
        saveSellerFile();
    }

    // 판매자 계정 수정(update)
    public void sellerAccountUpdate() throws IOException {
        System.out.println("[판매자 계정 수정]");
        sellerAccountRead();

        while (true) {
            System.out.print("수정을 원하는 id를 입력해주세요 : ");
            String id = sc.nextLine();
            boolean checker = false;

            for (int i = 0; i < sellerList.size(); i++) {
                if (id.equals(sellerList.get(i).getId())) {
                    System.out.println("입력한 id 계정 정보 : " + sellerList.get(i));
                    System.out.print("변경할 비밀번호를 입력해주세요 : ");
                    String newPassword = sc.nextLine();

                    sellerList.get(i).setPassword(newPassword);
                    saveSellerFile();

                    System.out.println("수정이 완료되었습니다.");
                    checker = true;
                    break;
                }
            }

            if (checker == true) {
                break;
            }

            if (checker == false) {
                System.out.println("입력한 id가 정확하지 않습니다.");
                System.out.println("1. id 다시 입력하기");
                System.out.println("2. 나가기");
                System.out.print(" : ");
                int choice = sc.nextInt();
                sc.nextLine();

                if (choice == 2) {
                    break;
                }
            }
        }
    }

    // 판매자 계정 삭제 (GUI)
    public void sellerAccountDelete(User seller) throws IOException {
        sellerList.remove(seller);

        saveSellerFile();
    }

    // 판매자 계정 삭제(delete) (CLI)
    public void sellerAccountDelete() throws IOException {
        System.out.println("[판매자 계정 삭제]");

        while (true) {
            sellerAccountRead();
            System.out.print("삭제를 원하는 id를 입력해주세요 : ");
            String id = sc.nextLine();
            boolean checker = false;
            boolean shouldExit = false;

            for (int i = 0; i < sellerList.size(); i++) {
                if (id.equals(sellerList.get(i).getId())) {
                    System.out.println("입력한 id 계정 정보 : " + sellerList.get(i));
                    System.out.println("정말 삭제하시겠습니까?");
                    System.out.println("1. 네");
                    System.out.println("2. 아니요");
                    System.out.print(" : ");
                    int choice = sc.nextInt();
                    sc.nextLine();

                    if (choice == 1) {
                        sellerList.remove(i);

                        saveSellerFile();
                        checker = true;
                        break;
                    } else if (choice == 2) {
                        System.out.println("1. 다른 ID 입력");
                        System.out.println("2. 나가기");
                        System.out.print(" : ");
                        int choice2 = sc.nextInt();
                        sc.nextLine();

                        if (choice2 == 1) {
                            break;
                        } else if (choice2 == 2) {
                            return;
                        }
                    }

                    if (shouldExit) {
                        break;
                    }
                }
            }

            if (checker) {
                System.out.println("삭제가 완료되었습니다.");
                break;
            }

            if (checker == false) {
                System.out.println("입력한 id가 유효하지 않습니다.");
                System.out.println("1. id 재입력");
                System.out.println("2. 나가기");
                System.out.print(" : ");
                int choice = sc.nextInt();
                sc.nextLine();

                if (choice == 2) {
                    break;
                }
            }
        }
    }

    // Seller Save (seller.txt) + Add sellerList
    public void saveSellerFile() throws IOException {
        Path sellerFilePath = Constants.BASE_PATH.resolve("Seller.txt");
        FileWriter writer = new FileWriter(sellerFilePath.toFile());

        for (User seller : sellerList) {
            writer.write(seller.getId() + "," +
                    seller.getPassword() + "," +
                    seller.getRole().name() + "," +
                    seller.getStoreId() + "\n"
            );
        }
        writer.close();
    }

    // Seller load (seller.txt) + Add sellerList
    public void loadSellerFile() throws IOException {
        Path userFilePath = Constants.BASE_PATH.resolve("Seller.txt");
        BufferedReader sellerReader = new BufferedReader(new FileReader(userFilePath.toFile()));

        String line;
        while ((line = sellerReader.readLine()) != null) {
            String[] parts = line.split(",");
            String sellerId = parts[0];
            String sellerPassword = parts[1];
            UserRole role = UserRole.valueOf(parts[2]);
            int storeId = Integer.parseInt(parts[3]);

            User seller = new User(sellerId, sellerPassword, role, storeId);
            sellerList.add(seller);
        }
        sellerReader.close();
    }

    // Admin 파일 load + adminlist에 업로드
    public void loadAdminFile() throws IOException {
        Path adminFilePath = Constants.BASE_PATH.resolve("Admin.txt");
        BufferedReader reader = new BufferedReader((new FileReader(adminFilePath.toFile())));

        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            String id = parts[0];
            String pw = parts[1];
            UserRole role = UserRole.valueOf(parts[2]);

            User user = new User(id, pw, role);
            adminList.add(user);
        }
        reader.close();
    }

    // Customer Save (customer.txt) + Add customerList
    public void saveCustomerFile() throws IOException {
        Path customerFilePath = Constants.BASE_PATH.resolve("Customer.txt");
        FileWriter writer = new FileWriter(customerFilePath.toFile());

        for (User customer : customerList) {
            writer.write(customer.getId() + "," +
                    customer.getPassword() + "," +
                    customer.getRole().name() + "\n");
        }
        writer.close();
    }

    // Customer.txt load + customerList add
    public void loadCustomerFile() throws IOException {
        Path adminFilePath = Constants.BASE_PATH.resolve("Customer.txt");
        BufferedReader reader = new BufferedReader((new FileReader(adminFilePath.toFile())));

        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            String id = parts[0];
            String pw = parts[1];
            UserRole role = UserRole.valueOf(parts[2]);

            User user = new User(id, pw, role);
            customerList.add(user);
        }
        reader.close();
    }

    // id,tarList에 맞는 객체 찾기
    public User findUser(String id, ArrayList<User> targetList) {
        for (int i = 0; i < targetList.size(); i++) {
            if (targetList.get(i).getId().equals(id)) {
                return targetList.get(i);
            }
        }
        return null;
    }


}
