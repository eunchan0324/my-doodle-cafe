package com.cafe.order.domain.store.repo;

import com.cafe.order.domain.store.entity.Store;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Optional;

//@Repository
public class SqlStoreRepository {

    private final JdbcTemplate jdbcTemplate;

    public SqlStoreRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // CREATE : 지점 추가
    public Store save(Store store) {
        String sql = "INSERT INTO stores (name) VALUES (?)";

        jdbcTemplate.update(sql, store.getName());

        return store;
    }

    // READ : 전체 지점 조회
    public List<Store> findAll() {
        String sql = "SELECT id, name FROM stores";

        return jdbcTemplate.query(sql, storeRowMapper());
    }

    // READ : id로 store 객체 반환
    public Optional<Store> findById(Integer id) {
        String sql = "SELECT id, name FROM stores WHERE id = ?";

        try {
            Store store = jdbcTemplate.queryForObject(sql, storeRowMapper(), id);
            return Optional.of(store); // 성공
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty(); // 실패
        }
    }

    // UPDATE : 지점 수정
    public Store update(Store store) {
        String sql = "UPDATE stores SET name = ?  WHERE id = ?";

        jdbcTemplate.update(sql, store.getName(), store.getId());

        return store;
    }

    // DELETE : 지점 삭제
    public void deleteById(Integer id) {
        String sql = "DELETE FROM stores WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    // ResultSet을 Store 객체로 반환하는 메서드
    private RowMapper<Store> storeRowMapper() {

        return (rs, rowNum) -> {
            Store store = new Store();
            store.setId(rs.getInt("id"));
            store.setName(rs.getString("name"));
            return store;
        };
    }


}
