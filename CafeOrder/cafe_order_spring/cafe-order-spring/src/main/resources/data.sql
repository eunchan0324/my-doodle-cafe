-- 메뉴 테스트 데이터
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


-- 지점 테스트 데이터
INSERT INTO stores (name)
VALUES ('강남점'),
       ('홍대점'),
       ('신촌점'),
       ('잠실점'),
       ('판교점');


-- 사용자 테스트 데이터
-- 주의: 모든 계정의 비밀번호는 '1234' 입니다. (BCrypt 암호화 적용됨)
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
-- orders
-- ============================

INSERT INTO orders (order_id, customer_id, store_id, order_time, total_price, status, waiting_number)
VALUES
    (X'550e8400e29b41d4a716446655440001', 'customer1', 1, DATEADD('MINUTE', -5, NOW()), 5000, 'ORDER_PLACED', 1),
    (X'550e8400e29b41d4a716446655440002', 'customer2', 1, DATEADD('MINUTE', -10, NOW()), 11000, 'PREPARING', 2),
    (X'550e8400e29b41d4a716446655440003', 'customer1', 1, DATEADD('MINUTE', -15, NOW()), 6500, 'READY', 3),
    (X'550e8400e29b41d4a716446655440004', 'customer2', 1, DATEADD('DAY', -1, NOW()), 4500, 'COMPLETED', 1);

INSERT INTO orders (order_id, customer_id, store_id, order_time, total_price, status, waiting_number)
VALUES
    (X'550e8400e29b41d4a716446655440005', 'customer1', 2, DATEADD('MINUTE', -20, NOW()), 10000, 'PREPARING', 1),
    (X'550e8400e29b41d4a716446655440006', 'customer2', 2, DATEADD('DAY', -2, NOW()), 12000, 'COMPLETED', 1),
    (X'550e8400e29b41d4a716446655440007', 'customer1', 2, DATEADD('MINUTE', -3, NOW()), 5500, 'READY', 2);

INSERT INTO orders (order_id, customer_id, store_id, order_time, total_price, status, waiting_number)
VALUES
    (X'550e8400e29b41d4a716446655440008', 'customer2', 3, DATEADD('DAY', -3, NOW()), 15000, 'COMPLETED', 1),
    (X'550e8400e29b41d4a716446655440009', 'customer1', 3, DATEADD('MINUTE', -30, NOW()), 6000, 'PREPARING', 1);


-- ============================
-- order_items
-- ============================

-- 강남점 주문 1
INSERT INTO order_items
(order_id, menu_id, menu_name, menu_price, temperature, cup_type, options, quantity, final_price)
VALUES
    (X'550e8400e29b41d4a716446655440001',
     X'a0eebc99999b4d6eb3c9aa91c1e4e000',
     '아메리카노', 5000, 'ICE', 'DISPOSABLE', 'BASIC', 1, 5000);

-- 강남점 주문 2
INSERT INTO order_items
(order_id, menu_id, menu_name, menu_price, temperature, cup_type, options, quantity, final_price)
VALUES
    (X'550e8400e29b41d4a716446655440002',
     X'a0eebc99999b4d6eb3c9aa91c1e4e001',
     '카페라떼', 5500, 'HOT', 'PERSONAL', 'BASIC', 2, 11000);

-- 강남점 주문 3
INSERT INTO order_items
(order_id, menu_id, menu_name, menu_price, temperature, cup_type, options, quantity, final_price)
VALUES
    (X'550e8400e29b41d4a716446655440003',
     X'a0eebc99999b4d6eb3c9aa91c1e4e000',
     '아메리카노', 3000, 'HOT', 'DISPOSABLE', 'BASIC', 1, 3000),
    (X'550e8400e29b41d4a716446655440003',
     X'a0eebc99999b4d6eb3c9aa91c1e4e002',
     '녹차라떼', 3500, 'ICE', 'STORE', 'NONE', 1, 3500);

-- 강남점 주문 4
INSERT INTO order_items
(order_id, menu_id, menu_name, menu_price, temperature, cup_type, options, quantity, final_price)
VALUES
    (X'550e8400e29b41d4a716446655440004',
     X'a0eebc99999b4d6eb3c9aa91c1e4e000',
     '아메리카노', 4500, 'ICE', 'DISPOSABLE', 'NONE', 1, 4500);

