package com.cafe.order.domain.storemenu.dto;

import com.cafe.order.domain.menu.dto.Category;
import com.cafe.order.domain.storemenu.entity.SalesStatus;
import com.cafe.order.domain.storemenu.entity.StoreMenu;
import lombok.Getter;

import java.util.UUID;

@Getter
public class SellerMenuResponse {

    private final UUID menuId;
    private final String name;
    private final int price;
    private final Category category;

    // 판매자에게 중요한 운영 정보
    private final int stock;
    private final SalesStatus status;
    private final RecommendType recommendType;

    /**
     * StoreMenu 엔티티를 받아서 바로 DTO 변환하는 생성자
     */
    public SellerMenuResponse(StoreMenu storeMenu) {
        this.menuId = storeMenu.getMenu().getId();
        this.name = storeMenu.getMenu().getName();
        this.price = storeMenu.getMenu().getPrice();
        this.category = storeMenu.getMenu().getCategory();

        this.stock = storeMenu.getStock();
        this.status = storeMenu.getSalesStatus();
        this.recommendType = storeMenu.getRecommendType();
    }
}
