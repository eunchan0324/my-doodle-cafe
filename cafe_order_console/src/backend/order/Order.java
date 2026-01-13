package backend.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

// Order 기능
public class Order {
    private UUID orderId;
    private String customerId;
    private int storeId;
    private LocalDateTime orderTime;
    private int totalPrice;
    private OrderStatus status;
    private final ArrayList<OrderItem> items = new ArrayList<>();
    private int waitingNumber;


    // 신규 주문용 생성자
    public Order(String customerId, int storeId, int totalPrice, OrderStatus status, ArrayList<OrderItem> items) {
        this.orderId = UUID.randomUUID();
        this.customerId = customerId;
        this.storeId = storeId;
        this.orderTime = LocalDateTime.now();
        this.totalPrice = totalPrice;
        this.status = status;
        this.items.addAll(items);
    }

    // 파일 로드용 생성자
    public Order(UUID orderId, String customerId, int storeId, LocalDateTime orderTime, int totalPrice, OrderStatus status, int waitingNumber) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.storeId = storeId;
        this.orderTime = orderTime;
        this.totalPrice = totalPrice;
        this.status = status;
        this.waitingNumber = waitingNumber;
    }


    public UUID getOrderId() {
        return orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public ArrayList<OrderItem> getItems() {
        return items;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getWaitingNumber() {
        return waitingNumber;
    }

    public void setWaitingNumber(int waitingNumber) {
        this.waitingNumber = waitingNumber;
    }

}
