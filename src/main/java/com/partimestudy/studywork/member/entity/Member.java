package com.partimestudy.studywork.member.entity;

import com.partimestudy.studywork.challenge.entity.Registration;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "login_id_unique",
                        columnNames = {"login_id"}
                )
        }
)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String loginId;
    private String password;
    private String goal;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Registration> registrations = new ArrayList<>();

    /**
     * 주문 추가 메서드.
     *
     * @param registration 주문
     */
    public void addRegistration(Registration registration){
        this.registrations.add(registration);
        registration.setMember(this);
    }
}
