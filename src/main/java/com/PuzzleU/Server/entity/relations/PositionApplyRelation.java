package com.PuzzleU.Server.entity.relations;

import com.PuzzleU.Server.entity.apply.Apply;
import com.PuzzleU.Server.entity.position.Position;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "positionApplyRelation")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class PositionApplyRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long PositionApplyRelationId;

    // 의존 관계 매핑 (Position)
    @ManyToOne
    @JoinColumn(name = "positionId")
    private Position Position;

    // 의존관계 매핑 (Apply)
    @ManyToOne
    @JoinColumn(name = "applyId")
    private Apply Apply;
}