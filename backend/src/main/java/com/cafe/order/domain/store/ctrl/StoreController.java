package com.cafe.order.domain.store.ctrl;

import com.cafe.order.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    // CREATE : 지점 생성 폼
    @GetMapping("/new")
    public String createForm() {
        return "store/new";
    }

    // CREATE : 지점 생성
    @PostMapping("/new")
    public String create(@RequestParam String name) {
        storeService.create(name);
        return "redirect:/admin/stores";
    }

    // READ : 전체 지점 조회
    @GetMapping
    public String list(Model model) {
        model.addAttribute("stores", storeService.findAll());
        return "store/list";
    }

    // UPDATE : 지점 수정 폼
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model) {
        model.addAttribute("store", storeService.findById(id));
        return "store/edit";
    }

    // UPDATE : 지점 수정
    @PostMapping("/{id}/update")
    public String update(@PathVariable Integer id, @RequestParam String name) {
        storeService.update(id, name);
        return "redirect:/admin/stores";
    }

    // DELETE : 지점 삭제
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id) {
        storeService.delete(id);
        return "redirect:/admin/stores";
    }
}
