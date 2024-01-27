package com.PuzzleU.Server.entity.relations;

import com.PuzzleU.Server.entity.team.Team;
import com.PuzzleU.Server.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity(name = "teamuserrelations")
public class TeamUserRelation {

    @Id
    @GeneratedValue
    private Long TeamUserRelationId;

    @ManyToOne
    @JoinColumn(name = "teams")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "users")
    private User user;

}
