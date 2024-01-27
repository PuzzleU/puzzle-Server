package com.PuzzleU.Server.entity.relations;

import com.PuzzleU.Server.entity.location.Location;
import com.PuzzleU.Server.entity.team.Team;
import jakarta.persistence.*;
import lombok.Getter;

@Entity(name = "teamlocationrelation")
@Getter
public class TeamLocationRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long TeamLocationRelationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TeamId")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LocationId")
    private Location location;
}
