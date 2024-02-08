package com.PuzzleU.Server.relations.entity;

import com.PuzzleU.Server.team.entity.Team;
import com.PuzzleU.Server.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "team_user_relation")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class TeamUserRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamUserRelationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private User user;

    @ColumnDefault("false")
    @Column(nullable = false)
    private Boolean isWriter;

}
