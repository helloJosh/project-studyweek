package com.partimestudy.studyweek.member.controller;

import com.partimestudy.studyweek.api.Response;
import com.partimestudy.studyweek.auth.service.AuthenticationService;
import com.partimestudy.studyweek.member.dto.GetMemberResponse;
import com.partimestudy.studyweek.member.dto.PostLoginRequest;
import com.partimestudy.studyweek.member.dto.PostLoginResponse;
import com.partimestudy.studyweek.member.dto.PostMemberRequest;
import com.partimestudy.studyweek.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 회원컨트롤러.
 *
 * @author 김병우
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MemberController {
    private final MemberService memberService;
    private final AuthenticationService authenticationService;

    /**
     * 회원가입
     *
     * @param postMemberRequest 회원생성요청
     * @return Response
     */
    @PostMapping("/members")
    @ResponseStatus(HttpStatus.CREATED)
    public Response<Void> createMember(@RequestBody PostMemberRequest postMemberRequest){
        memberService.signIn(postMemberRequest);

        return Response.createSuccess();
    }

    /**
     * 회원 정보 조회
     *
     * @return Response
     */
    @GetMapping("/members")
    @ResponseStatus(HttpStatus.CREATED)
    public Response<GetMemberResponse> readMember(
            @RequestHeader("Access-Token") String accessToken,
            @RequestHeader("Refresh-Token") String refreshToken){
        String loginId = authenticationService.validateToken(accessToken, refreshToken);

        return Response.success();
    }

    /**
     * 로그인
     *
     * @param postLoginRequest 로그인 요청 데이터
     * @return JWT 토큰이 포함된 Response
     */
    @PostMapping("/members/login")
    public Response<PostLoginResponse> login(@RequestBody PostLoginRequest postLoginRequest, HttpServletResponse response){
        Map<String, String> tokenMap = authenticationService.login(postLoginRequest.loginId(), postLoginRequest.password());

        response.setHeader("Access-Token", tokenMap.get("AccessToken"));
        response.setHeader("Refresh-Token", tokenMap.get("RefreshToken"));

        PostLoginResponse postLoginResponse = PostLoginResponse.builder()
                .accessToken(tokenMap.get("AccessToken"))
                .refreshToken(tokenMap.get("RefreshToken"))
                .build();

        return Response.success(postLoginResponse);
    }

}
