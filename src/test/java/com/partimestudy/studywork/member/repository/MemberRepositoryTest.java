package com.partimestudy.studywork.member.repository;

import com.partimestudy.studywork.member.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void existsMemberByLoginId_shouldReturnTrue_whenMemberExists() {
        // Given
        Member member = Member.builder()
                .name("테스트 이름")
                .loginId("test")
                .password("테스트 패스워드")
                .goal("테스트 공부목표")
                .build();

        memberRepository.saveAndFlush(member);

        // When
        boolean exists = memberRepository.existsMemberByLoginId("test");

        // Then
        assertTrue(exists);
    }

    @Test
    void existsMemberByLoginId_shouldReturnFalse_whenMemberDoesNotExist() {
        // When
        boolean exists = memberRepository.existsMemberByLoginId("nonExistingLoginId");

        // Then
        assertFalse(exists);
    }

}