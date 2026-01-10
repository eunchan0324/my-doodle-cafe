package com.cafe.order.domain.favorite.entity;

import com.cafe.order.domain.menu.entity.Menu;
import com.cafe.order.domain.user.entity.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.cafe.order.common.util.UUIDUtils.convertBytesToUUID;

public class FavoriteMenuRowMapper implements RowMapper<FavoriteMenu> {

    @Override
    public FavoriteMenu mapRow(ResultSet rs, int rowNum) throws SQLException {
        // 1. 빈 객체 생성
        var favoriteMenu = new FavoriteMenu();

        // 2. User 껍데기 객체 생성 (user_id 컬럼)
        int userId = rs.getInt("user_id");
        var user = new User();
        user.setId(userId);
        favoriteMenu.setUser(user);

        // 3. Menu 껍데기 객체 생성(menu_id 컬럼)
        byte[] menuIdBytes = rs.getBytes("menu_id");
        UUID menuId = convertBytesToUUID(menuIdBytes);
        var menu = new Menu();
        menu.setId(menuId);
        favoriteMenu.setMenu(menu);

        // 4. 복합키 생성 및 주입
        FavoriteMenuId id = new FavoriteMenuId(userId, menuId);
        favoriteMenu.setId(id);

        // 5. 생성일시 매핑
        java.sql.Timestamp timestamp = rs.getTimestamp("created_at");
        LocalDateTime createdAt = (timestamp != null) ? timestamp.toLocalDateTime() : null;
        favoriteMenu.setCreatedAt(createdAt);

        return favoriteMenu;
    }
}
