-- ============================
-- 1. Menus (메뉴 마스터 데이터)
-- ============================
INSERT INTO menus (id, name, price, category, description)
VALUES (RANDOM_UUID(), '아메리카노', 4500, 'COFFEE', '깊고 진한 에스프레소에 물을 더한 커피'),
       (RANDOM_UUID(), '카페라떼', 5000, 'COFFEE', '에스프레소와 스팀 우유의 조화'),
       (RANDOM_UUID(), '카푸치노', 5000, 'COFFEE', '에스프레소와 우유 거품의 완벽한 균형'),
       (RANDOM_UUID(), '바닐라라떼', 5500, 'COFFEE', '달콤한 바닐라 시럽이 들어간 라떼'),
       (RANDOM_UUID(), '카라멜마끼아또', 5500, 'COFFEE', '카라멜 시럽과 우유, 에스프레소'),
       (RANDOM_UUID(), '녹차라떼', 5500, 'BEVERAGE', '고소한 녹차와 우유'),
       (RANDOM_UUID(), '초코라떼', 5500, 'BEVERAGE', '진한 초콜릿과 우유'),
       (RANDOM_UUID(), '딸기스무디', 6000, 'BEVERAGE', '신선한 딸기로 만든 스무디'),
       (RANDOM_UUID(), '치즈케이크', 6000, 'DESSERT', '부드러운 크림치즈 케이크'),
       (RANDOM_UUID(), '티라미수', 6500, 'DESSERT', '이탈리아 전통 디저트');


-- ============================
-- 2. Stores (지점 데이터)
-- ============================
INSERT INTO stores (name)
VALUES ('강남점'),
       ('홍대점'),
       ('신촌점'),
       ('잠실점'),
       ('판교점');


-- ============================
-- 3. Users (사용자 데이터)
-- 비밀번호: 1234
-- ============================
INSERT INTO users (login_id, password, name, role, store_id)
VALUES ('admin', '$2a$10$6xA3ErUBejb1ruhD7PsPx.XxuR6Ws46MsiugfZcdoOyNMY8q9dkg.', '관리자', 'ADMIN', NULL);

INSERT INTO users (login_id, password, name, role, store_id)
VALUES ('seller1', '$2a$10$6xA3ErUBejb1ruhD7PsPx.XxuR6Ws46MsiugfZcdoOyNMY8q9dkg.', '판매자1', 'SELLER', 1),
       ('seller2', '$2a$10$6xA3ErUBejb1ruhD7PsPx.XxuR6Ws46MsiugfZcdoOyNMY8q9dkg.', '판매자2', 'SELLER', 2),
       ('seller3', '$2a$10$6xA3ErUBejb1ruhD7PsPx.XxuR6Ws46MsiugfZcdoOyNMY8q9dkg.', '판매자3', 'SELLER', 3);

INSERT INTO users (login_id, password, name, role, store_id)
VALUES ('customer1', '$2a$10$6xA3ErUBejb1ruhD7PsPx.XxuR6Ws46MsiugfZcdoOyNMY8q9dkg.', '구매자1', 'CUSTOMER', NULL),
       ('customer2', '$2a$10$6xA3ErUBejb1ruhD7PsPx.XxuR6Ws46MsiugfZcdoOyNMY8q9dkg.', '구매자2', 'CUSTOMER', NULL);


-- ============================
-- 4. Store Menus (지점별 판매 메뉴) 
-- ★ 중요: is_available 제거 -> stock, sales_status 추가됨
-- ============================

-- 강남점(1) : 아메리카노 (판매중, 재고 100, 추천: BEST)
INSERT INTO store_menus (store_id, menu_id, stock, sales_status, recommend_type)
SELECT 1, id, 100, 'ON_SALE', 'BEST'
FROM menus WHERE name = '아메리카노';

-- 강남점(1) : 카페라떼 (판매중지, 재고 50, 추천: NEW)
INSERT INTO store_menus (store_id, menu_id, stock, sales_status, recommend_type)
SELECT 1, id, 50, 'STOP', 'NEW'
FROM menus WHERE name = '카페라떼';

-- 강남점(1) : 카푸치노 (품절, 재고 0, 추천: NONE)
INSERT INTO store_menus (store_id, menu_id, stock, sales_status, recommend_type)
SELECT 1, id, 0, 'SOLD_OUT', 'NONE'
FROM menus WHERE name = '카푸치노';

-- 홍대점(2) : 티라미수 (판매중, 재고 20, 추천: NONE)
INSERT INTO store_menus (store_id, menu_id, stock, sales_status, recommend_type)
SELECT 2, id, 20, 'ON_SALE', 'NONE'
FROM menus WHERE name = '티라미수';


