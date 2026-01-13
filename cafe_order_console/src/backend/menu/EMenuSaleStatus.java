package backend.menu;

public enum EMenuSaleStatus {
    AVAILABLE("판매중"),
    SOLD_OUT("품절");

    private final String displayStatus;

    EMenuSaleStatus(String displayStatus) {
        this.displayStatus = displayStatus;
    }

    public String getDisplayStatus() {
        return displayStatus;
    }
}
