package com.cafe.order.domain.menustatus.repo;

import com.cafe.order.domain.menu.entity.Menu;
import com.cafe.order.domain.menu.repo.JpaMenuRepository;
import com.cafe.order.domain.storemenu.entity.SalesStatus;
import com.cafe.order.domain.store.entity.Store;
import com.cafe.order.domain.store.repo.JpaStoreRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

//@Repository
public class SqlSellerStockRepository {

    private final JdbcTemplate jdbcTemplate;
    private final JpaMenuRepository menuRepository;
    private final JpaStoreRepository storeRepository;

    public SqlSellerStockRepository(JdbcTemplate jdbcTemplate, JpaMenuRepository menuRepository, JpaStoreRepository storeRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.menuRepository = menuRepository;
        this.storeRepository = storeRepository;
    }

    /**
     * READ : Id.storeId로 List<MenuStatus> 조회
     */
    public List<MenuStatus> findByIdStoreId(Integer storeId) {
        String sql = "SELECT store_id, menu_id, status, stock " +
                "FROM menu_status WHERE store_id = ?";

        return jdbcTemplate.query(sql, getRowMapper(), storeId);
    }


    /**
     * READ : 복합키로 List<MenuStatus> 조회
     */
    public Optional<MenuStatus> findById(MenuStatusId id) {
        String sql = "SELECT store_id, menu_id, status, stock " +
                "FROM menu_status WHERE store_id = ? AND menu_id = ?";

        List<MenuStatus> result = jdbcTemplate.query(sql, getRowMapper(), id.getStoreId(), id.getMenuId());

        return result.stream().findFirst();
    }

    /**
     * UPDATE : MenuStatus 업데이트
     */
    public void save(MenuStatus ms) {
        String sql = "UPDATE menu_status " +
                "SET status = ?, stock = ? " +
                "WHERE store_id = ? AND menu_id = ?";

        jdbcTemplate.update(sql, ms.getStatus().name(), ms.getStock(), ms.getId().getStoreId(), ms.getId().getMenuId());
    }


    /**
     * RowMapper : 완전한 MenuStatus 객체 생성 (Menu + Store 포함)
     */
    private RowMapper<MenuStatus> getRowMapper() {
        return (rs, rowNum) -> {

            Integer storeId = rs.getInt("store_id");
            UUID menuId = rs.getObject("menu_id", UUID.class);

            MenuStatusId id = new MenuStatusId(storeId, menuId);

            // 연관 객체 조회 (JPA는 자동, SQL에서는 수동으로 해줘야 함)
            Store store = storeRepository.findById(storeId).orElseThrow(() -> new IllegalArgumentException("store not found: " + storeId));

            Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new IllegalArgumentException("Menu not found: " + menuId));

            // MenuStatus 생성
            return new MenuStatus(
                    store,
                    menu,
                    SalesStatus.valueOf(rs.getString("status")),
                    rs.getInt("stock")
            );
        };
    }

}
