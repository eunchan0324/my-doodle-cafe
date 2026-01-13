package backend.store;

// Store 기능
public class Store {
    private int storeId;
    private String storeName;

    // 생성자 (신규 지점 생성, 기존 파일 로드 시)
    public Store(int storeId, String storeName) {
        this.storeId = storeId;
        this.storeName = storeName;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}
