package com.cafe.order.domain.user.repo;

import com.cafe.order.domain.store.entity.Store;
import com.cafe.order.domain.user.entity.User;
import com.cafe.order.domain.user.entity.UserRole;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Optional;

//@Repository
public class SqlUserRepository {

    private final JdbcTemplate jdbcTemplate;

    public SqlUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // CREATE : seller 계정 DB 추가
    public User save(User user) {
        String sql = "INSERT INTO users (login_id, password, name, role, store_id) VALUES (?, ? ,? ,?, ?)";

        Integer storeId = null;
        if (user.getStore() != null) {
            storeId = user.getStore().getId();
        }

        jdbcTemplate.update(sql, user.getLoginId(), user.getPassword(), user.getName(), user.getRole().name(), storeId);

        return user;
    }

    // READ : user role로 해당 role User List 반환
    public List<User> findByRole(UserRole role) {
        String sql = "SELECT id, login_id, password, name, role, store_id FROM users WHERE role = ?";

        return jdbcTemplate.query(sql, userRowMapper(), role.name()); // Enum -> 문자열로 전달
    }

    // READ : User id로 해당 id User 반환
    public Optional<User> findById(Integer id) {
        String sql = "SELECT id, login_id, password, name, role, store_id FROM users WHERE id = ?";

        try {
            User user = jdbcTemplate.queryForObject(sql, userRowMapper(), id);
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    // UPDATE : seller 계정 DB 수정
    public User update(User seller) {
        String sql = "UPDATE users SET password = ?, name = ?, store_id = ? WHERE id = ?";

        // storeId 추출 (Null 체크)
        Integer storeId = null;
        if (seller.getStore() != null) {
            storeId = seller.getStore().getId();
        }

        jdbcTemplate.update(sql, seller.getPassword(), seller.getName(), storeId, seller.getId());

        return seller;
    }

    // DELETE : seller id로 해당 계정 DB 삭제
    public void deleteById(Integer id) {
        String sql = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    // ResultSet -> User 객체로 반환하는 RowMapper 메서드 (람다식)
    private RowMapper<User> userRowMapper() {

        return (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setLoginId(rs.getString("login_id"));
            user.setPassword(rs.getString("password"));
            user.setName(rs.getString("name"));
            user.setRole(UserRole.valueOf(rs.getString("role")));

            // store_id가 NULL이면 null로 설정, 아니면 값 설정 (ADMIN, CUSTOMER 고려)
            Integer storeId = rs.getObject("store_id", Integer.class);

            if (storeId != null) {
                Store fakeStore = new Store();
                fakeStore.setId(storeId);

                user.setStore(fakeStore);
            }

            return user;
        };
    }

//     ResultSet -> User 객체로 반환하는 RowMapper 메서드 (익명 클래스로 구현한 원시 코드)
//    private RowMapper<User> userRowMapper() {
//        return new RowMapper<User>() {
//            @Override
//            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
//                User user = new User();
//                user.setId(rs.getInt("id"));
//                user.setUsername(rs.getString("username"));
//                user.setPassword(rs.getString("password"));
//                user.setRole(UserRole.valueOf(rs.getString("role")));
//                user.setStoreId(rs.getInt("store_id"));
//
//                return user;
//            }
//        };
//    }

    // ResultSet -> User 객체로 반환하는 RowMapper 메서드 (내부 클래스로 구현한 원시 코드)
//    private RowMapper<User> userRowMapper() {
//
//        // 메서드 내부에 클래스 선언
//        class UserRowMapperImpl implements RowMapper<User> {
//            @Override
//            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
//                User user = new User();
//                user.setId(rs.getInt("id"));
//                user.setUsername(rs.getString("username"));
//                user.setPassword(rs.getString("password"));
//                user.setRole(UserRole.valueOf(rs.getString("role")));
//                user.setStoreId(rs.getInt("store_id"));
//
//                return user;
//            }
//        }
//        // 내부 클래스 인스턴스 생성
//        RowMapper<User> userRowMapper = new UserRowMapperImpl();
//
//        // 반환
//        return userRowMapper;
//    }

    /**
     * loginId로 회원 찾기 (로그인용)
     */
    public Optional<User> findByLoginId(String loginId) {
        String sql = "SELECT id, login_id, password, name, role, store_id FROM users WHERE login_id = ?";

        try {
            // queryForObject는 결과가 0개면 에러가 터지므로 try-catch 필수
            User user = jdbcTemplate.queryForObject(sql, userRowMapper(), loginId);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty(); // 결과가 없으면 빈 Optional 반환
        }
    }

}
