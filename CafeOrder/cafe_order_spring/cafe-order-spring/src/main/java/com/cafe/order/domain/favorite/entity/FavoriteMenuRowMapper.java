package com.cafe.order.domain.favorite.entity;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.cafe.order.common.util.UUIDUtils.convertBytesToUUID;

public class FavoriteMenuRowMapper implements RowMapper<FavoriteMenu> {

    @Override
    public FavoriteMenu mapRow(ResultSet rs, int rowNum) throws SQLException {

        // 1. Customer ID 추출 (String) todo : 로그인 기능 이후 수정
        String customerId = rs.getString("customer_id");

        // 2. Menu ID 추출 (Bytes -> UUID 변환)
        byte[] menuIdBytes = rs.getBytes("menu_id");
        UUID menuId = convertBytesToUUID(menuIdBytes);

        // 3. 복합 키 객체 생성
        FavoriteMenuId id = new FavoriteMenuId(customerId, menuId);

        // 4. 생성일시 추출 (Timestamp -> LocalDateTiem)
        java.sql.Timestamp timestamp = rs.getTimestamp("created_at");
        LocalDateTime createdAt = (timestamp != null) ? timestamp.toLocalDateTime() : null;

        // 5. 엔티티 생성 및 반환 (조회용 생성자 사용)
        return new FavoriteMenu(id, createdAt);
    }
}
