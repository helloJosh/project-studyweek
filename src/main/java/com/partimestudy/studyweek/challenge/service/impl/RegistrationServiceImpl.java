package com.partimestudy.studyweek.challenge.service.impl;

import com.partimestudy.studyweek.challenge.dto.ChallengeScheduleRequest;
import com.partimestudy.studyweek.challenge.dto.PostRegistrationRequest;
import com.partimestudy.studyweek.challenge.dto.PostRegistrationResponse;
import com.partimestudy.studyweek.challenge.entity.Challenge;
import com.partimestudy.studyweek.challenge.entity.ChallengeSchedule;
import com.partimestudy.studyweek.challenge.entity.Registration;
import com.partimestudy.studyweek.challenge.exception.*;
import com.partimestudy.studyweek.challenge.repository.ChallengeRepository;
import com.partimestudy.studyweek.challenge.repository.RegistrationRepository;
import com.partimestudy.studyweek.challenge.service.RegistrationService;
import com.partimestudy.studyweek.member.entity.Member;
import com.partimestudy.studyweek.member.exception.MemberNotFoundException;
import com.partimestudy.studyweek.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {
    private final RegistrationRepository registrationRepository;
    private final ChallengeRepository challengeRepository;
    private final MemberRepository memberRepository;

    @Override
    public PostRegistrationResponse makeRegistration(PostRegistrationRequest postRegistrationRequest, String loginId) {
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
                // 같은 챌리지 예외처리
                throw new DuplicatedChallengeException(
                        "이미 해당 챌린지가 신청되었습니다. " + postRegistrationRequest.challengeName()
                );
            }
        }

        if (!challenge.getStatus().equals("ACTIVE")) {
            // 활성화가 되지않은 상태일떄 예외처리
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

        for (ChallengeScheduleRequest challengeScheduleRequest : postRegistrationRequest.challengeSchedules()) {

            ChallengeSchedule challengeSchedule = ChallengeSchedule.builder()
                    .applyDate(challengeScheduleRequest.applyDate())
                    .hours(challengeScheduleRequest.hours())
                    .build();

            registration.addChallengeSchedule(challengeSchedule);
        }

        registrationRepository.save(registration);

        return PostRegistrationResponse.builder()
                .challengeName(registration.getChallengeName())
                .challengeDeposit(registration.getChallengeDeposit())
                .challengePaymentAmount(registration.getChallengePaymentAmount())
                .status(registration.getStatus())
                .createdAt(registration.getCreatedAt())
                .challengeSchedule(postRegistrationRequest.challengeSchedules())
                .build();
    }
}
