package com.partimestudy.studyweek.challenge.service.impl;

import com.partimestudy.studyweek.challenge.dto.ChallengeScheduleRequest;
import com.partimestudy.studyweek.challenge.dto.PostRegistrationRequest;
import com.partimestudy.studyweek.challenge.dto.PostRegistrationResponse;
import com.partimestudy.studyweek.challenge.entity.Challenge;
import com.partimestudy.studyweek.challenge.entity.ChallengeSchedule;
import com.partimestudy.studyweek.challenge.entity.Registration;
import com.partimestudy.studyweek.challenge.exception.ChallangeNotFoundException;
import com.partimestudy.studyweek.challenge.repository.ChallengeRepository;
import com.partimestudy.studyweek.challenge.repository.RegistrationRepository;
import com.partimestudy.studyweek.challenge.service.RegistrationService;
import com.partimestudy.studyweek.member.entity.Member;
import com.partimestudy.studyweek.member.exception.MemberNotFoundException;
import com.partimestudy.studyweek.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {
    private final RegistrationRepository registrationRepository;
    private final ChallengeRepository challengeRepository;
    private final MemberRepository memberRepository;

    @Override
    public PostRegistrationResponse makeRegistration(PostRegistrationRequest postRegistrationRequest, Long memberId) {
        Member member = memberRepository
                .findById(memberId)
                .orElseThrow(()-> new MemberNotFoundException(memberId + "가 존재하지 않습니다."));

        Challenge challenge = challengeRepository.findById(postRegistrationRequest.challengeId())
                .orElseThrow(() -> new ChallangeNotFoundException(postRegistrationRequest.challengeId() + "가 존재하지 않습니다."));

        Registration registration = Registration.builder()
                .challengeName(postRegistrationRequest.challengeName())
                .challengeDeposit(postRegistrationRequest.deposit())
                .challengePaymentAmount(postRegistrationRequest.deposit())
                .status("")
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
