package com.cafe.order.domain.storemenu.repo;

import com.cafe.order.domain.storemenu.entity.StoreMenu;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import com.cafe.order.domain.storemenu.entity.StoreMenuRowMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.cafe.order.common.util.UUIDUtils.convertUUIDToBytes;

//@Repository
public class SqlStoreMenuRepository {

    private final JdbcTemplate jdbcTemplate;

    public SqlStoreMenuRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 판매 메뉴 관리 + 추천 메뉴 관리
    /**
     * CREATE/UPDATE : 판매 메뉴 추가/수정
     * - ID가 없으면 INSERT
     * - ID가 있으면 UPDATE
     */
    public StoreMenu save(StoreMenu storeMenu) {
        // 1. 객체에서 값 추출
        Integer storeId = storeMenu.getStore().getId();
        byte[] menuIdBytes = convertUUIDToBytes(storeMenu.getMenu().getId());

        // 2. Enum -> String 변환
        String recommendTypeStr = storeMenu.getRecommendType().name();
        String salesStatusStr = storeMenu.getSalesStatus().name();
        Integer stock = storeMenu.getStock();

        if (storeMenu.getId() == null) {
            // INSERT
            String sql = "INSERT INTO store_menus (store_id, menu_id, stock, sales_status, recommend_type) " +
                    "VALUES (?, ?, ?, ?, ?)";

            jdbcTemplate.update(sql,
                    storeId,
                    menuIdBytes,
                    stock,
                    salesStatusStr,
                    recommendTypeStr
            );
        } else {
            // UPDATE
            String sql = "UPDATE store_menus " +
                    "SET store_id = ?, menu_id = ?, stock = ?, sales_status = ?, recommend_type = ? " +
                    "WHERE id = ?";

            jdbcTemplate.update(sql,
                    storeId,
                    menuIdBytes,
                    stock,
                    salesStatusStr,
                    recommendTypeStr,
                    storeMenu.getId());
        }

        return storeMenu;
    }

    // 판매 메뉴 관리
    /**
     * READ : 지점의 판매 메뉴 조회
     */
    public List<StoreMenu> findByStoreId(Integer storeId) {
        String sql = "SELECT id, store_id, menu_id, stock, sales_status, recommend_type " +
            "FROM store_menus WHERE store_id = ?";

        return jdbcTemplate.query(sql, new StoreMenuRowMapper(), storeId);
    }

    /**
     * DELETE : ID로 삭제
     */
    public void deleteById(Integer id) {
        String sql = "DELETE FROM store_menus WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    // 추천 메뉴 관리
    /**
     * READ : storeId와 menuId로 StoreMenu 조회
     *
     * @return Optional<StoreMenu> (없으면 empty)
     */
    public Optional<StoreMenu> findByStoreIdAndMenuId(Integer storeId, UUID menuId) {
        String sql = "SELECT id, store_id, menu_id, stock, sales_status, recommend_type " +
            "FROM store_menus WHERE store_id = ? AND menu_id = ?";

        byte[] menuIdByte = convertUUIDToBytes(menuId);

        try {
            StoreMenu storeMenu = jdbcTemplate.queryForObject(sql, new StoreMenuRowMapper(), storeId, menuIdByte);
            return Optional.of(storeMenu);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
