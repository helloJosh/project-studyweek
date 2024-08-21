package com.partimestudy.studyweek.challenge.entity;

import com.partimestudy.studyweek.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String challengeName;
    private int challengeDeposit;
    private int challengePaymentAmount;
    private String status;

    private LocalDateTime createdAt;

    @Setter
    @ManyToOne(optional = false)
    private Challenge challenge;
    @Setter
    @ManyToOne(optional = false)
    private Member member;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "registration", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChallengeSchedule> schedules = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    /**
     * 챌린지 스케쥴 추가 메서드.
     *
     * @param challengeSchedule 첼린지 스케줄
     */
    public void addChallengeSchedule(ChallengeSchedule challengeSchedule) {
        this.schedules.add(challengeSchedule);
        challengeSchedule.setRegistration(this);
    }

}
