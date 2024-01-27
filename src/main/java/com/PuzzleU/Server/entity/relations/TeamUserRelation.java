package com.PuzzleU.Server.entity.relations;

import com.PuzzleU.Server.entity.team.Team;
import com.PuzzleU.Server.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity(name = "teamUserRelation")
@Setter
public class TeamUserRelation {

    @Id
    @GeneratedValue
    private Long TeamUserRelationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TeamId")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserId")
    private User user;

}
