package com.cafe.order.domain.order.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class Order {

    @Id
    @Column(columnDefinition = "BINARY(16)", name = "order_id")
    private UUID orderId;

    @Column(nullable = false, name = "customer_id")
    private String customerId;

    @Column(nullable = false, name = "store_id")
    private Integer storeId;

    @Column(nullable = false, updatable = false, name = "order_time")
    private LocalDateTime orderTime;

    @Column(nullable = false, name = "total_price")
    private Integer totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status;

    @Column(name = "waiting_number")
    private Integer waitingNumber;

    // OrderItem과의 관계 (1:N)
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    private List<OrderItem> items = new ArrayList<>();

    // 신규 주문 생성자
    public Order(String customerId, Integer storeId, Integer totalPrice, OrderStatus status, Integer waitingNumber) {
        this.orderId = UUID.randomUUID();
        this.customerId = customerId;
        this.storeId = storeId;
        this.orderTime = LocalDateTime.now();
        this.totalPrice = totalPrice;
        this.status = status;
        this.waitingNumber = waitingNumber;
    }

    // DB 로드 생성자
    public Order(UUID orderId, String customerId, Integer storeId, LocalDateTime orderTime, Integer totalPrice, OrderStatus status, Integer waitingNumber) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.storeId = storeId;
        this.orderTime = orderTime;
        this.totalPrice = totalPrice;
        this.status = status;
        this.waitingNumber = waitingNumber;
    }

    // 편의 메서드 : 메뉴 요약 (아메리카노 외 1건)
    public String getMenuSummary() {
        if (items == null || items.isEmpty()) {
            return "메뉴없음";
        }
        String firstMenu = items.get(0).getMenuName();
        int otherCount = items.size() - 1;
        return otherCount > 0 ? firstMenu + " 외" + otherCount + "건" : firstMenu;
    }

    // 편의 메서드 : 총 수량
    public int getTotalQuantity() {
        if (items == null) {
            return 0;
        }
        return items.stream()
                .mapToInt(OrderItem::getQuantity)
                .sum();
    }
}
