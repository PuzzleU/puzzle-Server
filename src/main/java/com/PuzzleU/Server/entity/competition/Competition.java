package com.PuzzleU.Server.entity.competition;

import com.PuzzleU.Server.entity.BaseEntity;
import com.PuzzleU.Server.entity.heart.Heart;
import com.PuzzleU.Server.entity.interest.Interest;
import com.PuzzleU.Server.entity.relations.CompetitionInterestRelation;
import com.PuzzleU.Server.entity.team.Team;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// Competition Entity 생성
@Entity
@Table(name = "competition")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Competition extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long competitionId;

    @Column(name = "competition_name", nullable = true, length = 100)
    private String competitionName;

    @Column(name =  "competition_url",nullable = true, length = 200)
    private String competitionUrl;

    @Column(name = "competition_host", nullable = true, length = 50)
    private String competitionHost;

    @Column(name = "competition_poster", nullable = true, length = 200)
    private String competitionPoster;

    @Column(name = "competition_awards", nullable = true, length = 50)
    private String competitionAwards;

    @Column(name = "competition_start", nullable = true)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date competitionStart;

    @Column(name = "competition_end", nullable = true)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date competitionEnd;

    @Column(name = "competition_content", nullable = true, length = 2000)
    private String competitionContent;

    @Column(name = "competition_visit", nullable = true)
    private Integer competitionVisit;

    @Column(name = "competition_like", nullable = true)
    private Integer competitionLike;

    @Column(name = "competition_matching", nullable = true)
    private Integer competitionMatching;

    @Column(name = "competition_d_day", nullable = true)
    private Integer competitionDDay;


    @OneToMany(mappedBy = "competition",cascade = CascadeType.ALL)
    private List<Team> team = new ArrayList<>();

    @OneToMany(mappedBy = "competition",cascade = CascadeType.ALL)
    private List<Heart> heart = new ArrayList<>();

    @OneToMany(mappedBy = "competition", cascade = CascadeType.REMOVE)
    private List<CompetitionInterestRelation> competitionInterestRelations = new ArrayList<>();

}
