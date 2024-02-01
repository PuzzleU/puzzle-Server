package com.PuzzleU.Server.entity.relations;

import com.PuzzleU.Server.entity.competition.Competition;
import com.PuzzleU.Server.entity.interest.Interest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "competition_interest_relation")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompetitionInterestRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long competitionInterestRelationId;

    // 의존 관계 매핑 (Competition)
    @ManyToOne
    @JoinColumn(name = "competition_id")
    private Competition competition;

    // 의존관계 매핑 (Interest)
    @ManyToOne
    @JoinColumn(name = "interest_id")
    private Interest interest;
}
