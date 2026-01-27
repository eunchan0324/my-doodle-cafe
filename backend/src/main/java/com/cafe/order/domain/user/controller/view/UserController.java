package com.cafe.order.domain.user.controller.view;

import com.cafe.order.domain.store.entity.Store;
import com.cafe.order.domain.store.service.StoreService;
import com.cafe.order.domain.user.dto.UserSignupRequest;
import com.cafe.order.domain.user.entity.User;
import com.cafe.order.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final StoreService storeService;

    // ==========================================
    // [Admin] 관리자 및 판매자 관리 기능
    // ==========================================


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


    // ==========================================
    // [Public] 일반 고객 기능 (회원가입, 마이페이지 등)
    // ==========================================

    /**
     * 회원가입 폼 화면 (GET)
     */
    @GetMapping("/users/signup")
    public String signupForm(Model model) {
        model.addAttribute("userSignupRequest", new UserSignupRequest());
        return "customer/signup";
    }

    /**
     * 회원가입 처리 (POST)
     */
    @PostMapping("/users/signup")
    public String signup(@Valid @ModelAttribute UserSignupRequest request,
                         BindingResult bindingResult) {
        // 검증 에러 확인
        if (bindingResult.hasErrors()) {
            // 에러가 있다면 회원가입 폼으로 다시 돌려보냄 (입력한 정보 + 에러 정보가 같이)
            return "customer/signup";
        }

        // 서비스의 signup 메서드 호출 (DTO 전달)
        userService.signup(request);

        // 가입 성공 시 로그인 페이지로 리다이렉트
        return "redirect:/login";
    }
}
