package com.cafe.order.domain.order.repo;

import com.cafe.order.common.util.UUIDUtils;
import com.cafe.order.domain.menu.dto.CupType;
import com.cafe.order.domain.menu.dto.ShotOption;
import com.cafe.order.domain.menu.dto.Temperature;
import com.cafe.order.domain.order.entity.Order;
import com.cafe.order.domain.order.entity.OrderItem;
import com.cafe.order.domain.order.dto.OrderStatus;
import com.cafe.order.domain.store.entity.Store;
import com.cafe.order.domain.user.entity.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.cafe.order.common.util.UUIDUtils.convertBytesToUUID;
import static com.cafe.order.common.util.UUIDUtils.convertUUIDToBytes;

//@Repository
public class SqlOrderRepository {

    private final JdbcTemplate jdbcTemplate;

    public SqlOrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 관리자용
     */
    // READ : status = COMPLETE인 전체 지점 매출 조회
    public List<Order> findByStatus(OrderStatus status) {
        String sql = "SELECT order_id, customer_id, store_id, order_time, total_price, status, waiting_number FROM orders WHERE status = ?";

        return jdbcTemplate.query(sql, orderRowMapper(), status.name());
    }


    /**
     * 판매자용
     */
    // READ : storeId로 주문 목록 조회 (List<Order>)
    public List<Order> findByStoreId(Integer storeId) {
        String sql = "SELECT order_id, customer_id, store_id, order_time, total_price, status, waiting_number " +
                "FROM orders WHERE store_id = ?";

        List<Order> orders = jdbcTemplate.query(sql, orderRowMapper(), storeId);

        // 각 Order마다 OrderItem 조회
        for (Order order : orders) {
            String itemSql = "SELECT id, order_id, menu_id, menu_name, menu_price, temperature, cup_type, options, quantity, final_price " +
                    "FROM order_items WHERE order_id = ?";
            byte[] orderIdBytes = convertUUIDToBytes(order.getOrderId());
            List<OrderItem> items = jdbcTemplate.query(itemSql, orderItemRowMapper(), orderIdBytes);
            order.setItems(items);
        }

        return orders;
    }

