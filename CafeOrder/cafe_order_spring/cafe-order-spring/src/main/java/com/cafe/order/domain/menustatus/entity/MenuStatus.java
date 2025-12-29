package com.cafe.order.domain.menustatus.entity;

import com.cafe.order.common.entity.BaseEntity;
import com.cafe.order.domain.menu.entity.Menu;
import com.cafe.order.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Getter
@Table
@Entity
public class MenuStatus extends BaseEntity {

    @EmbeddedId
    private MenuStatusId id;

    @MapsId("storeId") // 연관관계로 들어온 엔티티의 PK 값을 EmbeddedId의 PK 값에 “복사해서” 매핑하라는 뜻.
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @MapsId("menuId")
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Enumerated(EnumType.STRING)
    private SalesStatus status;

    private int stock;

    public MenuStatus(Store store, Menu menu, SalesStatus status, int stock) {
        this.store = store;
        this.menu = menu;
        this.status = status;
        this.stock = stock;
        this.id = new MenuStatusId(store.getId(), menu.getId());
    }

    /**
     * SQL Repo 전용 생성자
     */
    public MenuStatus(MenuStatusId id, SalesStatus status, int stock) {
        this.id = id;
        this.status = status;
        this.stock = stock;
    }


    /**
     * 재고(Stock) 증가 메서드 :
     * <p>SOLD_OUT 이고 stock > 0 이면 status를 ON_SALE로 변경</p>
     */
    public void increaseStock(int amount) {
        // 1. amount 유효성 검사 (0 이하이면 예외)
        if (amount <= 0) {
            throw new IllegalArgumentException("재고 증가는 0이하로 할 수 없습니다. amount=" + amount);
        }

        // 2. stock 증가
        stock += amount;

        // 3. 상태 자동 변경 규칙 : SOLD_OUT 이고 stock > 0 이면 ON_SALE로 변경
        if (status == SalesStatus.SOLD_OUT && stock > 0) {
            status = SalesStatus.ON_SALE;
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
            throw new IllegalArgumentException("재고 감소는 0이하로 할 수 없습니다. amount=" + amount);
        }

        // 2. 감소 후의 값 검증 (this.stock - amount < 0 이면 예외)
        if (stock - amount < 0) {
            throw new IllegalStateException("재고 감소 결과는 0미만이 될 수 없습니다. 현재 stock=" + stock);
        }

        // 3. stock 실제 감소
        stock -= amount;

        // 4. 상태 자동 변경 규칙 : 재고가 0이면서 상태가 ON_SALE 이라면 SOLD_OUT 으로 변경
        //   - STOP/READY → 상태 변경 X
        if (stock == 0 && status == SalesStatus.ON_SALE) {
            status = SalesStatus.SOLD_OUT;
        }
    }

    /**
     * 판매 가능 여부 메서드 :
     * <br>
     * - 상태가 ON_SALE, 재고가 1이상은 true <br>
     * - STOP/READY/SOLD_OUT은 false
     */
    public boolean isSellable() {
        return status == SalesStatus.ON_SALE && stock > 0;
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
        status = SalesStatus.STOP;
    }

    /**
     * 판매 재개(restart) 메서드 :
     * <br>
     * - STOP 상태에서만 메서드 호출 가능 <br>
     * - 재고가 1이상이면 ON_SALE <br>
     * - 재고가 0이면 SOLD_OUT
     */
    public void resumeSelling() {
        // 예외처리 : status가 STOP이 아닌 모든 경우
        if (status != SalesStatus.STOP) {
            throw new IllegalStateException("resumeSelling()은 STOP 상태에서만 호출 가능합니다.");
        }

        if (stock > 0) {
            status = SalesStatus.ON_SALE; // 재고가 1이상이면 ON_SALE <br>
        } else {
            status = SalesStatus.SOLD_OUT; // 재고가 0이면 SOLD_OUT
        }
    }
}
