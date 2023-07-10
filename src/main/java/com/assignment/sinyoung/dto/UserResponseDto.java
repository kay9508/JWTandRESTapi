package com.assignment.sinyoung.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

public class UserResponseDto {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoadUser {
        private String nickname;
        private String phone;
        private String crn;
        private LocalDateTime createdAt;
    }
}
