package com.partimestudy.studywork.challenge.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;


/**
 * 주문 응답 레코드
 *
 * @param challengeName 챌린지명
 * @param challengeDeposit 보증금
 * @param challengePaymentAmount 결제금액
 * @param status 주문상태
 * @param createdAt 주문일시
 * @param challengeSchedule 스케쥴리스트
 */
@Builder
public record GetRegistrationResponse(
        String challengeName,
        int challengeDeposit,
        int challengePaymentAmount,
        String status,
        LocalDateTime createdAt,
        List<ChallengeScheduleResponse> challengeSchedule) {
}
