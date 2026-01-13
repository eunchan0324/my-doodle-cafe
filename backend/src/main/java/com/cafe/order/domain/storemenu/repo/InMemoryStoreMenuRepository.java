package com.cafe.order.domain.storemenu.repo;

import com.cafe.order.domain.storemenu.entity.StoreMenu;

import java.lang.reflect.Field;
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
            // INSERT : 새 ID 할당
            assignId(storeMenu, nextId++);

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
            if (sm.getStore().getId().equals(storeId)) {
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
            if (sm.getStore().getId().equals(storeId) && sm.getMenu().getId().equals(menuId)) {
                return Optional.of(sm);
            }
        }
        return Optional.empty();
    }

    // ==========================================
    // [Helper] 리플렉션을 이용한 ID 할당 메서드
    // ==========================================
    private void assignId(StoreMenu entity, Integer id) {
        try {
            // StoreMenu 클래스의 'id' 필드를 찾음 (상속받은 경우 부모 클래스 확인 필요할 수 있음)
            // 여기선 StoreMenu에 id 필드가 있다고 가정 (만약 BaseEntity 등 부모에 있다면 getSuperclass() 필요)
            Field field = StoreMenu.class.getDeclaredField("id");
            field.setAccessible(true); // private/protected 접근 허용
            field.set(entity, id); // 값 주입
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("ID 할당 실패", e);
        }
    }
}
