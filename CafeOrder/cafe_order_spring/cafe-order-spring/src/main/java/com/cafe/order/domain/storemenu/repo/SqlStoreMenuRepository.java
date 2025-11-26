package com.cafe.order.domain.storemenu.repo;

import com.cafe.order.domain.storemenu.dto.RecommendType;
import com.cafe.order.domain.storemenu.dto.StoreMenu;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

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

        return jdbcTemplate.query(sql, storeMenuRowMapper(), storeId);
    }

    /**
     * DELETE : ID로 삭제
     */
    public void deleteById(Integer id) {
        String sql = "DELETE FROM store_menus WHERE id = ?";
        jdbcTemplate.update(sql, id);

    }



    /**
     * RowMapper : ResultSet -> StoreMenu 변환
     */
    private RowMapper<StoreMenu> storeMenuRowMapper() {
        return (rs, rowNum) -> {
            StoreMenu storeMenu = new StoreMenu();
            storeMenu.setId(rs.getInt("id"));
            storeMenu.setStoreId(rs.getInt("store_id"));

            // UUID 변환
            byte[] menuIdBytes = rs.getBytes("menu_id");
            storeMenu.setMenuId(convertBytesToUUID(menuIdBytes));

            storeMenu.setIsAvailable(rs.getBoolean("is_available"));

            // Enum 변환 (NUll체크)
            String recommendTypeStr = rs.getString("recommend_type");
            if (recommendTypeStr != null) {
                storeMenu.setRecommendType(RecommendType.valueOf(recommendTypeStr));
            } else {
                storeMenu.setRecommendType(null);
            }

            return storeMenu;
        };
    }


}
