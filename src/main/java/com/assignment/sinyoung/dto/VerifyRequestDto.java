package com.assignment.sinyoung.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

public class VerifyRequestDto {
    @Getter
    @Setter
    public static class Account {
        @NotEmpty(message = "계정은 필수 입력값입니다.")
        private String account;
    }

    @Getter
    @Setter
    public static class Crn {
        @NotEmpty(message = "사업자등록번호는 필수 입력값입니다.")
        @Pattern(regexp = "^[0-9]{3}-[0-9]{2}-[0-9]{5}$", message = "사업자등록번호 형식에 맞지 않습니다.")
        private String crn;
    }

    @Getter
    @Setter
    public static class Nickname {
        @NotEmpty(message = "닉네임은 필수 입력값입니다.")
        private String nickname;
    }
}
