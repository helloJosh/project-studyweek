package com.partimestudy.studywork.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

/**
 * 로그인 요청 레코드.
 *
 * @param loginId 로그인 아이디
 * @param password 비밀번호
 */
@Builder
public record PostLoginRequest(
        @NotBlank String loginId,
        @NotBlank String password) {
}
