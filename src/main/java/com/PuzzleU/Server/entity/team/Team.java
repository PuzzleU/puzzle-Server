package com.PuzzleU.Server.entity.team;

import com.PuzzleU.Server.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DialectOverride;

@Entity
@Getter
@Setter
public class Team extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long TeamId;

    @Column(name="TeamTitle", nullable = true, length = 30)
    private String TeamTitle;

    @Column(name = "TeamMemberNow", nullable = true)
    private Integer TeamMemberNow;

    @Column(name = "TeamMemberNeed", nullable = true)
    private Integer TeamMemberNeed;

    @ColumnDefault("true")
    @Column(name = "TeamUntact", nullable = true)
    private boolean TeamUntact;

    @Column(name = "TeamUrl", nullable = true, length=200)
    private String TeamUrl;

    @Column(name = "TeamIntroduce", nullable = true, length = 300)
    private String TeamIntroduce;

    @Column(name = "TeamContent", nullable = true, length = 500)
    private String TeamContent;

    @ColumnDefault("true")
    @Column(name = "TeamStatus",nullable = true)
    private Boolean TeamStatus;


}
