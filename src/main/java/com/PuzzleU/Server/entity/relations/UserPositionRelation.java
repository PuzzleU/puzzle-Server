package com.PuzzleU.Server.entity.relations;

import com.PuzzleU.Server.entity.apply.Apply;
import com.PuzzleU.Server.entity.enumSet.Priority;
import com.PuzzleU.Server.entity.position.Position;
import com.PuzzleU.Server.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPositionRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userPositionRelationId;

    @Enumerated(EnumType.STRING)
    private Priority positionPriority;

    // 의존 관계 매핑 (User)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 의존관계 매핑 (Position)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id")
    private Position position;
}
