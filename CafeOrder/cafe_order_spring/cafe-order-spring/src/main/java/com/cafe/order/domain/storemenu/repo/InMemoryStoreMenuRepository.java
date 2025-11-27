package com.cafe.order.domain.storemenu.repo;

import com.cafe.order.domain.menu.dto.Category;
import com.cafe.order.domain.menu.dto.Menu;
import com.cafe.order.domain.storemenu.dto.RecommendType;
import com.cafe.order.domain.storemenu.dto.StoreMenu;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

//@Repository
public class InMemoryStoreMenuRepository {

    private final List<StoreMenu> storeMenus = new ArrayList<>();
    private Integer nextId = 1;

    // 생성자: 초기 데이터 없음
    public InMemoryStoreMenuRepository() {
        // 비어있음 - 판매자가 직접 추가할 것
    }

    // 판매 메뉴 관리 + 추천 메뉴 관리
    /**
     * CREATE/UPDATE : 판매 메뉴 추가/수정
     * - ID가 없으면 INSERT
     * - ID가 있으면 UPDATE
     */
    public StoreMenu save(StoreMenu storeMenu) {
        if (storeMenu.getId() == null) {
            // INSERT : 새 ID 할당 후 추가
            storeMenu.setId(nextId++);
            storeMenus.add(storeMenu);
        } else {
            // UPDATE : 기존 데이터 찾아서 수정
            for (int i = 0; i < storeMenus.size(); i++) {
                if (storeMenus.get(i).getId().equals(storeMenu.getId())) {
                    storeMenus.set(i, storeMenu); // 교체
                    break;
                }
            }
        }
        return storeMenu;
    }

    // 판매 메뉴 관리
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


    // 추천 메뉴 관리
    /**
     * READ : storeId와 menuId로 StoreMenu 조회
     *
     * @param storeId
     * @param menuId
     * @return
     */
    public Optional<StoreMenu> findByStoreIdAndMenuId(Integer storeId, UUID menuId) {
        for (StoreMenu sm : storeMenus) {
            if (sm.getStoreId().equals(storeId) && sm.getMenuId().equals(menuId)) {
                return Optional.of(sm);
            }
        }
        return Optional.empty();
    }


}
