package com.partimestudy.studyweek.challenge.service;

import com.partimestudy.studyweek.challenge.dto.PostRegistrationRequest;
import com.partimestudy.studyweek.challenge.dto.PostRegistrationResponse;

public interface RegistrationService {
    PostRegistrationResponse makeRegistration(PostRegistrationRequest postRegistrationRequest, String loginId);
}
