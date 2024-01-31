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
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPositionRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userPositionRelationId;

    // 의존 관계 매핑 (User)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // 의존관계 매핑 (Position)
    @ManyToOne
    @JoinColumn(name = "position_id")
    private Position position;
}
