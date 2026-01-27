package com.cafe.order.domain.menu.controller.api;


import com.cafe.order.domain.menu.dto.AdminMenuCreateRequest;
import com.cafe.order.domain.menu.dto.AdminMenuResponse;
import com.cafe.order.domain.menu.entity.Menu;
import com.cafe.order.domain.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/menus")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminMenuApiController {

    private final MenuService menuService;

    /**
     * 메뉴 생성
     */
    @PostMapping()
    public ResponseEntity<AdminMenuResponse> createMenu(@RequestBody AdminMenuCreateRequest request) {
        Menu menu = menuService.create(request.getName(), request.getPrice(), request.getCategory(), request.getDescription());
        return ResponseEntity.status(HttpStatus.CREATED).body(new AdminMenuResponse(menu));
    }

    /**
     * 전체 메뉴 조회
     */
    @GetMapping
    public ResponseEntity<List<AdminMenuResponse>> getAllMenus() {
        List<Menu> allMenus = menuService.findAll();

        List<AdminMenuResponse> response = allMenus.stream()
                .map(AdminMenuResponse::new)
                .toList();

        return ResponseEntity.ok(response);
    }

    /**
     * 메뉴 상세 조회
     */
    @GetMapping("/{menuId}")
    public ResponseEntity<AdminMenuResponse> getMenuDetail(@PathVariable UUID menuId) {
        Menu menu = menuService.findById(menuId);
        return ResponseEntity.ok(new AdminMenuResponse(menu));
    }

    /**
     * 메뉴 수정
     */
    @PutMapping("/{menuId}")
    public ResponseEntity<AdminMenuResponse> updateMenu(@PathVariable UUID menuId, @RequestBody AdminMenuCreateRequest request) {
        Menu updatedMenu = menuService.update(menuId, request.getName(), request.getPrice(), request.getCategory(), request.getDescription());
        return ResponseEntity.ok(new AdminMenuResponse(updatedMenu));
    }

    /**
     * 메뉴 삭제
     */
    @DeleteMapping("/{menuId}")
    public ResponseEntity<?> deleteMenu(@PathVariable UUID menuId) {
        menuService.delete(menuId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
