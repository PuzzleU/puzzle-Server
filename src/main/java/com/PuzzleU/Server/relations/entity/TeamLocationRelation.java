package com.PuzzleU.Server.relations.entity;

import com.PuzzleU.Server.location.entity.Location;
import com.PuzzleU.Server.team.entity.Team;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "team_location_relation")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeamLocationRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamLocationRelationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;
}
