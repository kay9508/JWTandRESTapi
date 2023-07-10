package com.assignment.sinyoung.controller;

import com.assignment.sinyoung.dto.VerifyRequestDto;
import com.assignment.sinyoung.dto.VerifyResponseDto;
import com.assignment.sinyoung.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/verify")
public class VerifyController {

    private final UserService userService;

    /**
     * 계정 중복 체크
     *
     * @param accountDto
     * @return
     */
    @GetMapping("/account")
    public ResponseEntity<VerifyResponseDto.Account> account(@Valid VerifyRequestDto.Account accountDto) {
        VerifyResponseDto.Account returnDto = new VerifyResponseDto.Account(userService.accountVerify(accountDto.getAccount()));
        return ResponseEntity.ok().body(returnDto);
    }

    /**
     * 사업자등록번호 체크
     * (사업자 번호가 중복으로 가입을 할 수도 있다고 생각해서 일단 중복확인은 적용하지 않음)
     * @param crnDto
     * @return
     */
    @GetMapping("/crn")
    public ResponseEntity<VerifyResponseDto.Crn> crn(@Valid VerifyRequestDto.Crn crnDto) {
        //VerifyResponseDto.Crn returnDto = new VerifyResponseDto.Crn(userService.crnVerify(crnDto));
        VerifyResponseDto.Crn returnDto = new VerifyResponseDto.Crn(true);
        return ResponseEntity.ok().body(returnDto);
    }

    /**
     * 사용자 닉네임 중복체크
     *
     * @param nicknameDto
     * @return
     */
    @GetMapping("/nickname")
    public ResponseEntity<VerifyResponseDto.Nickname> nickname(@Valid VerifyRequestDto.Nickname nicknameDto) {
        VerifyResponseDto.Nickname returnDto = new VerifyResponseDto.Nickname(userService.nicknameVerify(nicknameDto.getNickname()));
        return ResponseEntity.ok().body(returnDto);
    }
}
