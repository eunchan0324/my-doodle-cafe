package com.cafe.order.domain.storemenu.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "store_menus")
@Getter
@Setter
@NoArgsConstructor
public class StoreMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "store_id", nullable = false)
    private Integer storeId;

    @Column(name = "menu_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID menuId;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable = true; // 판매 가능 여부 (재고 관리)

    @Enumerated(EnumType.STRING)
    @Column(name = "recommend_type", length = 20)
    private RecommendType recommendType; // 지점별 추천 여부, null = 추천 아님

    public StoreMenu(Integer storeId, UUID menuId) {
        this.storeId = storeId;
        this.menuId = menuId;
        this.isAvailable = true;
        this.recommendType = null; // 기본은 추천 x
    }
}