-- ============================
-- 5. Orders (주문 데이터)
-- ============================
INSERT INTO orders (order_id, user_id, store_id, order_time, total_price, status, waiting_number)
VALUES
    (X'550e8400e29b41d4a716446655440001', 5, 1, DATEADD('MINUTE', -5, NOW()), 5000, 'ORDER_PLACED', 1),
    (X'550e8400e29b41d4a716446655440002', 6, 1, DATEADD('MINUTE', -10, NOW()), 11000, 'PREPARING', 2),
    (X'550e8400e29b41d4a716446655440003', 5, 1, DATEADD('MINUTE', -15, NOW()), 6500, 'READY', 3),
    (X'550e8400e29b41d4a716446655440004', 6, 1, DATEADD('DAY', -1, NOW()), 4500, 'COMPLETED', 1),
    (X'550e8400e29b41d4a716446655440005', 5, 2, DATEADD('MINUTE', -20, NOW()), 10000, 'PREPARING', 1),
    (X'550e8400e29b41d4a716446655440006', 6, 2, DATEADD('DAY', -2, NOW()), 12000, 'COMPLETED', 1),
    (X'550e8400e29b41d4a716446655440007', 5, 2, DATEADD('MINUTE', -3, NOW()), 5500, 'READY', 2),
    (X'550e8400e29b41d4a716446655440008', 6, 3, DATEADD('DAY', -3, NOW()), 15000, 'COMPLETED', 1),
    (X'550e8400e29b41d4a716446655440009', 5, 3, DATEADD('MINUTE', -30, NOW()), 6000, 'PREPARING', 1);


-- ============================
-- 6. Order Items (주문 상세)
-- 주의: menu_id를 직접 Hex로 넣지 않고, menus 테이블의 이름으로 찾아서 넣음 (무결성 보장)
-- ============================

-- 강남점 주문 1
INSERT INTO order_items (order_id, menu_id, menu_name, menu_price, temperature, cup_type, options, quantity, final_price)
VALUES (X'550e8400e29b41d4a716446655440001', (SELECT id FROM menus WHERE name = '아메리카노'), '아메리카노', 5000, 'ICE', 'DISPOSABLE', 'BASIC', 1, 5000);

-- 강남점 주문 2
INSERT INTO order_items (order_id, menu_id, menu_name, menu_price, temperature, cup_type, options, quantity, final_price)
VALUES (X'550e8400e29b41d4a716446655440002', (SELECT id FROM menus WHERE name = '카페라떼'), '카페라떼', 5500, 'HOT', 'PERSONAL', 'BASIC', 2, 11000);

-- 강남점 주문 3
INSERT INTO order_items (order_id, menu_id, menu_name, menu_price, temperature, cup_type, options, quantity, final_price)
VALUES
    (X'550e8400e29b41d4a716446655440003', (SELECT id FROM menus WHERE name = '아메리카노'), '아메리카노', 3000, 'HOT', 'DISPOSABLE', 'BASIC', 1, 3000),
    (X'550e8400e29b41d4a716446655440003', (SELECT id FROM menus WHERE name = '녹차라떼'), '녹차라떼', 3500, 'ICE', 'STORE', 'NONE', 1, 3500);

-- 강남점 주문 4
INSERT INTO order_items (order_id, menu_id, menu_name, menu_price, temperature, cup_type, options, quantity, final_price)
VALUES (X'550e8400e29b41d4a716446655440004', (SELECT id FROM menus WHERE name = '아메리카노'), '아메리카노', 4500, 'ICE', 'DISPOSABLE', 'NONE', 1, 4500);

-- 홍대점 주문 5
INSERT INTO order_items (order_id, menu_id, menu_name, menu_price, temperature, cup_type, options, quantity, final_price)
VALUES (X'550e8400e29b41d4a716446655440005', (SELECT id FROM menus WHERE name = '카페라떼'), '카페라떼', 5000, 'HOT', 'DISPOSABLE', 'BASIC', 2, 10000);

-- 홍대점 주문 6
INSERT INTO order_items (order_id, menu_id, menu_name, menu_price, temperature, cup_type, options, quantity, final_price)
VALUES (X'550e8400e29b41d4a716446655440006', (SELECT id FROM menus WHERE name = '아메리카노'), '아메리카노', 6000, 'ICE', 'DISPOSABLE', 'BASIC', 2, 12000);

-- 홍대점 주문 7
INSERT INTO order_items (order_id, menu_id, menu_name, menu_price, temperature, cup_type, options, quantity, final_price)
VALUES
    (X'550e8400e29b41d4a716446655440007', (SELECT id FROM menus WHERE name = '아메리카노'), '아메리카노', 3000, 'ICE', 'DISPOSABLE', 'BASIC', 1, 3000),
    (X'550e8400e29b41d4a716446655440007', (SELECT id FROM menus WHERE name = '녹차라떼'), '녹차라떼', 2500, 'HOT', 'PERSONAL', 'NONE', 1, 2500);

-- 신촌점 주문 8
INSERT INTO order_items (order_id, menu_id, menu_name, menu_price, temperature, cup_type, options, quantity, final_price)
VALUES (X'550e8400e29b41d4a716446655440008', (SELECT id FROM menus WHERE name = '카페라떼'), '카페라떼', 5000, 'HOT', 'DISPOSABLE', 'BASIC', 3, 15000);

-- 신촌점 주문 9
INSERT INTO order_items (order_id, menu_id, menu_name, menu_price, temperature, cup_type, options, quantity, final_price)
VALUES (X'550e8400e29b41d4a716446655440009', (SELECT id FROM menus WHERE name = '아메리카노'), '아메리카노', 3000, 'ICE', 'DISPOSABLE', 'BASIC', 2, 6000);