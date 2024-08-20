package com.partimestudy.studyweek.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.partimestudy.studyweek.BaseDocumentTest;
import com.partimestudy.studyweek.member.dto.PostMemberRequest;
import com.partimestudy.studyweek.member.service.MemberService;
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


        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/members")
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
}