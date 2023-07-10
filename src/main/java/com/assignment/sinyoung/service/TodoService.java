package com.assignment.sinyoung.service;

import com.assignment.sinyoung.dto.TodoRequestDto;
import com.assignment.sinyoung.dto.TodoResponseDto;
import com.assignment.sinyoung.entity.QTodo;
import com.assignment.sinyoung.entity.Todo;
import com.assignment.sinyoung.entity.User;
import com.assignment.sinyoung.enums.TodoState;
import com.assignment.sinyoung.repository.TodoRepository;
import com.assignment.sinyoung.repository.UserRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JPAQueryFactory queryFactory;
    @Transactional
    public TodoResponseDto.TodoList list(TodoRequestDto.ListTodo listTodoDto, Pageable pageable) {
        QTodo qTodo = QTodo.todo;

        BooleanBuilder builder = new BooleanBuilder();
        if (listTodoDto.getLastSeenId() != null) {
            builder.and(qTodo.id.lt(listTodoDto.getLastSeenId()));
        }
        if (!listTodoDto.getState().equals(TodoState.ALL)) {
            builder.and(qTodo.state.eq(listTodoDto.getState()));
        }

        List<Todo> list = queryFactory.selectFrom(qTodo)
                .where(builder)
                .orderBy(qTodo.id.desc())
                .limit(pageable.getPageSize())
                .fetch();
        List<TodoResponseDto.TodoListInfo> itemsDto = modelMapper.map(list, new TypeToken<List<TodoResponseDto.TodoListInfo>>(){}.getType());
        TodoResponseDto.TodoList rtnList = new TodoResponseDto.TodoList();
        rtnList.setItems(itemsDto);

        return rtnList;
    }
    @Transactional
    public void saveTodo(TodoRequestDto.SaveTodo saveTodoDto) {
        //TODO 이부분 커스텀 어노테이션 사용해서 User정보를 받아오도록 변경 필요
        User currentUser = userRepository.findByAccountAndDeleteAt(SecurityContextHolder.getContext().getAuthentication().getName(),false).get();
        Todo savedTodo = modelMapper.map(saveTodoDto, Todo.class);
        savedTodo.setCreatedUser(currentUser);
        todoRepository.save(savedTodo);
    }

    @Transactional
    public void deleteTodo(TodoRequestDto.DeleteTodo deleteTodoDto) {
        User currentUser = userRepository.findByAccountAndDeleteAt(SecurityContextHolder.getContext().getAuthentication().getName(),false).get();
        Optional<Todo> loadTodo = todoRepository.findById(deleteTodoDto.getId());
        if (loadTodo.isPresent()) {
            if (this.permissionCheck(loadTodo.get(), currentUser)) {
                todoRepository.deleteById(loadTodo.get().getId());
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 Todo를 삭제할 권한이 없습니다.");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 Todo 입니다.");
        }
    }

    @Transactional
    public void updateTodo(TodoRequestDto.UpdateTodo updateTodoDto) {
        Optional<Todo> loadTodo = todoRepository.findById(updateTodoDto.getId());
        if (loadTodo.isPresent()) {
            loadTodo.get().updateTodo(updateTodoDto);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 Todo 입니다.");
        }
    }

    @Transactional
    public TodoResponseDto.LoadTodo loadTodo(TodoRequestDto.LoadTodo loadTodoDto) {
        Optional<Todo> loadTodo = todoRepository.findById(loadTodoDto.getId());
        if (loadTodo.isPresent()) {
            TodoResponseDto.LoadTodo rtnDto = modelMapper.map(loadTodo, TodoResponseDto.LoadTodo.class);
            return rtnDto;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 Todo 입니다.");
        }
    }

    private boolean permissionCheck(Todo target, User user) {
        if (target.getCreatedUser().equals(user)) {
            return true;
        } else {
            return false;
        }
    }
}
