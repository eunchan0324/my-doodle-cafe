package com.cafe.order.domain.menu.ctrl;

import com.cafe.order.domain.menu.dto.Category;
import com.cafe.order.domain.menu.entity.Menu;
import com.cafe.order.domain.menu.service.MenuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/admin/menus")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    // CREATE : 메뉴 생성 폼
    @GetMapping("/new")
    public String createForm() {
        return "menu/new";
    }

    // CREATE : 메뉴 저장 처리
    @PostMapping("/new")
    public String create(
            @RequestParam String name,
            @RequestParam Integer price,
            @RequestParam Category category,
            @RequestParam(required = false) String description) {
        menuService.create(name, price, category, description);
        return "redirect:/admin/menus";
    }

    // READ : 메뉴 전체 조회
    @GetMapping
    public String showAllMenus(Model model) {
        model.addAttribute("menus", menuService.findAll());
        return "menu/list";
    }

    // READ : 메뉴 상세 조회
    @GetMapping("/{id}")
    public String detail(@PathVariable UUID id, Model model) {
        // Service 호출
        Menu menu = menuService.findById(id);

        // Model에 담기
        model.addAttribute("menu", menu);
        return "menu/detail";
    }

    // UPDATE : 메뉴 수정 폼
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable UUID id, Model model) {
        model.addAttribute("menu", menuService.findById(id));
        return "menu/edit";
    }

    // UPDATE : 메뉴 수정 로직
    @PostMapping("/edit/{id}")
    public String edit(
            @PathVariable UUID id,
            @RequestParam String name,
            @RequestParam Integer price,
            @RequestParam Category category,
            @RequestParam(required = false) String description) {
        menuService.update(id, name, price, category, description);
        return "redirect:/admin/menus";
    }


    // DELETE : 메뉴 id로 메뉴 삭제
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable UUID id) {
        menuService.delete(id);
        return "redirect:/admin/menus";
    }



}
