package com.partimestudy.studywork.challenge.dto;

import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 챌린지 스케쥴 레코드.
 *
 * @param applyDate 신청일
 * @param hours 공부시간
 */
@Builder
public record ChallengeScheduleResponse(
        LocalDateTime applyDate,
        int hours) {
}
