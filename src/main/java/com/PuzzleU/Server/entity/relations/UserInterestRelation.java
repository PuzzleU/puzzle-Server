package com.PuzzleU.Server.entity.relations;

import com.PuzzleU.Server.entity.apply.Apply;
import com.PuzzleU.Server.entity.interest.Interest;
import com.PuzzleU.Server.entity.position.Position;
import com.PuzzleU.Server.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "userInterestRelation")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInterestRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long UserInterestRelationId;

    // 의존 관계 매핑 (User)
    @ManyToOne
    @JoinColumn(name = "UserId")
    private User User;

    // 의존관계 매핑 (Interest)
    @ManyToOne
    @JoinColumn(name = "InterestId")
    private Interest Interest;
}
