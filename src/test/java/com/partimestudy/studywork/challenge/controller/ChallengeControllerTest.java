package com.partimestudy.studywork.challenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.partimestudy.studywork.BaseDocumentTest;
import com.partimestudy.studywork.auth.service.AuthenticationService;
import com.partimestudy.studywork.challenge.dto.ChallengeScheduleRequest;
import com.partimestudy.studywork.challenge.dto.ChallengeScheduleResponse;
import com.partimestudy.studywork.challenge.dto.GetRegistrationResponse;
import com.partimestudy.studywork.challenge.dto.PostRegistrationRequest;
import com.partimestudy.studywork.challenge.service.RegistrationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ChallengeController.class)
class ChallengeControllerTest extends BaseDocumentTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegistrationService registrationService;

    @MockBean
    private AuthenticationService authenticationService;

    @DisplayName("새로운 챌린지 등록")
    @Test
    void registerChallenge() throws Exception {
        PostRegistrationRequest request = PostRegistrationRequest.builder()
                .challengeId(1L)
                .challengeName("월요일 1시간 공부하기")
                .deposit(10000)
                .challengeSchedules(List.of(new ChallengeScheduleRequest(LocalDateTime.now(), 1)))
                .build();

        String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0VXNlciIsImlhdCI6MTYzMjQyNzIxMCwiZXhwIjoxOTk5ODI3MjEwfQ.dQssvFfCfh62nbyVhueW5STXYVG1aIrZ3yWnB5hoF6U";
        String refreshToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0VXNlciIsImlhdCI6MTYzMjQyNzIxMCwiZXhwIjoxOTk5ODI3MjEwfQ.dQssvFfCfh62nbyVhueW5STXYVG1aIrZ3yWnB5hoF6U";

        String loginId = "testUser";

        when(authenticationService.validateToken(accessToken, refreshToken)).thenReturn(loginId);
        doNothing().when(registrationService).makeRegistration(request, loginId);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/challenges/registrations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Access-Token", accessToken)
                        .header("Refresh-Token", refreshToken)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document(snippetPath,
                        "챌린지를 등록하는 API",
                        requestHeaders(
                                headerWithName("Access-Token").description("액세스 토큰"),
                                headerWithName("Refresh-Token").description("리프레시 토큰")
                        ),
                        requestFields(
                                fieldWithPath("challengeId").type(JsonFieldType.NUMBER).description("챌린지 고유 ID"),
                                fieldWithPath("challengeName").type(JsonFieldType.STRING).description("챌린지명"),
                                fieldWithPath("deposit").type(JsonFieldType.NUMBER).description("보증금"),
                                fieldWithPath("challengeSchedules").type(JsonFieldType.ARRAY).description("챌린지 스케줄 목록"),
                                fieldWithPath("challengeSchedules[].applyDate").type(JsonFieldType.STRING).description("스케줄 적용 날짜"),
                                fieldWithPath("challengeSchedules[].hours").type(JsonFieldType.NUMBER).description("공부 시간")
                        ),
                        responseFields(
                                fieldWithPath("header.resultCode").type(JsonFieldType.NUMBER).description("결과 코드"),
                                fieldWithPath("header.successful").type(JsonFieldType.BOOLEAN).description("성공 여부")
                        )
                ));
    }

    @DisplayName("챌린지 등록 정보 조회")
    @Test
    void getChallengeRegistration() throws Exception {
        String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0VXNlciIsImlhdCI6MTYzMjQyNzIxMCwiZXhwIjoxOTk5ODI3MjEwfQ.dQssvFfCfh62nbyVhueW5STXYVG1aIrZ3yWnB5hoF6U";
        String refreshToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0VXNlciIsImlhdCI6MTYzMjQyNzIxMCwiZXhwIjoxOTk5ODI3MjEwfQ.dQssvFfCfh62nbyVhueW5STXYVG1aIrZ3yWnB5hoF6U";

        Long registrationId = 1L;

        GetRegistrationResponse response = GetRegistrationResponse.builder()
                .challengeName("월요일 1시간 공부하기")
                .challengeDeposit(10000)
                .challengePaymentAmount(10000)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .challengeSchedule(List.of(
                        new ChallengeScheduleResponse(LocalDateTime.now(), 1)
                ))
                .build();

        when(authenticationService.validateToken(accessToken, refreshToken)).thenReturn("testUser");
        when(registrationService.findRegistration(registrationId)).thenReturn(response);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/challenges/registrations/{registrationsId}", registrationId)
                        .header("Access-Token", accessToken)
                        .header("Refresh-Token", refreshToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document(snippetPath,
                        "챌린지 등록 정보를 조회하는 API",
                        requestHeaders(
                                headerWithName("Access-Token").description("액세스 토큰"),
                                headerWithName("Refresh-Token").description("리프레시 토큰")
                        ),
                        responseFields(
                                fieldWithPath("header.resultCode").type(JsonFieldType.NUMBER).description("결과 코드"),
                                fieldWithPath("header.successful").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                fieldWithPath("body.data.challengeName").type(JsonFieldType.STRING).description("챌린지명"),
                                fieldWithPath("body.data.challengeDeposit").type(JsonFieldType.NUMBER).description("보증금"),
                                fieldWithPath("body.data.challengePaymentAmount").type(JsonFieldType.NUMBER).description("결제 금액"),
                                fieldWithPath("body.data.status").type(JsonFieldType.STRING).description("챌린지 상태"),
                                fieldWithPath("body.data.createdAt").type(JsonFieldType.STRING).description("생성일자"),
                                fieldWithPath("body.data.challengeSchedule").type(JsonFieldType.ARRAY).description("챌린지 스케줄 목록"),
                                fieldWithPath("body.data.challengeSchedule[].applyDate").type(JsonFieldType.STRING).description("스케줄 적용 날짜"),
                                fieldWithPath("body.data.challengeSchedule[].hours").type(JsonFieldType.NUMBER).description("공부 시간")
                        )
                ));
    }
}