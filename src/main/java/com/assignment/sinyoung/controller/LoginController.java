package com.assignment.sinyoung.controller;

import com.assignment.sinyoung.dto.LoginRequestDto;
import com.assignment.sinyoung.dto.LoginResponseDto;
import com.assignment.sinyoung.jwt.TokenInfo;
import com.assignment.sinyoung.service.UserService;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class LoginController {
    private final UserService userService;

    private static final String AUTHORIZATION_HEADER = "Authorization";

    /**
     * 로그인
     *
     * @param loginRequestDto
     * @param response
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto,
                                                  HttpServletResponse response) {
        String account = loginRequestDto.getAccount();
        String password = loginRequestDto.getPassword();
        TokenInfo tokenInfo = userService.login(account, password);

        Cookie refreshToken = new Cookie("RefreshToken", String.valueOf(tokenInfo.getRefreshToken()));
        response.addCookie(refreshToken);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORIZATION_HEADER, tokenInfo.getGrantType() + tokenInfo.getAccessToken());

        //return new ResponseEntity<>(new TokenDto(tokenInfo.getAccessToken()), httpHeaders, HttpStatus.OK);
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(new LoginResponseDto(tokenInfo.getAccessToken()));
    }

    /**
     * 로그아웃
     *
     * @param response
     * @param request
     */
    @PostMapping("/logout")
    public void logout(HttpServletResponse response, ServletRequest request) {
        userService.logout(request);

        Cookie refreshToken = new Cookie("RefreshToken", null); // RefreshToken(쿠키 이름)에 대한 값을 null로 지정
        refreshToken.setMaxAge(0); // 유효시간을 0으로 설정
        response.addCookie(refreshToken); // 응답 헤더에 추가해서 없어지도록 함
    }

    /**
     * Access Token 재발급
     *
     * @param refreshToken
     * @param response
     * @return
     */
    @GetMapping("/token")
    public ResponseEntity<LoginResponseDto> token(@CookieValue("RefreshToken") String refreshToken,
                                                  HttpServletResponse response) {

        TokenInfo tokenInfo = userService.token(refreshToken);

        Cookie newRefreshToken = new Cookie("RefreshToken", String.valueOf(tokenInfo.getRefreshToken()));
        response.addCookie(newRefreshToken);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORIZATION_HEADER, tokenInfo.getGrantType() + tokenInfo.getAccessToken());

        //return new ResponseEntity<>(new TokenDto(tokenInfo.getAccessToken()), httpHeaders, HttpStatus.OK);
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(new LoginResponseDto(tokenInfo.getAccessToken()));
    }

}