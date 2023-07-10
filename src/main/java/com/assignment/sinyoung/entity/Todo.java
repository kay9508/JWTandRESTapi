package com.assignment.sinyoung.entity;

import com.assignment.sinyoung.dto.TodoRequestDto;
import com.assignment.sinyoung.enums.TodoState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "todo")
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_pid")
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    @Enumerated(EnumType.STRING)
    private TodoState state;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "modify_at")
    private LocalDateTime modifyAt;

    @ManyToOne
    @JoinColumn(name = "user_pid")
    private User createdUser;

    public void setCreatedUser(User user) {
        this.createdUser = user;
    }
    @PrePersist
    public void prePersist() {
        this.state = this.state == null ? TodoState.INCOMPLETE : this.state;
    }
    public void updateTodo(TodoRequestDto.UpdateTodo updateTodoDto) {
        this.name = updateTodoDto.getName();
        this.description = updateTodoDto.getDescription();
        this.state = updateTodoDto.getState();
    }

}
