package com.cafe.order.domain.storemenu.repo;

import com.cafe.order.domain.storemenu.dto.RecommendType;
import com.cafe.order.domain.storemenu.dto.StoreMenu;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.cafe.order.domain.storemenu.dto.StoreMenuRowMapper;

import java.util.List;

import static com.cafe.order.common.util.UUIDUtils.convertBytesToUUID;
import static com.cafe.order.common.util.UUIDUtils.convertUUIDToBytes;

@Repository
public class SqlStoreMenuRepository {

    private final JdbcTemplate jdbcTemplate;

    public SqlStoreMenuRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * CREATE : 판매 메뉴 추가
     */
    public StoreMenu save(StoreMenu storeMenu) {
        String sql = "INSERT INTO store_menus (store_id, menu_id, is_available, recommend_type) VALUES (?, ?, ?, ?)";

        byte[] menuIdBytes = convertUUIDToBytes(storeMenu.getMenuId());

        jdbcTemplate.update(sql, storeMenu.getStoreId(), menuIdBytes, storeMenu.getIsAvailable(),
                storeMenu.getRecommendType() != null ? storeMenu.getRecommendType().name() : null);

        return storeMenu;
    }


    /**
     * READ : 지점의 판매 메뉴 조회
     */
    public List<StoreMenu> findByStoreId(Integer storeId) {
        String sql = "SELECT id, store_id, menu_id, is_available, recommend_type FROM store_menus WHERE store_id = ?";

        return jdbcTemplate.query(sql, new StoreMenuRowMapper(), storeId);
    }

    /**
     * DELETE : ID로 삭제
     */
    public void deleteById(Integer id) {
        String sql = "DELETE FROM store_menus WHERE id = ?";
        jdbcTemplate.update(sql, id);

    }


}
