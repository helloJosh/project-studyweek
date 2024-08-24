package com.partimestudy.studywork.member.service.impl;

import com.partimestudy.studywork.member.dto.PostMemberRequest;
import com.partimestudy.studywork.member.entity.Member;
import com.partimestudy.studywork.member.exception.DuplicatedLoginIdException;
import com.partimestudy.studywork.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberServiceImpl memberService;
    @Spy
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    void signIn_shouldThrowException_whenLoginIdIsDuplicated() {
        // given
        PostMemberRequest postMemberRequest = new PostMemberRequest( "name", "testLoginId", "password", "goal");
        when(memberRepository.existsMemberByLoginId(postMemberRequest.loginId())).thenReturn(true);

        // when
        // then
        DuplicatedLoginIdException exception = assertThrows(DuplicatedLoginIdException.class, () -> {
            memberService.signIn(postMemberRequest);
        });

        assertEquals("testLoginId: 중복 에러", exception.getMessage());
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    void signIn_shouldSaveMember_whenLoginIdIsNotDuplicated() {
        // given
        PostMemberRequest postMemberRequest = new PostMemberRequest( "name", "testLoginId", "password","goal");
        when(memberRepository.existsMemberByLoginId(postMemberRequest.loginId())).thenReturn(false);

        // when
        memberService.signIn(postMemberRequest);

        // then
        ArgumentCaptor<Member> memberCaptor = ArgumentCaptor.forClass(Member.class);
        verify(memberRepository).save(memberCaptor.capture());

        Member savedMember = memberCaptor.getValue();
        assertEquals("testLoginId", savedMember.getLoginId());
        assertEquals("name", savedMember.getName());
        assertEquals("goal", savedMember.getGoal());
    }
}