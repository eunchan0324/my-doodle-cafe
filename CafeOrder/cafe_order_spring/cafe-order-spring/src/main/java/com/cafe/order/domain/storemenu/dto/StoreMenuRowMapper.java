package com.cafe.order.domain.storemenu.dto;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.cafe.order.common.util.UUIDUtils.convertBytesToUUID;

public class StoreMenuRowMapper implements RowMapper<StoreMenu> {

    @Override
    public StoreMenu mapRow(ResultSet rs, int rowNum) throws SQLException {

        StoreMenu storeMenu = new StoreMenu(); // ✔ protected 생성자 접근 가능(같은 패키지)

        storeMenu.setId(rs.getInt("id"));
        storeMenu.setStoreId(rs.getInt("store_id"));

        // UUID 변환
        byte[] menuIdBytes = rs.getBytes("menu_id");
        storeMenu.setMenuId(convertBytesToUUID(menuIdBytes));

        storeMenu.setIsAvailable(rs.getBoolean("is_available"));

        // Enum 변환 (NUll체크)
        String recommendTypeStr = rs.getString("recommend_type");
        storeMenu.setRecommendType(
                recommendTypeStr != null ? RecommendType.valueOf(recommendTypeStr) : RecommendType.NONE);

        return storeMenu;
    }

}
