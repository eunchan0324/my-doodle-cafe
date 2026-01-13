package com.cafe.order.domain.order.entity;

import com.cafe.order.common.entity.BaseEntity;
import com.cafe.order.domain.order.dto.OrderStatus;
import com.cafe.order.domain.store.entity.Store;
import com.cafe.order.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class Order extends BaseEntity {

    @Id
    @Column(columnDefinition = "BINARY(16)", name = "order_id")
    private UUID orderId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false, updatable = false, name = "order_time")
    private LocalDateTime orderTime;

    @Column(nullable = false, name = "total_price")
    private Integer totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status;

    @Column(name = "waiting_number")
    private Integer waitingNumber;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    // 신규 주문 생성자
    public Order(User user, Store store, Integer totalPrice, OrderStatus status, Integer waitingNumber) {
        this.orderId = UUID.randomUUID();
        this.user = user;
        this.store = store;
        this.orderTime = LocalDateTime.now();
        this.totalPrice = totalPrice;
        this.status = status;
        this.waitingNumber = waitingNumber;
    }

    // DB 로드 생성자
    public Order(UUID orderId, User user, Store store, LocalDateTime orderTime, Integer totalPrice, OrderStatus status, Integer waitingNumber) {
        this.orderId = orderId;
        this.user = user;
        this.store = store;
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

    // 편의 메서드 : 연관관계
    public void addOrderItem(OrderItem item) {
        this.items.add(item);
        item.setOrder(this);
    }
}
