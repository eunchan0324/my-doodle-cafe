package com.cafe.order.domain.storemenu.dto;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.jdbc.core.RowMapper;

import java.util.UUID;

import static com.cafe.order.common.util.UUIDUtils.convertBytesToUUID;

@Entity
@Table(name = "store_menus")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "store_id", nullable = false)
    private Integer storeId;

    @Column(name = "menu_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID menuId;

    // todo : MenuStatus 안에 SalesStatus와 중복되는 느낌이므로 확인 필요
    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable = true; // 판매 가능 여부 (재고 관리)

    @Enumerated(EnumType.STRING)
    @Column(name = "recommend_type", length = 20, nullable = false)
    private RecommendType recommendType = RecommendType.NONE; // 지점별 추천 여부, 기본값 지정

    public StoreMenu(Integer storeId, UUID menuId) {
        this.storeId = storeId;
        this.menuId = menuId;
        this.isAvailable = true;
        this.recommendType = RecommendType.NONE; // 기본값
    }


}
