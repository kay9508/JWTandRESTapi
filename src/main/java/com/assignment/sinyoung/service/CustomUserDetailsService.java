package com.assignment.sinyoung.service;

import com.assignment.sinyoung.entity.User;
import com.assignment.sinyoung.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails test = userRepository.findByAccountAndDeleteAt(username,false)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));

        return test;
    }

    // 해당하는 User 의 데이터가 존재한다면 UserDetails 객체로 만들어서 리턴
    private UserDetails createUserDetails(User user) {
        return org.springframework.security.core.userdetails.User.builder()
                //TODO 여기 해당되는 username은 고유한 값 이여하하기 때문에 현재 프로젝트구조상 User의 id값(Long)을 잡는게 맞다
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRoles().get(0).toString().split("ROLE_")[1])
                .build();
    }

}