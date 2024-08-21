package com.partimestudy.studyweek.init;

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
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        Member member = Member.builder()
                .name("test")
                .loginId("testUser")
                .password(passwordEncoder.encode("1234"))
                .goal("Get all test A")
                .build();

        memberRepository.save(member);
    }
}
