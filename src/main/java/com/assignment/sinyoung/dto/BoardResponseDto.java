package com.assignment.sinyoung.dto;

import com.assignment.sinyoung.entity.User;
import lombok.Getter;
import lombok.Setter;

public class BoardResponseDto {

    @Getter
    @Setter
    public static class LoadBoard {
        private Long id;
        private User user;
        private String title;
        private String content;
    }

    @Getter
    @Setter
    public static class ListBoard {
        private Long id;
        private User user;
        private String title;
    }
}
