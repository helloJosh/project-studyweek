package com.partimestudy.studywork.auth.service.impl;

import com.partimestudy.studywork.auth.exception.AuthenticationInvalidException;
import com.partimestudy.studywork.auth.util.JwtUtil;
import com.partimestudy.studywork.member.entity.Member;
import com.partimestudy.studywork.member.exception.LoginIdNotFoundException;
import com.partimestudy.studywork.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class AuthenticationServiceImplTest {

    @Mock
    private JwtUtil jwtUtil;

    @Spy
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Test
    void login_ShouldReturnTokens_WhenCredentialsAreValid() {
        // Given
        String loginId = "testLoginId";
        String password = "testPassword";
        Member member = Member.builder()
                .goal("t")
                .loginId("testLoginId")
                .name("t")
                .password(passwordEncoder.encode(password))
                .build();

        when(memberRepository.findMemberByLoginId(loginId)).thenReturn(Optional.of(member));
        when(jwtUtil.generateAccessToken(loginId)).thenReturn("accessToken");
        when(jwtUtil.generateRefreshToken(loginId)).thenReturn("refreshToken");

        // When
        Map<String, String> tokens = authenticationService.login(loginId, password);
        // Then
        assertNotNull(tokens);
        assertEquals("accessToken", tokens.get("AccessToken"));
        assertEquals("refreshToken", tokens.get("RefreshToken"));

        verify(memberRepository, times(1)).findMemberByLoginId(loginId);
        verify(jwtUtil, times(1)).generateAccessToken(loginId);
        verify(jwtUtil, times(1)).generateRefreshToken(loginId);
    }

    @Test
    void login_ShouldThrowLoginIdNotFoundException_WhenLoginIdDoesNotExist() {
        // Given
        String loginId = "nonExistentLoginId";

        when(memberRepository.findMemberByLoginId(loginId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(LoginIdNotFoundException.class, () -> authenticationService.login(loginId, "password"));

        verify(memberRepository, times(1)).findMemberByLoginId(loginId);
        verify(passwordEncoder, never()).encode(anyString());
        verify(jwtUtil, never()).generateAccessToken(anyString());
        verify(jwtUtil, never()).generateRefreshToken(anyString());
    }

    @Test
    void login_ShouldThrowAuthenticationInvalidException_WhenPasswordIsIncorrect() {
        // Given
        String loginId = "testLoginId";
        String password = "wrongPassword";
        Member member = Member.builder()
                .goal("t")
                .loginId("t")
                .name("t")
                .password("t")
                .build();

        when(memberRepository.findMemberByLoginId(loginId)).thenReturn(Optional.of(member));
        when(passwordEncoder.encode(password)).thenReturn("encodedWrongPassword");

        // When & Then
        assertThrows(AuthenticationInvalidException.class, () -> authenticationService.login(loginId, password));

        verify(memberRepository, times(1)).findMemberByLoginId(loginId);
        verify(passwordEncoder, times(1)).encode(password);
        verify(jwtUtil, never()).generateAccessToken(anyString());
        verify(jwtUtil, never()).generateRefreshToken(anyString());
    }

    @Test
    void validateToken_ShouldReturnAccessToken_WhenTokenIsValid() {
        // Given
        String loginId = "testLoginId";
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";

        when(jwtUtil.validateAccessToken(accessToken, refreshToken)).thenReturn(loginId);

        // When
        String result = authenticationService.validateToken(accessToken, refreshToken);

        // Then
        assertEquals(loginId, result);
        verify(jwtUtil, times(1)).validateAccessToken(accessToken, refreshToken);
    }
}