-- 홍대점 주문 5
INSERT INTO order_items
(order_id, menu_id, menu_name, menu_price, temperature, cup_type, options, quantity, final_price)
VALUES
    (X'550e8400e29b41d4a716446655440005',
     X'a0eebc99999b4d6eb3c9aa91c1e4e001',
     '카페라떼', 5000, 'HOT', 'DISPOSABLE', 'BASIC', 2, 10000);

-- 홍대점 주문 6
INSERT INTO order_items
(order_id, menu_id, menu_name, menu_price, temperature, cup_type, options, quantity, final_price)
VALUES
    (X'550e8400e29b41d4a716446655440006',
     X'a0eebc99999b4d6eb3c9aa91c1e4e000',
     '아메리카노', 6000, 'ICE', 'DISPOSABLE', 'BASIC', 2, 12000);

-- 홍대점 주문 7
INSERT INTO order_items
(order_id, menu_id, menu_name, menu_price, temperature, cup_type, options, quantity, final_price)
VALUES
    (X'550e8400e29b41d4a716446655440007',
     X'a0eebc99999b4d6eb3c9aa91c1e4e000',
     '아메리카노', 3000, 'ICE', 'DISPOSABLE', 'BASIC', 1, 3000),
    (X'550e8400e29b41d4a716446655440007',
     X'a0eebc99999b4d6eb3c9aa91c1e4e002',
     '녹차라떼', 2500, 'HOT', 'PERSONAL', 'NONE', 1, 2500);

-- 신촌점 주문 8
INSERT INTO order_items
(order_id, menu_id, menu_name, menu_price, temperature, cup_type, options, quantity, final_price)
VALUES
    (X'550e8400e29b41d4a716446655440008',
     X'a0eebc99999b4d6eb3c9aa91c1e4e001',
     '카페라떼', 5000, 'HOT', 'DISPOSABLE', 'BASIC', 3, 15000);

-- 신촌점 주문 9
INSERT INTO order_items
(order_id, menu_id, menu_name, menu_price, temperature, cup_type, options, quantity, final_price)
VALUES
    (X'550e8400e29b41d4a716446655440009',
     X'a0eebc99999b4d6eb3c9aa91c1e4e000',
     '아메리카노', 3000, 'ICE', 'DISPOSABLE', 'BASIC', 2, 6000);


-- ============================
-- store_menus 재생성
-- ============================

DROP TABLE IF EXISTS store_menus;

CREATE TABLE store_menus (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             store_id INT NOT NULL,
                             menu_id BINARY(16) NOT NULL,
                             is_available BOOLEAN DEFAULT TRUE NOT NULL,
                             recommend_type VARCHAR(20),
                             UNIQUE(store_id, menu_id)
);

INSERT INTO store_menus (store_id, menu_id, is_available, recommend_type)
SELECT 1, id, true, 'BEST'
FROM menus WHERE name = '아메리카노' LIMIT 1;

INSERT INTO store_menus (store_id, menu_id, is_available, recommend_type)
SELECT 1, id, true, 'NEW'
FROM menus WHERE name = '카페라떼' LIMIT 1;

INSERT INTO store_menus (store_id, menu_id, is_available, recommend_type)
SELECT 1, id, true, 'NONE'
FROM menus WHERE name = '카푸치노' LIMIT 1;


-- ============================
-- menu_status
-- ============================

INSERT INTO menu_status (store_id, menu_id, status, stock)
SELECT 1, id, 'ON_SALE', 10
FROM menus WHERE name = '아메리카노' LIMIT 1;

INSERT INTO menu_status (store_id, menu_id, status, stock)
SELECT 1, id, 'STOP', 5
FROM menus WHERE name = '카페라떼' LIMIT 1;

INSERT INTO menu_status (store_id, menu_id, status, stock)
SELECT 1, id, 'SOLD_OUT', 0
FROM menus WHERE name = '카푸치노' LIMIT 1;

INSERT INTO menu_status (store_id, menu_id, status, stock)
SELECT 2, id, 'ON_SALE', 7
FROM menus WHERE name = '티라미수' LIMIT 1;
