package com.partimestudy.studyweek.member.service.impl;

import com.partimestudy.studyweek.member.dto.PostMemberRequest;
import com.partimestudy.studyweek.member.entity.Member;
import com.partimestudy.studyweek.member.exception.DuplicatedLoginIdException;
import com.partimestudy.studyweek.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberServiceImpl memberService;

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
        assertEquals("password", savedMember.getPassword());
        assertEquals("name", savedMember.getName());
        assertEquals("goal", savedMember.getGoal());
    }
}