package com.cafe.order.domain.user.controller.api;

import com.cafe.order.domain.user.dto.UserSignupRequest;
import com.cafe.order.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserJoinApiController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody UserSignupRequest request) {
        userService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");
    }
}
