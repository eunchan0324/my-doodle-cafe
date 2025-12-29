package com.cafe.order.domain.user.ctrl;

import com.cafe.order.domain.store.entity.Store;
import com.cafe.order.domain.store.service.StoreService;
import com.cafe.order.domain.user.entity.User;
import com.cafe.order.domain.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {

    private final UserService userService;
    private final StoreService storeService;

    public UserController(UserService userService, StoreService storeService) {
        this.userService = userService;
        this.storeService = storeService;
    }

    // CREATE : 판매자 계정 생성 폼
    @GetMapping("/admin/sellers/new")
    public String createForm(Model model) {
        // 이미 배정된 지점 ID 목록
        List<Integer> assignedStoreIds = userService.getAssignedStoreIds();

        // 배정 가능한 지점만 전달
        List<Store> availableStores = storeService.findAvailableStores(assignedStoreIds);

        // 지점 목록을 model에 담아서 전달
        model.addAttribute("stores", availableStores);
        return "seller/new";
    }

    // CREATE : 판매자 계정 생성
    @PostMapping("/admin/sellers/new")
    public String create(@RequestParam String loginId,
                         @RequestParam String password,
                         @RequestParam String name,
                         @RequestParam Integer storeId) {
        userService.create(loginId, password, name, storeId);
        return "redirect:/admin/sellers";
    }


    // READ : 전체 판매자 계정 조회
    @GetMapping("/admin/sellers")
    public String sellerList(Model model) {
        model.addAttribute("sellers", userService.findAllSellerWithStoreName());
        return "seller/list";
    }

    // UPDATE : 판매자 계정 수정 폼
    @GetMapping("/admin/sellers/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model) {
        User seller = userService.findById(id);
        model.addAttribute("seller", seller);

        // 현재 판매자를 제외한 배정된 지점 ID 목록
        List<Integer> assignedStoreIds = userService.getAssignedStoreIdsExcept(id);

        // 배정 가능한 지점만 전달
        List<Store> availableStores = storeService.findAvailableStores(assignedStoreIds);

        model.addAttribute("stores", availableStores);

        return "seller/edit";
    }

    // UPDATE : 판매자 계정 수정
    @PostMapping("/admin/sellers/{id}/update")
    public String update(@PathVariable Integer id,
                         @RequestParam(required = false) String password,
                         @RequestParam String name,
                         @RequestParam Integer storeId) {
        userService.update(id, password, name, storeId);
        return "redirect:/admin/sellers";
    }

    // DELETE : 판매자 계정 삭제
    @PostMapping("/admin/sellers/{id}/delete")
    public String delete(@PathVariable Integer id) {
        userService.delete(id);
        return "redirect:/admin/sellers";
    }

}
