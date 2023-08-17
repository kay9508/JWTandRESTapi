package com.assignment.sinyoung.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class BoardRequestDto {
    @Getter
    @Setter
    public static class LoadBoard {
        @NotNull(message = "id 값은 필수 입력값입니다.")
        private Long id;
    }
}
