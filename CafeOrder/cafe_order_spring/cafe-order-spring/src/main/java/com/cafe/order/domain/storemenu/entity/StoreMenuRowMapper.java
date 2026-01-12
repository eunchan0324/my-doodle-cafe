package com.cafe.order.domain.storemenu.entity;

import com.cafe.order.domain.menu.entity.Menu;
import com.cafe.order.domain.store.entity.Store;
import com.cafe.order.domain.storemenu.dto.RecommendType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static com.cafe.order.common.util.UUIDUtils.convertBytesToUUID;

public class StoreMenuRowMapper implements RowMapper<StoreMenu> {

    @Override
    public StoreMenu mapRow(ResultSet rs, int rowNum) throws SQLException {
        StoreMenu storeMenu = new StoreMenu(); // ✔ protected 생성자 접근 가능(같은 패키지)

        // 기본 필드 매핑
        storeMenu.setId(rs.getInt("id"));
        storeMenu.setStock(rs.getInt("stock"));
        storeMenu.setSalesStatus(SalesStatus.valueOf(rs.getString("sales_status")));

        String recommendTypeStr = rs.getString("recommend_type");
        storeMenu.setRecommendType(
            recommendTypeStr != null ? RecommendType.valueOf(recommendTypeStr) : RecommendType.NONE);

        int dbStoreId = rs.getInt("store_id");
        var fakeStore = new Store();
        fakeStore.setId(dbStoreId);
        storeMenu.setStore(fakeStore);

        // UUID 변환
        byte[] menuIdBytes = rs.getBytes("menu_id");
        if (menuIdBytes != null) {
            UUID dbMenuId = convertBytesToUUID(menuIdBytes);
            var fakeMenu = new Menu();
            fakeMenu.setId(dbMenuId);
            storeMenu.setMenu(fakeMenu);
        }

        return storeMenu;
    }
}
