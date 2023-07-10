package com.assignment.sinyoung.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TodoState {
    ALL("전체"),
    COMPLETE("완료"),
    INCOMPLETE("미완료");

    private String name;
}
