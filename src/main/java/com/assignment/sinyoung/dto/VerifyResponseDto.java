package com.assignment.sinyoung.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class VerifyResponseDto {
    @Getter
    @Setter
    @AllArgsConstructor
    public static class Account {
        private Boolean verify;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Crn {
        private Boolean verify;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Nickname {
        private Boolean verify;
    }
}
