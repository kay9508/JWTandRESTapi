package com.assignment.sinyoung.repository;

import com.assignment.sinyoung.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByAccount(String username);
    Optional<User> findByAccountAndDeleteAt(String username, Boolean deleteAt);

}