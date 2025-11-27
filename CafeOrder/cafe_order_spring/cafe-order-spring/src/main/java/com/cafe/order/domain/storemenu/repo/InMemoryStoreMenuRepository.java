package com.cafe.order.domain.storemenu.repo;

import com.cafe.order.domain.menu.dto.Category;
import com.cafe.order.domain.menu.dto.Menu;
import com.cafe.order.domain.storemenu.dto.RecommendType;
import com.cafe.order.domain.storemenu.dto.StoreMenu;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//@Repository
public class InMemoryStoreMenuRepository {

    private final List<StoreMenu> storeMenus = new ArrayList<>();
    private Integer nextId = 1;

    // 생성자: 초기 데이터 없음
    public InMemoryStoreMenuRepository() {
        // 비어있음 - 판매자가 직접 추가할 것
    }

    /**
     * CREATE : 판매 메뉴 추가
     */
    public StoreMenu save(StoreMenu storeMenu) {
        storeMenu.setId(nextId++);
        storeMenus.add(storeMenu);
        return storeMenu;
    }

    /**
     * READ : 지점의 판매 메뉴 조회
     */
    public List<StoreMenu> findByStoreId(Integer storeId) {
        List<StoreMenu> result = new ArrayList<>();
        for (StoreMenu sm : storeMenus) {
            if (sm.getStoreId().equals(storeId)) {
                result.add(sm);
            }
        }
        return result;
    }

    /**
     * DELETE : ID로 삭제
     */
    public void deleteById(Integer id) {
        storeMenus.removeIf(sm -> sm.getId().equals(id));
    }


}
