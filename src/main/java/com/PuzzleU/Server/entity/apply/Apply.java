package com.PuzzleU.Server.entity.apply;

import com.PuzzleU.Server.entity.BaseEntity;
import com.PuzzleU.Server.entity.enumSet.ApplyStatus;
import com.PuzzleU.Server.entity.team.Team;
import com.PuzzleU.Server.entity.university.University;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "apply")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
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
    @JoinColumn(name = "team_id")
    private Team team;
}
