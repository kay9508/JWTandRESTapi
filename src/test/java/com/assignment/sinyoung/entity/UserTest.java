package com.assignment.sinyoung.entity;

import com.assignment.sinyoung.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class UserTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void delete() {
        User user = new User(1L,
                "ddd",
                "1234",
                "닉네임",
                "010-7894-5553",
                "123-56-785231",
                LocalDateTime.now(),
                false,
                null,
                null,
                null);

        User savedUser = userRepository.save(user);

        savedUser.delete();

        assertAll(
                () -> assertTrue(savedUser.getPhone().equals("***-****-****"))
        );
    }
}