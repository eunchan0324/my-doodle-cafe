package backend.order;

public enum OrderStatus {
    ORDER_PLACED(0),
    PREPARING(1),
    READY(2),
    COMPLETED(3);

    private final int value;

    OrderStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static OrderStatus fromValue(int value) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        return null;
    }
}
