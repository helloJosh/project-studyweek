package com.partimestudy.studywork.challenge.service.impl;

import com.partimestudy.studywork.challenge.dto.ChallengeScheduleRequest;
import com.partimestudy.studywork.challenge.dto.ChallengeScheduleResponse;
import com.partimestudy.studywork.challenge.dto.PostRegistrationRequest;
import com.partimestudy.studywork.challenge.dto.GetRegistrationResponse;
import com.partimestudy.studywork.challenge.entity.Challenge;
import com.partimestudy.studywork.challenge.entity.ChallengeSchedule;
import com.partimestudy.studywork.challenge.entity.Registration;
import com.partimestudy.studywork.challenge.exception.*;
import com.partimestudy.studywork.challenge.repository.ChallengeRepository;
import com.partimestudy.studywork.challenge.repository.RegistrationRepository;
import com.partimestudy.studywork.challenge.service.RegistrationService;
import com.partimestudy.studywork.member.entity.Member;
import com.partimestudy.studywork.member.exception.MemberNotFoundException;
import com.partimestudy.studywork.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * 주문 서비스 구현체.
 *
 * @author 김병우
 */
@Service
@Transactional
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {
    private final RegistrationRepository registrationRepository;
    private final ChallengeRepository challengeRepository;
    private final MemberRepository memberRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public void makeRegistration(PostRegistrationRequest postRegistrationRequest, String loginId) {
        Member member = memberRepository
                .findMemberByLoginId(loginId)
                .orElseThrow(()-> new MemberNotFoundException(loginId + "가 존재하지 않습니다."));

        Challenge challenge = challengeRepository.findById(postRegistrationRequest.challengeId())
                .orElseThrow(() -> new ChallengeNotFoundException(postRegistrationRequest.challengeId() + "가 존재하지 않습니다."));

        if (postRegistrationRequest.deposit() < challenge.getMinDeposit()) {
            throw new MinDepositUnderFlowException(
                    "해당 챌린지의 최소 보증금 : " + challenge.getMinDeposit() +
                            ", 현재 보증금 :"+ postRegistrationRequest.deposit()
            );
        }
        if (postRegistrationRequest.deposit() > challenge.getMaxDeposit()) {
            throw new MaxDepositOverFlowException(
                    "해당 챌린지의 최대 보증금 : " + challenge.getMaxDeposit() +
                            ", 현재 보증금 :"+ postRegistrationRequest.deposit()
            );
        }

        List<Registration> registrations = member.getRegistrations();

        for (Registration registration : registrations) {
            if (Objects.equals(registration.getChallenge().getId(), postRegistrationRequest.challengeId())) {
                throw new DuplicatedChallengeException(
                        "이미 해당 챌린지가 신청되었습니다. " + postRegistrationRequest.challengeName()
                );
            }
        }

        if (!challenge.getStatus().equals("ACTIVE")) {
            throw new ChallengeNotActiveException(
                    "해당 챌린지는 비활성화 상태입니다 :" + postRegistrationRequest.challengeName()
            );
        }



        Registration registration = Registration.builder()
                .challengeName(postRegistrationRequest.challengeName())
                .challengeDeposit(postRegistrationRequest.deposit())
                .challengePaymentAmount(postRegistrationRequest.deposit())
                .status("ACTIVE")
                .build();

        member.addRegistration(registration);
        challenge.addRegistration(registration);

        List<ChallengeSchedule> challengeSchedules = new ArrayList<>();

        for (ChallengeScheduleRequest challengeScheduleRequest : postRegistrationRequest.challengeSchedules()) {
            ChallengeSchedule challengeSchedule = ChallengeSchedule.builder()
                    .applyDate(challengeScheduleRequest.applyDate())
                    .hours(challengeScheduleRequest.hours())
                    .build();
            challengeSchedules.add(challengeSchedule);
        }

        registration.addChallengeSchedules(challengeSchedules);

        registrationRepository.save(registration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GetRegistrationResponse findRegistration(Long registrationId) {
        Registration registration = registrationRepository.findById(registrationId)
                .orElseThrow(()->new RegistrationNotFoundException(registrationId + "가 존재하지 않습니다."));
        List<ChallengeScheduleResponse> challengeScheduleResponses = new ArrayList<>();

        for (ChallengeSchedule challengeSchedule : registration.getSchedules()) {
            challengeScheduleResponses.add(
                    new ChallengeScheduleResponse(challengeSchedule.getApplyDate(),challengeSchedule.getHours())
            );
        }

        return GetRegistrationResponse.builder()
                .challengeName(registration.getChallengeName())
                .challengeDeposit(registration.getChallengeDeposit())
                .challengePaymentAmount(registration.getChallengePaymentAmount())
                .status(registration.getStatus())
                .createdAt(registration.getCreatedAt())
                .challengeSchedule(challengeScheduleResponses)
                .build();
    }
}
