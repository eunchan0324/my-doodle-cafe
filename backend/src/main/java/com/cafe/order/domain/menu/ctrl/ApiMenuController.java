package com.cafe.order.domain.menu.ctrl;


import com.cafe.order.domain.menu.entity.Menu;
import com.cafe.order.domain.menu.service.MenuService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/menus")
public class ApiMenuController {

    private final MenuService menuService;

    public ApiMenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    // CREATE : 메뉴 생성
    @PostMapping("/new")
    public Menu create(@RequestBody Menu newMenu) {
        return menuService.create(newMenu.getName(), newMenu.getPrice(), newMenu.getCategory(), newMenu.getDescription());
    }

    // READ : 전체 조회
    @GetMapping
    public List<Menu> getAllMenus() {
        return menuService.findAll();
    }

    // READ : 상세 조회
    @GetMapping("/{id}")
    public Menu findById(@PathVariable UUID id) {
        return menuService.findById(id);
    }

    // UPDATE : 메뉴 수정
    @PutMapping("/{id}")
    public Menu update(@PathVariable UUID id, @RequestBody Menu m) {
        return menuService.update(id, m.getName(), m.getPrice(), m.getCategory(), m.getDescription());
    }

    // DELETE : 메뉴 삭제
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        menuService.delete(id);
    }
}
