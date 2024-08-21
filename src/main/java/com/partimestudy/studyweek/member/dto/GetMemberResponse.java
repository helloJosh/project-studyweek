package com.partimestudy.studyweek.member.dto;

import lombok.Builder;

/**
 * 회원가입 요청 레코드.
 *
 * @param id 사용자 고유번호
 * @param name 사용자명
 * @param loginId 로그인 아이디
 * @param password 비밀번호
 * @param goal 공부목표
 */
@Builder
public record GetMemberResponse(
        String id,
        String name,
        String loginId,
        String password,
        String goal) {
}
