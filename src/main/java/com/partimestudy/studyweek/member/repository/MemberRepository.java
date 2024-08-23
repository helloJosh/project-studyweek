package com.partimestudy.studyweek.member.repository;

import com.partimestudy.studyweek.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 맴버 리포지토리.
 *
 * @author 김병우
 */
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsMemberByLoginId(String loginId);
    Optional<Member> findMemberByLoginId(String loginId);
}
