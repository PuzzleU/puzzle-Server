package com.PuzzleU.Server.relations.entity;

import com.PuzzleU.Server.apply.entity.Apply;
import com.PuzzleU.Server.position.entity.Position;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "position_apply_relation")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class PositionApplyRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long positionApplyRelationId;

    // 의존 관계 매핑 (Position)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id")
    private Position position;

    // 의존관계 매핑 (Apply)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apply_id")
    private Apply apply;
}