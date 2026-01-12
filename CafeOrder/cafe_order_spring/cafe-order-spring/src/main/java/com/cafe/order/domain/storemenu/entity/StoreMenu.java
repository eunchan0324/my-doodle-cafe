package com.cafe.order.domain.storemenu.entity;

import com.cafe.order.common.entity.BaseEntity;
import com.cafe.order.domain.menu.entity.Menu;
import com.cafe.order.domain.store.entity.Store;
import com.cafe.order.domain.storemenu.dto.RecommendType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "store_menus",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_store_menu",
            columnNames = {"store_id", "menu_id"}
        )
    })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreMenu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.PROTECTED) // RowMapper 를 위해 개방
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    @Setter(AccessLevel.PROTECTED)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    @Setter(AccessLevel.PROTECTED)
    private Menu menu;

    @Column(nullable = false)
    @Setter(AccessLevel.PROTECTED)
    private Integer stock;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Setter(AccessLevel.PROTECTED)
    private SalesStatus salesStatus;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "recommend_type", length = 20, nullable = false)
    private RecommendType recommendType = RecommendType.NONE; // 지점별 추천 여부, 기본값 지정


    // 생성자
    public StoreMenu(Store store, Menu menu, Integer stock, SalesStatus salesStatus, RecommendType recommendType) {
        this.store = store;
        this.menu = menu;
        this.stock = stock;
        // null이면 기본값 READY(준비중) 혹은 ON_SALE(판매중)로 설정. 정책에 따라 결정.
        this.salesStatus = salesStatus != null ? salesStatus : SalesStatus.READY;
        this.recommendType = recommendType != null ? recommendType : RecommendType.NONE;
    }

    // ============ [비즈니스 로직] ============

    /**
     * 재고(Stock) 증가 메서드 :
     * <p>SOLD_OUT 이고 stock > 0 이면 status를 ON_SALE로 변경</p>
     */
    public void increaseStock(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("재고 증가는 0이하로 할 수 없습니다.");
        }

        this.stock += amount;

        // [로직 변경] 품절(SOLD_OUT) 상태였는데 재고가 생기면 -> 판매중(ON_SALE)으로 변경
        if (this.salesStatus == SalesStatus.SOLD_OUT && this.stock > 0) {
            this.salesStatus = SalesStatus.ON_SALE;
        }
    }

    /**
     * 재고(Stock) 감소 메서드 :
     * <br>
     * - 감소 후 0미만이면 예외 <br>
     * - 감소 후 0이 되고, 현재 ON_SALE이면 SOLD_OUT으로 변경
     */
    public void decreaseStock(int amount) {
        // 1. amount 유효성 검사 (0 이하이면 예외)
        if (amount <= 0) {
            throw new IllegalArgumentException("재고 감소는 0이하로 할 수 없습니다.");
        }

        // 2. 감소 후의 값 검증 (this.stock - amount < 0 이면 예외)
        if (this.stock < amount) {
            throw new IllegalStateException("재고가 부족합니다. 현재 stock=" + this.stock);
        }

        // 3. stock 실제 감소
        this.stock -= amount;

        // 4. 재고가 0이 되고, 현재 판매중(ON_SALE)이면 -> 품절(SOLD_OUT)로 변경
        if (this.stock == 0 && this.salesStatus == SalesStatus.ON_SALE) {
            this.salesStatus = SalesStatus.SOLD_OUT;
        }
    }

    /**
     * 판매 가능 여부 메서드 :
     * <br>
     * - 상태가 ON_SALE, 재고가 1이상은 true <br>
     * - STOP/READY/SOLD_OUT은 false
     */
    public boolean isSellable() {
        // 상태가 ON_SALE이고 재고가 있어야 함
        return this.salesStatus == SalesStatus.ON_SALE && this.stock > 0;
    }

    /**
     * 판매 중지 메서드 :
     * <br>
     * - 어떤 상태에서든 이 메서드 호출 시 상태를 STOP 으로 설정 <br>
     * - 이미 STOP 이면 아무 작업도 하지 않는다 (idempotent) <br>
     * - 재고(stock)는 변경하지 않는다 <br>
     * - auto-status 규칙 (increase/decrease)와 연동되지 않는다
     */
    public void stopSelling() {
        this.salesStatus = SalesStatus.STOP;
    }

    /**
     * 판매 재개(restart) 메서드 :
     * <br>
     * - STOP 상태에서만 메서드 호출 가능 <br>
     * - 재고가 1이상이면 ON_SALE <br>
     * - 재고가 0이면 SOLD_OUT
     */
    public void resumeSelling() {
        if (this.salesStatus != SalesStatus.STOP) {
            throw new IllegalStateException("resumeSelling()은 STOP 상태에서만 호출 가능합니다.");
        }

        if (this.stock > 0) {
            this.salesStatus = SalesStatus.ON_SALE;
        } else {
            this.salesStatus = SalesStatus.SOLD_OUT;
        }
    }
}
