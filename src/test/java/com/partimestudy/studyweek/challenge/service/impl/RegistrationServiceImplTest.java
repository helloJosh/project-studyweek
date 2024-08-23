package com.partimestudy.studyweek.challenge.service.impl;

import com.partimestudy.studyweek.challenge.dto.ChallengeScheduleRequest;
import com.partimestudy.studyweek.challenge.dto.GetRegistrationResponse;
import com.partimestudy.studyweek.challenge.dto.PostRegistrationRequest;
import com.partimestudy.studyweek.challenge.entity.Challenge;
import com.partimestudy.studyweek.challenge.entity.ChallengeSchedule;
import com.partimestudy.studyweek.challenge.entity.Registration;
import com.partimestudy.studyweek.challenge.exception.*;
import com.partimestudy.studyweek.challenge.repository.ChallengeRepository;
import com.partimestudy.studyweek.challenge.repository.RegistrationRepository;
import com.partimestudy.studyweek.member.entity.Member;
import com.partimestudy.studyweek.member.exception.MemberNotFoundException;
import com.partimestudy.studyweek.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceImplTest {
    @Mock
    private RegistrationRepository registrationRepository;

    @Mock
    private ChallengeRepository challengeRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private RegistrationServiceImpl registrationService;
    private Member member;
    private Challenge challenge;

    @BeforeEach
    void setUp() {
        member = Member.builder().id(1L).loginId("testUser").registrations(new ArrayList<>()).build();
        challenge = Challenge.builder().id(1L).minDeposit(5000).maxDeposit(20000).status("ACTIVE").build();
    }

    @DisplayName("새로운 등록 성공")
    @Test
    void makeRegistration_Success() {
        PostRegistrationRequest request = PostRegistrationRequest.builder()
                .challengeId(1L)
                .challengeName("월요일 1시간 공부하기")
                .deposit(10000)
                .challengeSchedules(List.of(new ChallengeScheduleRequest(LocalDateTime.now(), 1)))
                .build();

        when(memberRepository.findMemberByLoginId("testUser")).thenReturn(Optional.of(member));
        when(challengeRepository.findById(1L)).thenReturn(Optional.of(challenge));
        when(registrationRepository.save(any(Registration.class))).thenReturn(null);

        registrationService.makeRegistration(request, "testUser");

        verify(memberRepository, times(1)).findMemberByLoginId("testUser");
        verify(challengeRepository, times(1)).findById(1L);
        verify(registrationRepository, times(1)).save(any(Registration.class));
    }
    @DisplayName("멤버가 존재하지 않을 때 예외 발생")
    @Test
    void makeRegistration_MemberNotFoundException() {
        PostRegistrationRequest request = PostRegistrationRequest.builder()
                .challengeId(1L)
                .challengeName("월요일 1시간 공부하기")
                .deposit(10000)
                .challengeSchedules(List.of(new ChallengeScheduleRequest(LocalDateTime.now(), 1)))
                .build();

        when(memberRepository.findMemberByLoginId("testUser")).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> {
            registrationService.makeRegistration(request, "testUser");
        });
    }

    @DisplayName("챌린지가 존재하지 않을 때 예외 발생")
    @Test
    void makeRegistration_ChallengeNotFoundException() {
        PostRegistrationRequest request = PostRegistrationRequest.builder()
                .challengeId(1L)
                .challengeName("월요일 1시간 공부하기")
                .deposit(10000)
                .challengeSchedules(List.of(new ChallengeScheduleRequest(LocalDateTime.now(), 1)))
                .build();

        when(memberRepository.findMemberByLoginId("testUser")).thenReturn(Optional.of(member));
        when(challengeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ChallengeNotFoundException.class, () -> {
            registrationService.makeRegistration(request, "testUser");
        });
    }

    @DisplayName("보증금이 최소 보증금보다 적을 때 예외 발생")
    @Test
    void makeRegistration_MinDepositUnderFlowException() {
        PostRegistrationRequest request = PostRegistrationRequest.builder()
                .challengeId(1L)
                .challengeName("월요일 1시간 공부하기")
                .deposit(4000)
                .challengeSchedules(List.of(new ChallengeScheduleRequest(LocalDateTime.now(), 1)))
                .build();

        when(memberRepository.findMemberByLoginId("testUser")).thenReturn(Optional.of(member));
        when(challengeRepository.findById(1L)).thenReturn(Optional.of(challenge));

        assertThrows(MinDepositUnderFlowException.class, () -> {
            registrationService.makeRegistration(request, "testUser");
        });
    }

    @DisplayName("보증금이 최대 보증금보다 많을 때 예외 발생")
    @Test
    void makeRegistration_MaxDepositOverFlowException() {
        PostRegistrationRequest request = PostRegistrationRequest.builder()
                .challengeId(1L)
                .challengeName("월요일 1시간 공부하기")
                .deposit(25000)
                .challengeSchedules(List.of(new ChallengeScheduleRequest(LocalDateTime.now(), 1)))
                .build();

        when(memberRepository.findMemberByLoginId("testUser")).thenReturn(Optional.of(member));
        when(challengeRepository.findById(1L)).thenReturn(Optional.of(challenge));

        assertThrows(MaxDepositOverFlowException.class, () -> {
            registrationService.makeRegistration(request, "testUser");
        });
    }

    @DisplayName("이미 신청된 챌린지일 때 예외 발생")
    @Test
    void makeRegistration_DuplicatedChallengeException() {
        Registration existingRegistration = Registration.builder().challenge(challenge).build();
        member.getRegistrations().add(existingRegistration);

        PostRegistrationRequest request = PostRegistrationRequest.builder()
                .challengeId(1L)
                .challengeName("월요일 1시간 공부하기")
                .deposit(10000)
                .challengeSchedules(List.of(new ChallengeScheduleRequest(LocalDateTime.now(), 1)))
                .build();

        when(memberRepository.findMemberByLoginId("testUser")).thenReturn(Optional.of(member));
        when(challengeRepository.findById(1L)).thenReturn(Optional.of(challenge));

        assertThrows(DuplicatedChallengeException.class, () -> {
            registrationService.makeRegistration(request, "testUser");
        });
    }

    @DisplayName("챌린지가 비활성화 상태일 때 예외 발생")
    @Test
    void makeRegistration_ChallengeNotActiveException() {
        challenge.setStatus("INACTIVE");

        PostRegistrationRequest request = PostRegistrationRequest.builder()
                .challengeId(1L)
                .challengeName("월요일 1시간 공부하기")
                .deposit(10000)
                .challengeSchedules(List.of(new ChallengeScheduleRequest(LocalDateTime.now(), 1)))
                .build();

        when(memberRepository.findMemberByLoginId("testUser")).thenReturn(Optional.of(member));
        when(challengeRepository.findById(1L)).thenReturn(Optional.of(challenge));

        assertThrows(ChallengeNotActiveException.class, () -> {
            registrationService.makeRegistration(request, "testUser");
        });
    }

    @DisplayName("등록 정보 조회 성공")
    @Test
    void findRegistration_Success() {
        Registration registration = Registration.builder()
                .id(1L)
                .challengeName("월요일 1시간 공부하기")
                .challengeDeposit(10000)
                .challengePaymentAmount(10000)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();

        ChallengeSchedule schedule = ChallengeSchedule.builder()
                .applyDate(LocalDateTime.now())
                .hours(1)
                .build();
        registration.addChallengeSchedules(List.of(schedule));

        when(registrationRepository.findById(1L)).thenReturn(Optional.of(registration));

        GetRegistrationResponse response = registrationService.findRegistration(1L);

        assertEquals("월요일 1시간 공부하기", response.challengeName());
        assertEquals(10000, response.challengeDeposit());
        assertEquals(10000, response.challengePaymentAmount());
        assertEquals("ACTIVE", response.status());
        assertEquals(1, response.challengeSchedule().size());
    }

    @DisplayName("등록 정보가 존재하지 않을 때 예외 발생")
    @Test
    void findRegistration_RegistrationNotFoundException() {
        when(registrationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RegistrationNotFoundException.class, () -> {
            registrationService.findRegistration(1L);
        });
    }




}