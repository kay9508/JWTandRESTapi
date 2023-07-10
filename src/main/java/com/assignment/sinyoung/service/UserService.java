package com.assignment.sinyoung.service;

import com.assignment.sinyoung.dto.UserRequestDto;
import com.assignment.sinyoung.dto.UserResponseDto;
import com.assignment.sinyoung.entity.QUser;
import com.assignment.sinyoung.entity.User;
import com.assignment.sinyoung.enums.Authority;
import com.assignment.sinyoung.jwt.JwtTokenProvider;
import com.assignment.sinyoung.jwt.TokenInfo;
import com.assignment.sinyoung.repository.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JPAQueryFactory queryFactory;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.refresh-token-validity-in-seconds}")
    private Long refreshTokenValidityInMilliseconds;
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TYPE = "Bearer";

    @Transactional
    public TokenInfo login(String account, String password) {
        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(account, password);

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        //authenticationManagerBuilder가 호출되면서 CustomUserDetailService가 로드됨.
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        // 4. RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(), tokenInfo.getRefreshToken(), refreshTokenValidityInMilliseconds, TimeUnit.MILLISECONDS);

        return tokenInfo;
    }

    @Transactional
    public void logout(ServletRequest request) {
        // 1. Authentication을 가져옵니다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 2. Redis 에서 해당 User ID 로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제합니다.
        if (redisTemplate.opsForValue().get("RT:" + authentication.getName()) != null) {
            // Refresh Token 삭제
            redisTemplate.delete("RT:" + authentication.getName());
        }

        // 3. Request Header 에서 Access Token 토큰 추출
        String accessToken = "";
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String bearerToken = httpServletRequest.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_TYPE)) {
            accessToken = bearerToken.substring(7);
        }

        // 4. 해당 Access Token 유효시간 가지고 와서 BlackList 로 저장하기
        Long expiration = jwtTokenProvider.getExpiration(accessToken);
        redisTemplate.opsForValue()
                .set(accessToken, "logout", expiration, TimeUnit.MILLISECONDS);
    }

    @Transactional
    public TokenInfo token(String refreshToken) {
        // 1. Authentication을 가져옵니다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 2. Redis 에서 User ID 을 기반으로 저장된 Refresh Token 값을 가져옵니다.
        String loadRefreshToken = (String)redisTemplate.opsForValue().get("RT:" + authentication.getName());
        // 로그아웃되어 Redis 에 RefreshToken 이 존재하지 않는 경우 처리
        if(ObjectUtils.isEmpty(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");
        }
        if(!loadRefreshToken.equals(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Refresh Token 정보가 일치하지 않습니다.");
        }

        // 3. 새로운 토큰 생성
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        // 4. RefreshToken Redis 업데이트
        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(), tokenInfo.getRefreshToken(), refreshTokenValidityInMilliseconds, TimeUnit.MILLISECONDS);

        return tokenInfo;
    }

    @Transactional
    public boolean accountVerify(String account) {
        QUser qUser = QUser.user;

        User findUserWithAccount = queryFactory
                .select(qUser)
                .from(qUser)
                .where(qUser.account.eq(account)
                        .and(qUser.deleteAt.eq(false)))
                .fetchOne();

        if (findUserWithAccount != null) {
            return false;
        } else {
            return true;
        }
    }

    @Transactional
    public boolean crnVerify(String crn) {
        QUser qUser = QUser.user;

        User findUserWithCrn = queryFactory
                .select(qUser)
                .from(qUser)
                .where(qUser.crn.eq(crn))
                .fetchOne();

        if (findUserWithCrn != null) {
            return false;
        } else {
            return true;
        }
    }

    @Transactional
    public boolean nicknameVerify(String nickname) {
        QUser qUser = QUser.user;

        User findUserWithNickname = queryFactory
                .select(qUser)
                .from(qUser)
                .where(qUser.nickname.eq(nickname))
                .fetchOne();

        if (findUserWithNickname != null) {
            return false;
        } else {
            return true;
        }
    }

    @Transactional
    public void signIn(UserRequestDto.SignIn signInDto) {
        signInDto.setPassword(passwordEncoder.encode(signInDto.getPassword()));

        if (!this.accountVerify(signInDto.getAccount())) {
            //계정중복
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "동일한 계정이 존재합니다.");
        } else if (!this.nicknameVerify(signInDto.getNickname())) {
            //닉네임중복
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "동일한 닉네임이 존재합니다.");
        } else {
            //회원가입
            User user = modelMapper.map(signInDto, User.class);
            user.getRoles().add(Authority.ROLE_USER.name());

            userRepository.save(user);
        }
    }

    @Transactional
    public UserResponseDto.LoadUser loadUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String account = authentication.getName();

        QUser qUser = QUser.user;
        User load = queryFactory
                .selectFrom(qUser)
                .where(qUser.account.eq(account))
                .fetchOne();

        try {
            UserResponseDto.LoadUser rtnUserDto = modelMapper.map(load, UserResponseDto.LoadUser.class);
            return rtnUserDto;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    @Transactional
    public void deleteUser(ServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String account = authentication.getName();

        Optional<User> loadUser = userRepository.findByAccountAndDeleteAt(account, false);
        if (loadUser.isPresent()) {
            loadUser.get().delete();
            this.logout(request);
        }
    }
}