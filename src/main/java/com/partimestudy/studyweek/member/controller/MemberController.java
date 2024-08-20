package com.partimestudy.studyweek.member.controller;

import com.partimestudy.studyweek.api.Response;
import com.partimestudy.studyweek.member.dto.PostMemberRequest;
import com.partimestudy.studyweek.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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


}
