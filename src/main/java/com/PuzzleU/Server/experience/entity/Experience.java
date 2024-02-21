package com.PuzzleU.Server.experience.entity;

import com.PuzzleU.Server.common.enumSet.ExperienceType;
import com.PuzzleU.Server.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "experience")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Experience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long experienceId;

    @Column(length = 100)
    private String experienceName; // 경험 이름

    @Enumerated(EnumType.STRING)
    private ExperienceType experienceType; // 경험 종류 (CLUB, INTERNSHIP, PARTTIME, COMPETITION, CONTEST, OTHER)

    private Integer experienceStartYear; // 시작 년

    private Integer experienceStartMonth; // 시작 월

    private Integer experienceEndYear; // 종료 년

    private Integer experienceEndMonth; // 종료 월

    @ColumnDefault("true")
    private Boolean experienceStatus; // 현재 상태 (0: 종료 / 1: 진행 중)

    private String experienceRole; // 담당 업무

    //de
    // 의존 관계 매핑 (User)
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
