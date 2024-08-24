package com.partimestudy.studywork.challenge.controller;

import com.partimestudy.studywork.api.Response;
import com.partimestudy.studywork.auth.service.AuthenticationService;
import com.partimestudy.studywork.challenge.dto.PostRegistrationRequest;
import com.partimestudy.studywork.challenge.dto.GetRegistrationResponse;
import com.partimestudy.studywork.challenge.exception.PostRegistrationRequestFormErrorException;
import com.partimestudy.studywork.challenge.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * 챌린지 컨트롤러.
 *
 * @author 김병우
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ChallengeController {
    private final RegistrationService registrationService;
    private final AuthenticationService authenticationService;


    /**
     * 챌린지 추가 요청 핸들러.
     *
     * @param postRegistrationRequest 주문 추가 요청 레코드
     * @param bindingResult 바인딩리졸트
     * @param accessToken 액세스토큰
     * @param refreshToken 리프레쉬토큰
     * @return
     */
    @PostMapping("/challenges/registrations")
    public Response<Void> register(
            @RequestBody @Valid PostRegistrationRequest postRegistrationRequest,
            BindingResult bindingResult,
            @RequestHeader("Access-Token") String accessToken,
            @RequestHeader("Refresh-Token") String refreshToken) {
        if (bindingResult.hasErrors()) {
            throw new PostRegistrationRequestFormErrorException(bindingResult.getFieldError().toString());
        }

        String loginId = authenticationService.validateToken(accessToken, refreshToken);

        registrationService.makeRegistration(postRegistrationRequest, loginId);

        return Response.createSuccess();
    }

    /**
     * 챌린지 주문 조회 핸들러.
     *
     * @param registrationsId 주문 고유아이디
     * @param accessToken 액세스토큰
     * @param refreshToken 리프레쉬토큰
     * @return 조회 응답 레코드
     */
    @GetMapping("/challenges/registrations/{registrationsId}")
    public Response<GetRegistrationResponse> getRegistration(
            @PathVariable Long registrationsId,
            @RequestHeader("Access-Token") String accessToken,
            @RequestHeader("Refresh-Token") String refreshToken){
        authenticationService.validateToken(accessToken, refreshToken);

        return Response.success(registrationService.findRegistration(registrationsId));
    }
}
