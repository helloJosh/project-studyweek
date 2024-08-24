package com.partimestudy.studywork.challenge.repository;

import com.partimestudy.studywork.challenge.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 챌린지 리포지토리 인터페이스.
 *
 * @author 김병우
 */
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
}
