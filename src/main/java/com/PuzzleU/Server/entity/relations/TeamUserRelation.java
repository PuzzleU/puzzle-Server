package com.PuzzleU.Server.entity.relations;

import com.PuzzleU.Server.entity.team.Team;
import com.PuzzleU.Server.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "team_user_relation")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor

public class TeamUserRelation {

    @Id
    @GeneratedValue
    private Long TeamUserRelationId;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "id")
    private User user;

}
