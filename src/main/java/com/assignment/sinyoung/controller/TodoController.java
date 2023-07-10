package com.assignment.sinyoung.controller;

import com.assignment.sinyoung.dto.TodoRequestDto;
import com.assignment.sinyoung.dto.TodoResponseDto;
import com.assignment.sinyoung.service.TodoService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    /**
     * To do 목록
     *
     * @param listTodoDto
     * @param pageable
     * @return
     */
    @GetMapping("/todo")
    public ResponseEntity<TodoResponseDto.TodoList> listTodo(@Valid TodoRequestDto.ListTodo listTodoDto,
                                                             @PageableDefault Pageable pageable) {
        TodoResponseDto.TodoList rtnDto = todoService.list(listTodoDto, pageable);
        return ResponseEntity
                .ok()
                .body(rtnDto);
    }

    /**
     * To do 추가
     *
     * @param saveTodoDto
     */
    @PostMapping("/todo")
    public void saveTodo(@Valid @RequestBody TodoRequestDto.SaveTodo saveTodoDto) {
        todoService.saveTodo(saveTodoDto);
    }

    /**
     * To do 삭제
     *
     * @param deleteTodoDto
     */
    @DeleteMapping("/todo")
    public void deleteTodo(@Valid TodoRequestDto.DeleteTodo deleteTodoDto) {
        todoService.deleteTodo(deleteTodoDto);
    }

    /**
     * To do 수정
     *
     * @param updateTodoDto
     */
    @PutMapping("/todo")
    public void updateTodo(@Valid @RequestBody TodoRequestDto.UpdateTodo updateTodoDto) {
        todoService.updateTodo(updateTodoDto);
    }

    /**
     * To do에 대한 상세 정보
     *
     * @param loadTodoDto
     * @return
     */
    @GetMapping("/todo/{id}")
    public ResponseEntity<TodoResponseDto.LoadTodo> loadTodo(@Valid TodoRequestDto.LoadTodo loadTodoDto) {
        TodoResponseDto.LoadTodo rtnDto = todoService.loadTodo(loadTodoDto);
        return ResponseEntity
                .ok()
                .body(rtnDto);
    }
}
