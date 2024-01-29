package com.PuzzleU.Server.entity.relations;

import com.PuzzleU.Server.entity.location.Location;
import com.PuzzleU.Server.entity.team.Team;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "teamLocationRelation")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor

public class TeamLocationRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long TeamLocationRelationId;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;
}
