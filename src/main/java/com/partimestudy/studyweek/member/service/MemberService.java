package com.partimestudy.studyweek.member.service;

import com.partimestudy.studyweek.member.dto.PostMemberRequest;
import com.partimestudy.studyweek.member.exception.DuplicatedLoginIdException;

/**
 * 맴버 서비스 인터페이스.
 *
 * @author 김병우
 */
public interface MemberService {
    /**
     * 회원가입.
     *
     * @param postMemberRequest 맴버 생성 요청
     * @exception DuplicatedLoginIdException 로그인아이디 중복시 에러반환
     */
    void signIn(PostMemberRequest postMemberRequest) throws DuplicatedLoginIdException;
}
