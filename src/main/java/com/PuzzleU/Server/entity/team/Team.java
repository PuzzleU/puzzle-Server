package com.PuzzleU.Server.entity.team;

import com.PuzzleU.Server.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DialectOverride;

@Entity
@Table(name = "team")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Team extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long TeamId;

    @Column(name="Team_title", nullable = true, length = 30)
    private String TeamTitle;

    @Column(name = "Team_member_now", nullable = true)
    private Integer TeamMemberNow;

    @Column(name = "Team_member_need", nullable = true)
    private Integer TeamMemberNeed;

    @ColumnDefault("true")
    @Column(name = "Team_untact", nullable = true)
    private boolean TeamUntact;

    @Column(name = "Team_url", nullable = true, length=200)
    private String TeamUrl;

    @Column(name = "Team_introduce", nullable = true, length = 300)
    private String TeamIntroduce;

    @Column(name = "Team_content", nullable = true, length = 500)
    private String TeamContent;

    @ColumnDefault("true")
    @Column(name = "Team_status",nullable = true)
    private Boolean TeamStatus;


}
