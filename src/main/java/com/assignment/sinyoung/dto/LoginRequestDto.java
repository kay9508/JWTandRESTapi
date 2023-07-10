package com.assignment.sinyoung.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data // @Getter , @Setter , @RequiredArgsConstructor , @ToString , @EqualsAndHashCode 을 한꺼번에 설정
public class LoginRequestDto {
    @NotEmpty(message = "계정은 필수 입력값입니다.")
    private String account;
    @NotEmpty(message = "비밀번호는 필수 입력값입니다.")
    private String password;
}