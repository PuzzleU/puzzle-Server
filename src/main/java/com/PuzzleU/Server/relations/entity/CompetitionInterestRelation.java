package com.PuzzleU.Server.relations.entity;

import com.PuzzleU.Server.competition.entity.Competition;
import com.PuzzleU.Server.interest.entity.Interest;
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
