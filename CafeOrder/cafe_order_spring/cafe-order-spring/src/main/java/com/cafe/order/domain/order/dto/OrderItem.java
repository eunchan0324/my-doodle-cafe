package com.cafe.order.domain.order.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Menu와의 관계 (N:1)
    @Column(columnDefinition = "BINARY(16)", nullable = false)
    private UUID menuId;

    // 주문 시점의 메뉴 정보 (스냅샷)
    @Column(nullable = false, length = 100)
    private String menuName;

    @Column(nullable = false)
    private Integer menuPrice;

    // 옵션 정보
    @Column(length = 10)
    private String temperature; // HOT, ICE

    @Column(length = 20)
    private String cupType; // 일회용컵, 개인컵, 매장컵

    @Column(length = 50)
    private String options; // 샷추가, 얼음 추가 등

    // 수량 및 가격
    @Column(nullable = false)
    private Integer quantity; // 수량

    @Column(nullable = false)
    private Integer finalPrice; // 옵션 포함 최종 가격

    // 생성자
    public OrderItem(UUID menuId, String menuName, Integer menuPrice, String temperature, String cupType, String options, Integer quantity, Integer finalPrice) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.temperature = temperature;
        this.cupType = cupType;
        this.options = options;
        this.quantity = quantity;
        this.finalPrice = finalPrice;
    }
}
