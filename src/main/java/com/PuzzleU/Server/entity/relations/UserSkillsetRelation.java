package com.PuzzleU.Server.entity.relations;

import com.PuzzleU.Server.entity.enumSet.Level;
import com.PuzzleU.Server.entity.position.Position;
import com.PuzzleU.Server.entity.skillset.Skillset;
import com.PuzzleU.Server.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_skillset_relation")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSkillsetRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long UserSkillsetRelationId;
//a
    private Level Level;

    // 의존 관계 매핑 (User)
    @ManyToOne
    @JoinColumn(name = "id")
    private User User;

    // 의존관계 매핑 (Skillset)
    @ManyToOne
    @JoinColumn(name = "skillset_id")
    private Skillset Skillset;
}