    // READ : orderId로 Optional<Order> 조회
    public Optional<Order> findById(UUID orderId) {
        String orderSql = "SELECT order_id, customer_id, store_id, order_time, total_price, status, waiting_number " +
                "FROM orders WHERE order_id = ?";

        byte[] orderIdBytes = convertUUIDToBytes(orderId);

        try {
            // 1. Order 조회
            Order order = jdbcTemplate.queryForObject(orderSql, orderRowMapper(), orderIdBytes);

            // 2. OrderItem 조회
            String itemSql = "SELECT id, order_id, menu_id, menu_name, menu_price, temperature, cup_type, options, quantity, final_price " +
                    "FROM order_items WHERE order_id = ?";

            List<OrderItem> items = jdbcTemplate.query(itemSql, orderItemRowMapper(), orderIdBytes);

            // 3. Order에 items 설정
            order.setItems(items);

            return Optional.of(order);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }


    // READ : storeId, OrderStatus로 List<Order> 조회
    public List<Order> findByStoreIdAndStatus(Integer storeId, OrderStatus status) {
        String sql = "SELECT order_id, customer_id, store_id, order_time, total_price, status, waiting_number " +
                "FROM orders WHERE store_id = ? AND status = ?";

        List<Order> orders = jdbcTemplate.query(sql, orderRowMapper(), storeId, status.name());

        // 각 Order마다 OrderItem 조회
        for (Order order : orders) {
            String itemSql = "SELECT id, order_id, menu_id, menu_name, menu_price, temperature, cup_type, options, quantity, final_price " +
                    "FROM order_items WHERE order_id = ?";
            byte[] orderIdBytes = convertUUIDToBytes(order.getOrderId());
            List<OrderItem> items = jdbcTemplate.query(itemSql, orderItemRowMapper(), orderIdBytes);
            order.setItems(items);
        }

        return orders;
    }


    // UPDATE : order -> newOrder로 변경
    public Order update(Order order) {
        String sql = "UPDATE orders " +
                "SET customer_id = ?, store_id = ?, order_time = ?, total_price = ?," +
                "status = ?, waiting_number = ? " +
                "WHERE order_id = ?";

        // UUID 변환
        byte[] orderIdBytes = convertUUIDToBytes(order.getOrderId());

        jdbcTemplate.update(sql,
                order.getUser().getLoginId(),
                order.getStore().getId(),
                order.getOrderTime(),
                order.getTotalPrice(),
                order.getStatus().name(),
                order.getWaitingNumber(),
                orderIdBytes);

        return order;
    }

    /**
     * 구매자용
     */
    /**
     * READ : 주문 목록 확인
     */
    public List<Order> findByStoreIdAndCustomerId(Integer storeId, String customerId) {
        String sql = "SELECT order_id, customer_id, order_time, status, store_id, total_price, waiting_number " +
                "FROM orders WHERE store_id = ? AND customer_id = ?";

        List<Order> orders = jdbcTemplate.query(sql, orderRowMapper(), storeId, customerId);

        // 각 Order마다 OrderItem 조회
        for (Order order : orders) {
            String itemSql = "SELECT id, order_id, menu_id, menu_name, menu_price, temperature, cup_type, options, quantity, final_price " +
                    "FROM order_items WHERE order_id = ?";
            byte[] orderIdBytes = convertUUIDToBytes(order.getOrderId());
            List<OrderItem> items = jdbcTemplate.query(itemSql, orderItemRowMapper(), orderIdBytes);
            order.setItems(items);
        }

        return orders;
    }


    /**
     * RowMapper
     */
    // ResultSet -> Order 변환
    public RowMapper<Order> orderRowMapper() {
        return ((rs, rowNum) -> {
            Order order = new Order();

            // UUID 변환
            byte[] orderIdBytes = rs.getBytes("order_id");
            order.setOrderId(UUIDUtils.convertBytesToUUID(orderIdBytes));

            String dbCustomerId = rs.getString("customer_id");
            User fakeUser = new User();
            fakeUser.setLoginId(dbCustomerId);
            order.setUser(fakeUser);

            int dbStoreId = rs.getInt("store_id");
            Store fakeStore = new Store();
            fakeStore.setId(dbStoreId);
            order.setStore(fakeStore);

            order.setOrderTime(rs.getTimestamp("order_time").toLocalDateTime());
            order.setTotalPrice(rs.getInt("total_price"));
            order.setStatus(OrderStatus.valueOf(rs.getString("status")));

            // waitingNumber는 null 가능
            int waitingNumber = rs.getInt("waiting_number");
            order.setWaitingNumber(rs.wasNull() ? null : waitingNumber);

            return order;
        });
    }

    // OrderItem용 RowMapper
    private RowMapper<OrderItem> orderItemRowMapper() {
        return ((rs, rowNum) -> {
            OrderItem item = new OrderItem();

            item.setId(rs.getInt("id")); // 수정, 삭제 대비

            byte[] orderIdByte = rs.getBytes("order_id");
            UUID orderId = convertBytesToUUID(orderIdByte);

            Order fakeOrder = new Order();
            fakeOrder.setOrderId(orderId);

            item.setOrder(fakeOrder);

            byte[] menuIdByte = rs.getBytes("menu_id");
            item.setMenuId(convertBytesToUUID(menuIdByte));

            item.setMenuName(rs.getString("menu_name"));

            item.setMenuPrice(rs.getInt("menu_price"));

            item.setTemperature(rs.getString("temperature") == null ? null :
                    Temperature.valueOf(rs.getString("temperature")));

            item.setCupType(rs.getString("cup_type") == null ? null :
                            CupType.valueOf(rs.getString("cup_type")));

            item.setOptions(rs.getString("options") == null ? null :
                            ShotOption.valueOf(rs.getString("options")));

            item.setQuantity(rs.getInt("quantity"));

            item.setFinalPrice(rs.getInt("final_price"));

            return item;
        });
    }
}