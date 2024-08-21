package com.partimestudy.studyweek.init;

import com.partimestudy.studyweek.challenge.entity.Challenge;
import com.partimestudy.studyweek.challenge.repository.ChallengeRepository;
import com.partimestudy.studyweek.member.entity.Member;
import com.partimestudy.studyweek.member.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 인메모리 DB 데이터 초기화 클래스.
 *
 * @author 김병우
 */
@Component
@RequiredArgsConstructor
public class DataInitializer {
    private final MemberRepository memberRepository;
    private final ChallengeRepository challengeRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        Member member = Member.builder()
                .name("test")
                .loginId("testUser")
                .password(passwordEncoder.encode("1234"))
                .goal("Get all test A")
                .build();
        Challenge challenge1 = Challenge.builder()
                .name("원하는 일정으로 공부하기")
                .minDeposit(10000)
                .maxDeposit(100000)
                .status("ACTIVE")
                .build();
        Challenge challenge2 = Challenge.builder()
                .name("월요일 1시간 공부하기")
                .minDeposit(10000)
                .maxDeposit(50000)
                .status("ACTIVE")
                .build();
        Challenge challenge3 = Challenge.builder()
                .name("화요일 1시간 공부하기")
                .minDeposit(10000)
                .maxDeposit(50000)
                .status("ACTIVE")
                .build();
        Challenge challenge4 = Challenge.builder()
                .name("수요일 1시간 공부하기")
                .minDeposit(10000)
                .maxDeposit(50000)
                .status("ACTIVE")
                .build();
        Challenge challenge5 = Challenge.builder()
                .name("목요일 1시간 공부하기")
                .minDeposit(10000)
                .maxDeposit(50000)
                .status("ACTIVE")
                .build();
        Challenge challenge6 = Challenge.builder()
                .name("금요일 1시간 공부하기")
                .minDeposit(10000)
                .maxDeposit(50000)
                .status("ACTIVE")
                .build();
        Challenge challenge7 = Challenge.builder()
                .name("토요일 1시간 공부하기")
                .minDeposit(10000)
                .maxDeposit(50000)
                .status("ACTIVE")
                .build();
        Challenge challenge8 = Challenge.builder()
                .name("일요일 1시간 공부하기")
                .minDeposit(10000)
                .maxDeposit(50000)
                .status("ACTIVE")
                .build();

        memberRepository.save(member);
        challengeRepository.save(challenge1);
        challengeRepository.save(challenge2);
        challengeRepository.save(challenge3);
        challengeRepository.save(challenge4);
        challengeRepository.save(challenge5);
        challengeRepository.save(challenge6);
        challengeRepository.save(challenge7);
        challengeRepository.save(challenge8);
    }
}
