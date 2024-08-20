package com.partimestudy.studyweek.challenge.entity;

import com.partimestudy.studyweek.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String challengeName;
    private int challengeDeposit;
    private int challengePaymentAmount;
    private String memberStatus;
    private LocalDateTime applyDate;
    private LocalDateTime createdAt;
    private int hours;

    @ManyToOne
    private Challenge challenge;
    @ManyToOne
    private Member member;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}
