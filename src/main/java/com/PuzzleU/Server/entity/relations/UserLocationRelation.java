package com.PuzzleU.Server.entity.relations;

import com.PuzzleU.Server.entity.interest.Interest;
import com.PuzzleU.Server.entity.location.Location;
import com.PuzzleU.Server.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "userLocationRelation")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLocationRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long UserLocationRelationId;

    // 의존 관계 매핑 (User)
    @ManyToOne
    @JoinColumn(name = "UserId")
    private com.PuzzleU.Server.entity.user.User User;

    // 의존관계 매핑 (Location)
    @ManyToOne
    @JoinColumn(name = "LocationId")
    private Location Location;
}
