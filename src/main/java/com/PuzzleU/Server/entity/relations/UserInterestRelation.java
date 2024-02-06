package com.PuzzleU.Server.entity.relations;

import com.PuzzleU.Server.entity.interest.Interest;
import com.PuzzleU.Server.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "User_interest_relation")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInterestRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_interest_relation_id")
    private Long userInterestRelationId;

    // 의존 관계 매핑 (User)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private User user;

    // 의존관계 매핑 (Interest)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interest_id")
    private Interest interest;
}
