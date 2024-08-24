package com.partimestudy.studywork.member.service.impl;

import com.partimestudy.studywork.member.dto.GetMemberResponse;
import com.partimestudy.studywork.member.dto.PostMemberRequest;
import com.partimestudy.studywork.member.entity.Member;
import com.partimestudy.studywork.member.exception.DuplicatedLoginIdException;
import com.partimestudy.studywork.member.exception.MemberNotFoundException;
import com.partimestudy.studywork.member.repository.MemberRepository;
import com.partimestudy.studywork.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 맴버 서비스 구현체.
 *
 * @author 김병우
 */
@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

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
                .password(passwordEncoder.encode(postMemberRequest.password()))
                .goal(postMemberRequest.goal())
                .build();

        memberRepository.save(member);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GetMemberResponse findMember(String loginId) {
        Member member = memberRepository.findMemberByLoginId(loginId)
                .orElseThrow(()-> new MemberNotFoundException(loginId + "에 해당하는 맴버가 없습니다."));

        return GetMemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .loginId(member.getLoginId())
                .password(member.getPassword())
                .goal(member.getGoal())
                .build();
    }
}
