package com.PuzzleU.Server.apply.entity;

import com.PuzzleU.Server.common.enumSet.ApplyStatus;
import com.PuzzleU.Server.team.entity.Team;
import com.PuzzleU.Server.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "apply")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Apply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applyId;

    @Column(length = 30)
    private String applyTitle; // 지원서 제목

    @Column(length = 500)
    private String applyContent; // 지원서 내용

    @ColumnDefault("'WAITING'") // 따옴표 써야 SQL에서 인식함
    @Enumerated(value = EnumType.STRING)
    private ApplyStatus applyStatus; // 지원 상태 (대기/완료/취소)

    // 의존 관계 매핑 (Team)
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "user")
    private User user;
}
