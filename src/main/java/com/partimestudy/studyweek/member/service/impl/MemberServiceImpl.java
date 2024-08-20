package com.partimestudy.studyweek.member.service.impl;

import com.partimestudy.studyweek.member.dto.PostMemberRequest;
import com.partimestudy.studyweek.member.entity.Member;
import com.partimestudy.studyweek.member.exception.DuplicatedLoginIdException;
import com.partimestudy.studyweek.member.repository.MemberRepository;
import com.partimestudy.studyweek.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 맴버 서비스 구현체.
 *
 * @author 김병우
 */
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public void signIn(PostMemberRequest postMemberRequest) {
        if (memberRepository.existsMemberByLoginId(postMemberRequest.loginId())) {
            throw new DuplicatedLoginIdException(postMemberRequest.loginId() + ": 중복 에러");
        }

        Member member = Member.builder()
                .name(postMemberRequest.name())
                .loginId(postMemberRequest.loginId())
                .password(postMemberRequest.password())
                .goal(postMemberRequest.goal())
                .build();

        memberRepository.save(member);
    }
}
