package com.PuzzleU.Server.entity.relations;

import com.PuzzleU.Server.entity.team.Team;
import com.PuzzleU.Server.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity(name = "teamuserrelation")
public class TeamUserRelation {

    @Id
    @GeneratedValue
    private Long TeamUserRelationId;

    @ManyToOne
    @JoinColumn(name = "TeamId")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "UserId")
    private User user;

}
