package com.assignment.sinyoung.controller;

import com.assignment.sinyoung.dto.UserRequestDto;
import com.assignment.sinyoung.dto.UserResponseDto;
import com.assignment.sinyoung.service.UserService;
import jakarta.servlet.ServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * 회원가입
     *
     * @param signInDto
     */
    @PostMapping("/user")
    public void signIn(@Valid @RequestBody UserRequestDto.SignIn signInDto) {
        userService.signIn(signInDto);
    }

    /**
     * 사용자정보 확인
     *
     * @return
     */
    @GetMapping("/user")
    public ResponseEntity<UserResponseDto.LoadUser> loadUser() {
        return ResponseEntity.ok()
                .body(userService.loadUser());
    }

    /**
     * 회원탈퇴
     *
     * @param request
     */
    @DeleteMapping("/user")
    public void deleteUser(ServletRequest request) {
        userService.deleteUser(request);
    }

}
