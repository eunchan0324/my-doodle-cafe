package com.cafe.order.domain.menu.repo;

import com.cafe.order.domain.menu.dto.Category;
import com.cafe.order.domain.menu.entity.Menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

//@Repository
public class InMemoryMenuRepository {
    // List 타입으로 선언 (인터페이스 타입)
    private final List<Menu> menus;

    // 생성자에서 직접 초기화
    public InMemoryMenuRepository() {
        this.menus = new ArrayList<>();

        menus.add(new Menu("아메리카노", 4500, Category.COFFEE, "깊고 진한 에스프레소에 물을 더한 커피"));
        menus.add(new Menu("카페라떼", 5000, Category.COFFEE, "에스프레소와 스팀 우유의 조화"));
        menus.add(new Menu("카푸치노", 5000, Category.COFFEE, "에스프레소와 우유 거품의 완벽한 균형"));
        menus.add(new Menu("바닐라라떼", 5500, Category.COFFEE, "달콤한 바닐라 시럽이 들어간 라떼"));
        menus.add(new Menu("카라멜마끼아또", 5500, Category.COFFEE, "카라멜 시럽과 우유, 에스프레소"));
        menus.add(new Menu("녹차라떼", 5500, Category.BEVERAGE, "고소한 녹차와 우유"));
        menus.add(new Menu("초코라떼", 5500, Category.BEVERAGE, "진한 초콜릿과 우유"));
        menus.add(new Menu("딸기스무디", 6000, Category.BEVERAGE, "신선한 딸기로 만든 스무디"));
        menus.add(new Menu("치즈케이크", 6000, Category.DESSERT, "부드러운 크림치즈 케이크"));
        menus.add(new Menu("티라미수", 6500, Category.DESSERT, "이탈리아 전통 디저트"));
    }

    // CREATE : 메뉴 추가
    public Menu save(Menu menu) {
        menus.add(menu);
        return menu;
    }

    // READ : 메뉴 전체 조회
    public List<Menu> findAll() {
        return new ArrayList<>(menus);
    }

    // READ : 메뉴 id로 상세 조회
    public Optional<Menu> findById(UUID uuid) {
        for (Menu menu : menus) {
            if (menu.getId().equals(uuid)) {
                return Optional.of(menu);
            }
        }
        return Optional.empty();
    }

    // UPDATE : 메뉴 수정
    public Menu update(Menu menu) {
        for (Menu m : menus) {
            if (m.getId().equals(menu.getId())) {
                m.setName(menu.getName());
                m.setPrice(menu.getPrice());
                m.setCategory(menu.getCategory());
                m.setDescription(menu.getDescription());
                break;
            }
        }
        return menu;
    }

    // DELETE : 메뉴 삭제
    public void deleteById(UUID uuid) {
        menus.removeIf(menu -> menu.getId().equals(uuid));
    }
}
