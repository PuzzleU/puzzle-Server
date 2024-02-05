package com.PuzzleU.Server.entity.relations;

import com.PuzzleU.Server.entity.interest.Interest;
import com.PuzzleU.Server.entity.location.Location;
import com.PuzzleU.Server.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_location_relation")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLocationRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userLocationRelationId;

    // 의존 관계 매핑 (User)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private com.PuzzleU.Server.entity.user.User user;

    // 의존관계 매핑 (Location)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;
}
