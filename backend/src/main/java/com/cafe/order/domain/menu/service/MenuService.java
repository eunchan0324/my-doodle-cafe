package com.cafe.order.domain.menu.service;

import com.cafe.order.domain.menu.repo.JpaMenuRepository;
import com.cafe.order.domain.menu.dto.Category;
import com.cafe.order.domain.menu.entity.Menu;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MenuService {

    // JPA
    private final JpaMenuRepository menuRepository;

    // SQL
//  private final SqlMenuRepository menuRepository;

    // InMemory
//  private final InMemoryMenuRepository menuRepository;


    public MenuService(JpaMenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    // CREATE : 메뉴 생성
    public Menu create(String name, Integer price, Category category, String description) {
        Menu menu;

        if (description == null || description.isEmpty()) {
            menu = new Menu(name, price, category);
        } else {
            menu = new Menu(name, price, category, description);
        }

        return menuRepository.save(menu);
    }

    // READ : 메뉴 전체 조회
    public List<Menu> findAll() {
        return menuRepository.findAll();
    }

    // READ : 메뉴 id로 상세 조회
    public Menu findById(UUID uuid) {
//    Optional<Menu> optional = menuRepository.findById(uuid);
//    return optional.orElse(null); // 없으면 null 반환

        return menuRepository.findById(uuid).orElse(null);
    }

    // UPDATE : 메뉴 수정
    public Menu update(UUID id, String name, Integer price, Category category, String description) {
        Menu menu = menuRepository.findById(id).orElse(null);

        if (menu == null) {
            throw new IllegalArgumentException("메뉴를 찾을 수 없습니다.");
        }

        menu.setName(name);
        menu.setPrice(price);
        menu.setCategory(category);
        menu.setDescription(description);

        return menuRepository.save(menu); // JPA
//        return menuRepository.update(menu); // SQL, InMemory
    }

    // DELETE : 메뉴 삭제
    public void delete(UUID uuid) {
        menuRepository.deleteById(uuid);
    }


}
