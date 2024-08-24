package com.partimestudy.studywork.challenge.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int minDeposit;
    private int maxDeposit;
    @Setter
    private String status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "challenge", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Registration> registrations = new ArrayList<>();

    /**
     * 주문 추가 메서드.
     *
     * @param registration 주문
     */
    public void addRegistration(Registration registration){
        this.registrations.add(registration);
        registration.setChallenge(this);
    }

}
