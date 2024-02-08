package com.PuzzleU.Server.team.entity;

import com.PuzzleU.Server.common.enumSet.BaseEntity;
import com.PuzzleU.Server.apply.entity.Apply;
import com.PuzzleU.Server.competition.entity.Competition;
import com.PuzzleU.Server.position.entity.Position;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "team")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Team extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;

    @Column(name="team_title", nullable = true, length = 30)
    private String teamTitle;

    @Column(name = "team_member_now", nullable = true)
    private Integer teamMemberNow;

    @Column(name = "team_member_need", nullable = true)
    private Integer teamMemberNeed;

    @ColumnDefault("true")
    @Column(name = "team_untact", nullable = true)
    private boolean teamUntact;

    @Column(name = "team_url", nullable = true, length=200)
    private String teamUrl;

    @Column(name = "team_introduce", nullable = true, length = 300)
    private String teamIntroduce;

    @Column(name = "team_content", nullable = true, length = 500)
    private String teamContent;

    @ColumnDefault("true")
    @Column(name = "team_status",nullable = true)
    private Boolean teamStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competition_id")
    private Competition competition;

    @OneToMany(mappedBy = "team", cascade = CascadeType.REMOVE)
    private List<Apply> applyList = new ArrayList<>();

    @OneToMany(mappedBy = "team", cascade = CascadeType.REMOVE)
    private List<Position> positionList = new ArrayList<>();
}
