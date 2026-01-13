package com.cafe.order.domain.favorite.repo;

import com.cafe.order.domain.favorite.entity.FavoriteMenu;
import com.cafe.order.domain.favorite.entity.FavoriteMenuId;
import com.cafe.order.domain.favorite.entity.FavoriteMenuRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import static com.cafe.order.common.util.UUIDUtils.convertUUIDToBytes;

@RequiredArgsConstructor
@Repository
public class SqlFavoriteMenuRepository {

    private final JdbcTemplate jdbcTemplate;

    /**
     * READ : 고객의 찜 목록 전체 조회
     */
    public List<FavoriteMenu> findById_CustomerId(Integer userId) {
        String sql = "SELECT user_id, menu_id, created_at FROM favorite_menu WHERE customer_id = ?";

        return jdbcTemplate.query(sql, new FavoriteMenuRowMapper(), userId);
    }

    /**
     * READ : 특정 고객이 특정 메뉴를 찜했는지 여부를 조회 (메뉴 상세 화면용)
     */
    public boolean existsById_CustomerIdAndId_MenuId(Integer userId, UUID menuId) {
        String sql = "SELECT count(*) FROM favorite_menu WHERE user_id = ? AND menu_id = ?";

        // menuId는 DB에 따라 바이트로 변환이 필요할 수 있음 (여기선 UUIDUtils 사용)
        byte[] menuIdBytes = convertUUIDToBytes(menuId);

        // Integer.class: 반환 타입을 정수로 지정
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId, menuId);

        // 결과 판단 (null 체크 및 0보다 큰지 확인)
        return count != null && count > 0;
    }

    /**
     * READ : 복합 키 객체(favoriteMenuId)를 받아서 존재 여부 확인
     */
    public boolean existsById(FavoriteMenuId id) {
        String sql = "SELECT count(*) FROM favorite_menu WHERE user_id = ? AND menu_id = ?";

        byte[] menuIdBytes = convertUUIDToBytes(id.getMenuId());

        // id 객체에서 값을 꺼내 파라미터로 전달
        Integer count = jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                id.getUserId(),
                id.getMenuId()
        );

        return count != null && count > 0;
    }

    /**
     * DELETE : 찜 해제
     */
    public void deleteById(FavoriteMenuId id) {
        String sql = "DELETE FROM favorite_menu WHERE user_id = ? AND menu_id = ?";

        byte[] menuIdBytes = convertUUIDToBytes(id.getMenuId());

        jdbcTemplate.update(sql, id.getUserId(), menuIdBytes);
    }

    /**
     * CREATE : 찜 저장
     */
    public void save(FavoriteMenu favoriteMenu) {
        String sql = "INSERT INTO favorite_menu (user_id, menu_id, created_at) VALUES (?, ?, ?)";

        // 1. User 객체에서 ID 추출
        Integer userId = favoriteMenu.getUser().getId();

        // 2. Menu 객체에서 ID 추출 후 바이트 변환
        byte[] menuIdBytes = convertUUIDToBytes(favoriteMenu.getMenu().getId());

        jdbcTemplate.update(
            sql,
            userId,
            menuIdBytes,
            favoriteMenu.getCreatedAt()
        );
    }
}
