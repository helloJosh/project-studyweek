package com.partimestudy.studywork.challenge.repository;

import com.partimestudy.studywork.challenge.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 주문 리포지토리.
 *
 * @author 김병우
 */
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
}
