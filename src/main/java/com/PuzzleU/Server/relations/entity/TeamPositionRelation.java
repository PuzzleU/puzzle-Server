package com.PuzzleU.Server.relations.entity;

import com.PuzzleU.Server.position.entity.Position;
import com.PuzzleU.Server.team.entity.Team;
import com.PuzzleU.Server.university.entity.University;
import com.PuzzleU.Server.user.entity.User;
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
public class TeamPositionRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamPositionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id")
    private Position position;
}
