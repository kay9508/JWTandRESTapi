package com.assignment.sinyoung.dto;

import com.assignment.sinyoung.enums.TodoState;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class TodoRequestDto {

    @Getter
    @Setter
    public static class ListTodo {

        @NotNull(message = "상태는 필수 입력값입니다.")
        private TodoState state;

        @NotNull(message = "size는 필수 입력값입니다.")
        private Integer size;

        private Long lastSeenId;

    }
    @Getter
    @Setter
    public static class SaveTodo {

        @NotEmpty(message = "이름은 필수 입력값입니다.")
        private String name;

        @NotEmpty(message = "내용은 필수 입력값입니다.")
        private String description;

    }

    @Getter
    @Setter
    public static class DeleteTodo {

        @NotNull(message = "id 값은 필수 입력값입니다.")
        private Long id;

    }

    @Getter
    @Setter
    public static class UpdateTodo {

        @NotNull(message = "id 값은 필수 입력값입니다.")
        private Long id;

        @NotEmpty(message = "이름은 필수 입력값입니다.")
        private String name;

        @NotEmpty(message = "내용은 필수 입력값입니다.")
        private String description;

        @NotNull(message = "완료여부는 필수 입력값입니다.")
        @Enumerated(EnumType.STRING)
        private TodoState state;
    }

    @Getter
    @Setter
    public static class LoadTodo {
        @NotNull(message = "id 값은 필수 입력값입니다.")
        private Long id;
    }
}
