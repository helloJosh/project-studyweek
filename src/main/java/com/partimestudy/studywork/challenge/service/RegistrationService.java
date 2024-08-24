package com.partimestudy.studywork.challenge.service;

import com.partimestudy.studywork.challenge.dto.GetRegistrationResponse;
import com.partimestudy.studywork.challenge.dto.PostRegistrationRequest;

/**
 * 주문 서비스 인터페이스.
 *
 * @author 김병우.
 */
public interface RegistrationService {

    /**
     * 주문 생성.
     *
     * @param postRegistrationRequest 주문 생성 요청 레코드
     * @param loginId 로그인아이디
     */
    void makeRegistration(PostRegistrationRequest postRegistrationRequest, String loginId);

    /**
     * 주문 조회.
     *
     * @param registrationId 주문 고유아이디
     * @return 주문 조회 응답 레코드
     */
    GetRegistrationResponse findRegistration(Long registrationId);
}
