package com.PuzzleU.Server.entity.relations;

import com.PuzzleU.Server.entity.apply.Apply;
import com.PuzzleU.Server.entity.position.Position;
import com.PuzzleU.Server.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "userPositionRelation")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPositionRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long UserPositionRelationId;

    // 의존 관계 매핑 (User)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User User;

    // 의존관계 매핑 (Position)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "positionId")
    private Position Position;
}
