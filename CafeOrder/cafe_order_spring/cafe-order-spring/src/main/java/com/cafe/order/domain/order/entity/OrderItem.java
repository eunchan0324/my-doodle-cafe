package com.cafe.order.domain.order.entity;

import com.cafe.order.common.entity.BaseEntity;
import com.cafe.order.domain.menu.dto.CupType;
import com.cafe.order.domain.menu.dto.ShotOption;
import com.cafe.order.domain.menu.dto.Temperature;
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
public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // Menu와의 관계 (N:1)
    @Column(columnDefinition = "BINARY(16)", nullable = false, name = "menu_id")
    private UUID menuId;

    // 주문 시점의 메뉴 정보 (스냅샷)
    @Column(nullable = false, length = 100, name = "menu_name")
    private String menuName;

    @Column(nullable = false, name = "menu_price")
    private Integer menuPrice;

    // 옵션 정보
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Temperature temperature; // HOT, ICE

    @Enumerated(EnumType.STRING)
    @Column(name = "cup_type", nullable = false)
    private CupType cupType; // 일회용컵, 개인컵, 매장컵

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShotOption options; // 샷추가, 기본, 오트우유 등

    // 수량 및 가격
    @Column(nullable = false)
    private Integer quantity; // 수량

    @Column(nullable = false, name = "final_price")
    private Integer finalPrice; // 옵션 포함 최종 가격

    // 생성자
    public OrderItem(UUID menuId, String menuName, Integer menuPrice, Temperature temperature, CupType cupType, ShotOption options, Integer quantity, Integer finalPrice) {
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
