package com.partimestudy.studywork.challenge.dto;

import lombok.Builder;

import java.util.List;

/**
 * 주문 요청 레코드.
 *
 * @param challengeId 챌린지아이디
 * @param challengeName 챌린지명
 * @param deposit 보증금
 * @param challengeSchedules 챌린지스케쥴
 */
@Builder
public record PostRegistrationRequest(
        Long challengeId,
        String challengeName,
        Integer deposit,
        List<ChallengeScheduleRequest> challengeSchedules) {
}
