package com.PuzzleU.Server.user.entity;

import com.PuzzleU.Server.apply.entity.Apply;
import com.PuzzleU.Server.common.enumSet.UniversityStatus;
import com.PuzzleU.Server.common.enumSet.UserRoleEnum;
import com.PuzzleU.Server.common.enumSet.WorkType;
import com.PuzzleU.Server.experience.entity.Experience;
import com.PuzzleU.Server.friendship.entity.FriendShip;
import com.PuzzleU.Server.heart.entity.Heart;
import com.PuzzleU.Server.major.entity.Major;
import com.PuzzleU.Server.position.entity.Position;
import com.PuzzleU.Server.profile.entity.Profile;
import com.PuzzleU.Server.relations.entity.UserInterestRelation;
import com.PuzzleU.Server.relations.entity.UserLocationRelation;
import com.PuzzleU.Server.relations.entity.UserSkillsetRelation;
import com.PuzzleU.Server.university.entity.University;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

// 나중에 추가가능
@Getter
@Entity(name="users")
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length =20)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;


    // 필수 정보

    @Column(nullable = true, length = 20)
    private String userPuzzleId;

    @Column(nullable = true, length = 6)
    private String userKoreaName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_profile_id",nullable = true)
    private Profile userProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_position_id1")
    private Position userPosition1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_position_id2")
    private Position userPosition2;


    // 선택 정보
    @Column(nullable = true)
    @Enumerated(value = EnumType.STRING)
    private UniversityStatus universityStatus;

    @Column(nullable = true)
    private Integer universityStart;

    @Column(nullable = true)
    private Integer universityEnd;

    @Column(nullable = true)
    @Enumerated(value = EnumType.STRING)
    private WorkType workType;

    @Column(nullable = true, length = 100)
    private String userRepresentativeExperience;

    @Column(nullable = true, length = 100)
    private String userRepresentativeProfileSentence;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="university_id")
    private University university;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="major_id")
    private Major major;


    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<UserInterestRelation> userInterestRelations = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE)
    private List<UserLocationRelation> userLocationRelations = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE)
    private List<UserSkillsetRelation> userSkillsetRelations = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE)
    private List<Experience> experience = new ArrayList<>();

    @OneToMany(mappedBy = "user1",cascade = CascadeType.REMOVE)
    private List<FriendShip> friendShip1 = new ArrayList<>();

    @OneToMany(mappedBy = "user2",cascade = CascadeType.REMOVE)
    private List<FriendShip> friendShip2 = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE)
    private List<Heart> hearts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Apply> applies = new ArrayList<>();
    @Builder
    public User(
            Long id,
            String username,
            String password,
            UserRoleEnum role,
            String userPuzzleId,
            String userKoreaName,
            Profile userProfile
    ) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.userPuzzleId = userPuzzleId;
        this.userKoreaName = userKoreaName;
        this.userProfile = userProfile;
    }

    public static User of(String username, String password, UserRoleEnum role)
    {
        return User.builder()
                .username(username)
                .password(password)
                .role(role)
                .build();
    }
}
