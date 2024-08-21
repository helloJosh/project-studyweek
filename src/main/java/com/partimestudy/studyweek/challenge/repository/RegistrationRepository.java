package com.partimestudy.studyweek.challenge.repository;

import com.partimestudy.studyweek.challenge.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
}
