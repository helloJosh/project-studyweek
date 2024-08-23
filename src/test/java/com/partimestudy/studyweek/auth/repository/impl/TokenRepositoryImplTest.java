package com.partimestudy.studyweek.auth.repository.impl;

import com.partimestudy.studyweek.auth.repository.TokenRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class TokenRepositoryImplTest {
    private final TokenRepository tokenRepository = new TokenRepositoryImpl();

    @Test
    void testSaveAndFindAccessToken() {
        // Given
        String accessToken = "테스트액세스토큰";
        String loginId = "123";

        // When
        tokenRepository.saveAccessToken(accessToken, loginId);
        String foundAccessToken = tokenRepository.findAccessToken(loginId);

        // Then
        assertEquals(accessToken, foundAccessToken);
    }

    @Test
    void testSaveAndFindRefreshToken() {
        // Given
        String refreshToken = "테스트리프레쉬토큰";
        String loginId = "123";

        // When
        tokenRepository.saveRefreshToken(refreshToken, loginId);
        String foundRefreshToken = tokenRepository.findRefreshToken(loginId);

        // Then
        assertEquals(refreshToken, foundRefreshToken);
    }

    @Test
    void findAccessTokenWhenNotSet() {
        String loginId = "123";
        // When
        String foundAccessToken = tokenRepository.findAccessToken(loginId);

        // Then
        assertNull(foundAccessToken);
    }

    @Test
    void findRefreshTokenWhenNotSet() {
        String loginId = "123";
        // When
        String foundRefreshToken = tokenRepository.findRefreshToken(loginId);

        // Then
        assertNull(foundRefreshToken);
    }

}