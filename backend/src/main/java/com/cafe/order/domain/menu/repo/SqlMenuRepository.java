package com.cafe.order.domain.menu.repo;

import com.cafe.order.domain.menu.dto.Category;
import com.cafe.order.domain.menu.entity.Menu;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.cafe.order.common.util.UUIDUtils.convertBytesToUUID;
import static com.cafe.order.common.util.UUIDUtils.convertUUIDToBytes;

//@Repository
public class SqlMenuRepository {

    private final JdbcTemplate jdbcTemplate;


    public SqlMenuRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // CREATE : 메뉴 추가
    public Menu save(Menu menu) {
        // 데이터 준비 : UUID 변환
        byte[] idBytes = convertUUIDToBytes(menu.getId());

        // 데이터 전달 : SQL 작성
        String sql = "INSERT INTO menus (id, name, price, category, description)" +
                "VALUES (?, ?, ?, ?, ?)";

        // 데이터 저장
        jdbcTemplate.update(sql, idBytes, menu.getName(), menu.getPrice(), menu.getCategory().name(), menu.getDescription());

        // 데이터 반환
        return menu;
    }

    // READ : 메뉴 전체 조회
    public List<Menu> findAll() {
        // 1. SQL 직접 작성
        String sql = "SELECT id, name, price, category, description FROM menus";

        // 2. 쿼리 실행 + RowMapper로 변환
        return jdbcTemplate.query(sql, menuRowMapper());
    }

    // READ : 메뉴 id로 상세 조회
    public Optional<Menu> findById(UUID uuid) {
        // SQL 준비
        String sql = "SELECT id, name, price, category, description FROM menus WHERE id = ?";

        // UUID 변환
        byte[] idBytes = convertUUIDToBytes(uuid);

        // DB에 요청
        try {
            Menu menu = jdbcTemplate.queryForObject(sql, menuRowMapper(), idBytes);
            return Optional.of(menu); // 성공
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty(); // 실패
        }
    }

    // UPDATE : 메뉴 수정
    public Menu update(Menu menu) {
        String sql = "UPDATE menus " +
                "SET name = ?, price = ?, category = ?, description = ?, " +
                "WHERE id = ?";

        // UUID 변환
        byte[] idBytes = convertUUIDToBytes(menu.getId());

        jdbcTemplate.update(sql, menu.getName(), menu.getPrice(), menu.getCategory().name(), menu.getDescription(), idBytes);

        return menu;
    }

    // DELETE : 메뉴 삭제
    public void deleteById(UUID uuid) {
        // UUID 변환
        byte[] idBytes = convertUUIDToBytes(uuid);

        // SQL 작성
        String sql = "DELETE FROM menus WHERE id = ?";

        // 삭제 실행
        jdbcTemplate.update(sql, idBytes);
    }



    // ResultSet을 Menu 객체로 변환하는 메서드
    private RowMapper<Menu> menuRowMapper() {

        return (rs, rowNum) -> {
            Menu menu = new Menu();

            // UUID 변환 (H2의 BINARY(16))
            byte[] idBytes = rs.getBytes("id");
            menu.setId(convertBytesToUUID(idBytes));

            // 나머지 필드
            menu.setName(rs.getString("name"));
            menu.setPrice(rs.getInt("price"));
            menu.setCategory(Category.valueOf(rs.getString("category")));
            menu.setDescription(rs.getString("description"));

            return menu;
        };
    }
}
