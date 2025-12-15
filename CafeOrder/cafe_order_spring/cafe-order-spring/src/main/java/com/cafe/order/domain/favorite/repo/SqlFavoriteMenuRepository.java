package com.cafe.order.domain.favorite.repo;

import com.cafe.order.domain.favorite.entity.FavoriteMenu;
import com.cafe.order.domain.favorite.entity.FavoriteMenuId;
import com.cafe.order.domain.favorite.entity.FavoriteMenuRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class SqlFavoriteMenuRepository {

    private final JdbcTemplate jdbcTemplate;

    /**
     * READ : 고객의 찜 목록 전체 조회
     */
    public List<FavoriteMenu> findById_CustomerId(String customerId) {
        String sql = "SELECT customer_id, menu_id, created_at FROM favorite_menu WHERE customer_id = ?";

        return jdbcTemplate.query(sql, new FavoriteMenuRowMapper(), customerId);
    }

    /**
     * READ : 특정 고객이 특정 메뉴를 찜했는지 여부를 조회 (메뉴 상세 화면용)
     */
    public boolean existsById_CustomerIdAndId_MenuId(String customerId, UUID menuId) {
        String sql = "SELECT count(*) FROM favorite_menu WHERE customer_id = ? AND menu_id = ?";

        // Integer.class: 반환 타입을 정수로 지정
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, customerId, menuId);

        // 결과 판단 (null 체크 및 0보다 큰지 확인)
        return count != null && count > 0;
    }

    /**
     * READ : 복합 키 객체(favoriteMenuId)를 받아서 존재 여부 확인
     */
    public boolean existsById(FavoriteMenuId id) {
        String sql = "SELECT count(*) FROM favorite_menu WHERE customer_id = ? AND menu_id = ?";

        // id 객체에서 값을 꺼내 파라미터로 전달
        Integer count = jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                id.getCustomerId(),
                id.getMenuId()
        );

        return count != null && count > 0;
    }

    /**
     * DELETE : 찜 해제
     */
    public void deleteById(FavoriteMenuId id) {
        String sql = "DELETE FROM favorite_menu WHERE customer_id = ? AND menu_id = ?";

        jdbcTemplate.update(sql, id.getCustomerId(), id.getMenuId());
    }

    /**
     * CREATE : 찜 저장
     */
    public void save(FavoriteMenu favoriteMenu) {
        String sql = "INSERT INTO favorite_menu (customer_id, menu_id, created_at) VALUES (?, ?, ?)";

        jdbcTemplate.update(
                sql,
                favoriteMenu.getId().getCustomerId(),
                favoriteMenu.getId().getMenuId(),
                favoriteMenu.getCreatedAt()
        );
    }
}
