package com.partimestudy.studyweek.challenge.repository;

import com.partimestudy.studyweek.challenge.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

}
