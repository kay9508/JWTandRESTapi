package com.assignment.sinyoung.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

public class UserRequestDto {

    @Getter
    @Setter
    public static class SignIn {
        @NotEmpty(message = "계정은 필수 입력값입니다.")
        private String account;

        @NotEmpty(message = "비밀번호는 필수 입력값입니다.")
        private String password;

        @NotEmpty(message = "닉네임은 필수 입력값입니다.")
        private String nickname;

        @NotEmpty(message = "핸드폰번호는 필수 입력값입니다.")
        @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "사업자등록번호 형식에 맞지 않습니다.")
        private String phone;

        @NotEmpty(message = "사업자등록번호는 필수 입력값입니다.")
        @Pattern(regexp = "^[0-9]{3}-[0-9]{2}-[0-9]{5}$", message = "사업자등록번호 형식에 맞지 않습니다.")
        private String crn;
    }
}
