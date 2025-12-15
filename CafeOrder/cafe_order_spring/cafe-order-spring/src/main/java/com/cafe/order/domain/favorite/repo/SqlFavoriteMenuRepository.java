package com.cafe.order.domain.favorite.repo;

import com.cafe.order.domain.favorite.entity.FavoriteMenu;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class SqlFavoriteMenuRepository {

    private final JdbcTemplate jdbcTemplate;

    /**
     * READ : 고객의 찜 목록 전체 조회
     */
    public List<FavoriteMenu> findById_CustomerId(String customerId) {
        String sql = "SELECT customer_id, menu_id, created_at FROM favorite_menu WHERE customer_id = ?";

        return jdbcTemplate.query(sql, , customerId);


    }



}
