package backend.menu;

import java.util.UUID;

public class MenuStatus {
    private int storeId;
    private UUID menuId;
    private EMenuSaleStatus status;
    private int stock;

    public MenuStatus(int storeId, UUID menuId, EMenuSaleStatus status, int stock) {
        this.storeId = storeId;
        this.menuId = menuId;
        this.status = status;
        this.stock = stock;
    }

    public UUID getMenuId() {
        return menuId;
    }

    public int getStoreId() {
        return storeId;
    }

    public EMenuSaleStatus getStatus() {
        return status;
    }

    public void setStatus(EMenuSaleStatus status) {
        this.status = status;
    }

    public int getStock() {
        return stock;
    }

    public boolean setStock(int stock) {
        // 음수 검증
        if (stock < 0) {
            return false;
        }

        this.stock = stock;
        return true;
    }

    @Override
    public String toString() {
        return "MenuStatus{" +
                " storeId=" + storeId +
                ", menuId=" + menuId +
                ", status='" + status + '\'' +
                ", stock=" + stock +
                '}';
    }

}
