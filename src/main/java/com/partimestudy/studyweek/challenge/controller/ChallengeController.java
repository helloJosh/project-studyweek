package com.partimestudy.studyweek.challenge.controller;

import com.partimestudy.studyweek.api.Response;
import com.partimestudy.studyweek.auth.service.AuthenticationService;
import com.partimestudy.studyweek.challenge.dto.PostRegistrationRequest;
import com.partimestudy.studyweek.challenge.dto.GetRegistrationResponse;
import com.partimestudy.studyweek.challenge.exception.PostRegistrationRequestFormErrorException;
import com.partimestudy.studyweek.challenge.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ChallengeController {
    private final RegistrationService registrationService;
    private final AuthenticationService authenticationService;


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

    @GetMapping("/challenges/registrations/{registrationsId}")
    public Response<GetRegistrationResponse> getRegistration(
            @PathVariable Long registrationsId,
            @RequestHeader("Access-Token") String accessToken,
            @RequestHeader("Refresh-Token") String refreshToken){
        authenticationService.validateToken(accessToken, refreshToken);

        return Response.success(registrationService.findRegistration(registrationsId));
    }
}
