package backend.store;

import backend.constant.Constants;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class StoreList {
    ArrayList<Store> stores = new ArrayList<>();

    public ArrayList<Store> getStores() {
        return stores;
    }

    // 생성자 - loadStoreFIle
    public StoreList() {
        try {
            loadStoreFIle();
        } catch (IOException e) {
            System.out.println("지점 파일 로드 중 오류 발생");
        }
    }

    // backend.store save (stores.txt)
    public void saveStoreFile() throws IOException {
        Path storeFilePath = Constants.BASE_PATH.resolve("Stores.txt");
        FileWriter storeWriter = new FileWriter(storeFilePath.toFile());

        for (Store store : stores) {
            storeWriter.write(
                    store.getStoreId() + "," +
                            store.getStoreName() + "\n"
            );
        }
        storeWriter.close();
    }

    // backend.store load (stores.txt)
    public void loadStoreFIle() throws IOException {
        Path storeFilePath = Constants.BASE_PATH.resolve("Stores.txt");
        BufferedReader storeReader = new BufferedReader(new FileReader(storeFilePath.toFile()));

        String line;
        while ((line = storeReader.readLine()) != null) {
            String[] parts = line.split(",");
            int storeId = Integer.parseInt(parts[0]);
            String storeName = parts[1];

            Store store = new Store(storeId, storeName);
            stores.add(store);
        }
        storeReader.close();
    }

    // 입력받은 지점 ID로 store반환
    public Store findStoreById(int storeId) {
        // 매개변수 storeId가 stores에 있다면, backend.store 반환
        for (Store store : stores) {
            if (storeId == store.getStoreId()) {
                return store;
            }
        }
        // 매개변수 storeId가 stores에 있다면, null 반환
        return null;
    }

    // 지점 목록 조회
    public void showAllStores() {
        // 지점이 아무것도 등록되지 않았을 때
        if (stores.isEmpty()) {
            System.out.println("아무 지점이 등록되지 않았습니다.");
            return;
        }

        // 지점이 1개 이상 등록되었을 때
        System.out.println("--- 전체 지점 목록 ---");
        for (Store store : stores) {
            System.out.println("지점 ID : " + store.getStoreId() + " | 지점명 : " + store.getStoreName());
        }
    }

    // 지점 중복 검사
    public boolean isDuplicateName(String storeName) {
        for (Store store : stores) {
            if (store.getStoreName().equals(storeName)) {
                return true; // 중복 이름 발견
            }
        }
        return false; // 중복 없음
    }

    // 신규 지점 등록 (CLI)
    public boolean registerNewStore(String newStoreName) {
        // 중복 검사, 중복시 false 리턴
        if (isDuplicateName(newStoreName)) {
            return false;
        }

        // 현재까지 storeId중, maxId 구하기
        int maxId = 0;
        for (Store store : stores) {
            if (store.getStoreId() > maxId) {
                maxId = store.getStoreId();
            }
        }

        // maxId + 1로 newId 계산
        int newId = maxId + 1;

        Store newStore = new Store(newId, newStoreName);
        stores.add(newStore);

        // 파일 저장
        try {
            saveStoreFile();
        } catch (IOException e) {
            System.out.println("파일 저장 중 오류가 발생했습니다.");
            return false;
        }

        System.out.println("'" + newStoreName + "' 지점이 성공적으로 등록되었습니다.");
        return true; // 모든 과정 성공

    }

    // 신규 지점 등록 (GUI)
    public void registerNewStoreGUI(String newStoreName) throws IOException, IllegalArgumentException {
        // 중복 여부를 예외로 알림
        if (isDuplicateName(newStoreName)) {
            throw new IllegalArgumentException("이미 존재하는 지점명입니다.");
        }

        // MaxId 구하기
        int maxId = 0;
        for (Store store : stores) {
            if (store.getStoreId() > maxId) {
                maxId = store.getStoreId();
            }
        }
        int newId = maxId + 1;

        // stores에 newStore 더하기
        Store newStore = new Store(newId, newStoreName);
        stores.add(newStore);

        // 파일 저장
        saveStoreFile();
    }

    // 지점 삭제 (GUI)
    public void deleteStore(int storeId) throws IOException {
        // 1. stores 리스트에서 해당 ID를 가진 Store 객체를 찾아 제거
        Store storeToRemove = null;
        for (Store store : stores) {
            if (store.getStoreId() == storeId) {
                storeToRemove = store;
                break;
            }
        }

        if (storeToRemove != null) {
            stores.remove(storeToRemove);
        }

        // 2. 파일 저장
        saveStoreFile();
    }



}
