package com.cafe.order.domain.menustatus.repo;

import com.cafe.order.domain.menu.entity.Menu;
import com.cafe.order.domain.menu.repo.InMemoryMenuRepository;
import com.cafe.order.domain.menustatus.entity.MenuStatus;
import com.cafe.order.domain.menustatus.entity.MenuStatusId;
import com.cafe.order.domain.storemenu.entity.SalesStatus;
import com.cafe.order.domain.store.entity.Store;
import com.cafe.order.domain.store.repo.InMemoryStoreRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

//@Repository
public class InMemorySellerStockRepository {

    // 복합키 기반 저장소
    private final Map<MenuStatusId, MenuStatus> store = new ConcurrentHashMap<>();

    private final InMemoryMenuRepository menuRepository;
    private final InMemoryStoreRepository storeRepository;


    public InMemorySellerStockRepository(
            InMemoryMenuRepository menuRepository,
            InMemoryStoreRepository storeRepository
    ) {
        this.menuRepository = menuRepository;
        this.storeRepository = storeRepository;
        initDummyData();
    }

    /**
     * READ : storeId로 List<MenuStatus> 조회
     */
    public List<MenuStatus> findByIdStoreId(Integer storeId) {
        return store.values().stream()
                .filter(ms -> ms.getId().getStoreId().equals(storeId))
                .collect(Collectors.toList());
    }

    /**
     * READ : 복합키로 단건 조회
     */
    public Optional<MenuStatus> findById(MenuStatusId id) {
        return Optional.ofNullable(store.get(id));
    }

    /**
     * UPDATE : MenuStatus 저장 (덮어쓰기)
     */
    public void save(MenuStatus ms) {
        store.put(ms.getId(), ms);
    }

    // =======================================================
    // 초기 데이터 세팅 (storeId=1을 기준으로 생성)
    // =======================================================
    private void initDummyData() {
        Integer storeId = 1;
        Store storeEntity = storeRepository.findById(storeId).orElseThrow();

        // 메뉴 전체 조회
        List<Menu> menus = menuRepository.findAll();

        int i = 1;
        for (Menu menu : menus) {

            SalesStatus status = (i % 3 == 0)
                    ? SalesStatus.SOLD_OUT
                    : SalesStatus.ON_SALE;

            int stock = (status == SalesStatus.SOLD_OUT) ? 0 : 10;

            MenuStatusId id = new MenuStatusId(storeId, menu.getId());

            MenuStatus ms = new MenuStatus(
                    storeEntity,
                    menu,
                    status,
                    stock
            );

            store.put(id, ms);
            i++;
        }
    }


}
