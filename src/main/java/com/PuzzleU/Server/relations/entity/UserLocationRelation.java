package com.PuzzleU.Server.relations.entity;

import com.PuzzleU.Server.location.entity.Location;
import com.PuzzleU.Server.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private User user;

    // 의존관계 매핑 (Location)
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;
}
