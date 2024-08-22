package com.partimestudy.studyweek.challenge.service;

import com.partimestudy.studyweek.challenge.dto.GetRegistrationResponse;
import com.partimestudy.studyweek.challenge.dto.PostRegistrationRequest;

public interface RegistrationService {
    void makeRegistration(PostRegistrationRequest postRegistrationRequest, String loginId);
    GetRegistrationResponse findRegistration(Long registrationId);
}
