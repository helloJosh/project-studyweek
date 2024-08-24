package com.partimestudy.studywork.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.partimestudy.studywork.BaseDocumentTest;
import com.partimestudy.studywork.auth.service.AuthenticationService;
import com.partimestudy.studywork.member.dto.GetMemberResponse;
import com.partimestudy.studywork.member.dto.PostMemberRequest;
import com.partimestudy.studywork.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
class MemberControllerTest extends BaseDocumentTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private AuthenticationService authenticationService;

    @DisplayName("새로운 회원 생성")
    @Test
    void createMember() throws Exception {
        PostMemberRequest request = PostMemberRequest.builder()
                .name("testn")
                .loginId("testl")
                .password("testp")
                .goal("testg")
                .build();

        doNothing().when(memberService).signIn(request);


        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"testn\", \"loginId\": \"testl\", \"password\": \"testp\", \"goal\": \"testg\"}"))
                .andExpect(status().isCreated())
                .andDo(document(snippetPath,
                        "회원의 정보를 받아 등록하는 API",
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("사용자명"),
                                fieldWithPath("loginId").type(JsonFieldType.STRING).description("로그인아이디"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                fieldWithPath("goal").type(JsonFieldType.STRING).description("공부목표")
                        ),
                        responseFields(
                                fieldWithPath("header.resultCode").type(JsonFieldType.NUMBER).description("결과 코드"),
                                fieldWithPath("header.successful").type(JsonFieldType.BOOLEAN).description("성공 여부")
                        )
                ));

    }

    @DisplayName("회원 정보 조회")
    @Test
    void readMember() throws Exception {
        String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0VXNlciIsImlhdCI6MTcyNDI0NjkxNSwiZXhwIjo3MTI0MjQ2OTE1fQ.ZDA54NLIsUYoYdOVc8q7UqOFsq8uDEb2KMsBOaQsDcY";
        String refreshToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0VXNlciIsImlhdCI6MTcyNDI0NjkxNSwiZXhwIjoxODMxNjQyNDY5MTV9.dxt-wjPErgx8gez84Ga2XjL6aG_mjDxVS_sNQtU3cas";
        String loginId = "testUser";
        GetMemberResponse getMemberResponse = GetMemberResponse.builder()
                .id(1L)
                .loginId(loginId)
                .name("test")
                .password("test")
                .goal("goal")
                .build();

        when(authenticationService.validateToken(accessToken, refreshToken)).thenReturn(loginId);

        when(memberService.findMember(loginId)).thenReturn(getMemberResponse);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/members")
                        .header("Access-Token", accessToken)
                        .header("Refresh-Token", refreshToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document(snippetPath,
                        "회원 정보를 조회하는 API",
                        requestHeaders(
                                headerWithName("Access-Token").description("액세스 토큰"),
                                headerWithName("Refresh-Token").description("리프레시 토큰")
                        ),
                        responseFields(
                                fieldWithPath("header.resultCode").type(JsonFieldType.NUMBER).description("결과 코드"),
                                fieldWithPath("header.successful").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                fieldWithPath("body.data.id").type(JsonFieldType.NUMBER).description("사용자 고유 번호"),
                                fieldWithPath("body.data.name").type(JsonFieldType.STRING).description("사용자명"),
                                fieldWithPath("body.data.loginId").type(JsonFieldType.STRING).description("로그인 아이디"),
                                fieldWithPath("body.data.password").type(JsonFieldType.STRING).description("단방향 암호화 비밀번호"),
                                fieldWithPath("body.data.goal").type(JsonFieldType.STRING).description("공부 목표")
                        )
                ));
    }
}