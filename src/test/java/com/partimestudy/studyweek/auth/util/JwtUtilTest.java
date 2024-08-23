package com.partimestudy.studyweek.auth.util;

import com.partimestudy.studyweek.auth.exception.RefreshTokenExpireException;
import com.partimestudy.studyweek.auth.repository.TokenRepository;
import com.partimestudy.studyweek.auth.service.impl.AuthenticationServiceImpl;
import com.partimestudy.studyweek.member.entity.Member;
import com.partimestudy.studyweek.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtUtilTest {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationServiceImpl authenticationService;

    private final String loginId = "testUser1";
    private final String password = "testPassword";
    Member member;

    @BeforeEach
    void setUp() {
        member = Member.builder()
            .loginId(loginId)
            .password(passwordEncoder.encode(password))
            .name("Test User")
            .goal("Test Goal")
            .build();
        memberRepository.save(member);
    }
    @AfterEach
    void tearDown() {
        memberRepository.delete(member);
    }

    @Test
    void testGenerateAccessTokenAndRefreshToken() {
        String accessToken = jwtUtil.generateAccessToken(loginId);
        String refreshTokenToken = jwtUtil.generateRefreshToken(loginId);

        assertNotNull(accessToken);
        assertNotNull(refreshTokenToken);
    }

    @Test
    void testGenerateAndValidateAccessToken() {
        // Given
        jwtUtil.generateAccessToken(loginId);
        String accessToken = tokenRepository.findAccessToken(loginId);

        // When
        String newLoginId = jwtUtil.validateAccessToken(accessToken, loginId);

        // Then
        assertNotNull(accessToken);
        assertEquals(newLoginId, loginId);
    }

    @Test
    void testAccessTokenExpiredAndRefreshTokenIsNot() {
        // Given -> accessToken : 만료, refreshToken : 2082.2.18일까지
        String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0VXNlciIsImlhdCI6MTcyNDI0MjU1MCwiZXhwIjoxNzI0MjQyNTUwfQ.RjAZSholL12oRBEQKRontp8WSqaWw3xinRdF9d3YKi8";
        String refreshToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0VXNlciIsImlhdCI6MTcyNDI0MjY1NiwiZXhwIjozNTM4NjQyNjU2fQ.WnnoQ8fhkvpjT771TUySC3CJbNPFdTDLAV2rNellhR0";
        tokenRepository.saveAccessToken(accessToken, loginId);
        tokenRepository.saveRefreshToken(refreshToken, loginId);

        // When
        Assertions.assertDoesNotThrow(()->{
            jwtUtil.validateAccessToken(accessToken, refreshToken);
        });
    }

    @Test
    void testBothAccessTokenAndRefreshTokenIsExpired() {
        // Given -> 둘다 만료된 토큰
        String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0VXNlciIsImlhdCI6MTcyNDI0MjU1MCwiZXhwIjoxNzI0MjQyNTUwfQ.RjAZSholL12oRBEQKRontp8WSqaWw3xinRdF9d3YKi8";
        String refreshToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0VXNlciIsImlhdCI6MTcyNDI0MjU1MCwiZXhwIjoxNzI0MjQyNTUwfQ.gbIzHPAtETJeDGCjpshnK2A90EUHkoLFWxvOkfZr8no";

        tokenRepository.saveAccessToken(accessToken, loginId);
        tokenRepository.saveRefreshToken(refreshToken, loginId);

        // When & Then
        assertThrows(RefreshTokenExpireException.class, ()->{
            jwtUtil.validateAccessToken(accessToken, refreshToken);
        });
    }


}