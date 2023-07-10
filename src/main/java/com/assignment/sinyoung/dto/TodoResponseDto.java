package com.assignment.sinyoung.dto;

import com.assignment.sinyoung.enums.TodoState;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

public class TodoResponseDto {
    @Getter
    @Setter
    public static class TodoList {
        List<TodoListInfo> items;
    }

    @Getter
    @Setter
    public static class TodoListInfo {
        private Long id;
        private String name;
        private TodoState state;
        private LocalDateTime createdAt;
    }

    @Getter
    @Setter
    public static class LoadTodo {
        private Long id;
        private String name;
        private String description;
        private TodoState state;
    }
}
