package com.PuzzleU.Server.entity.user;

import com.PuzzleU.Server.entity.enumSet.UniversityStatus;
import com.PuzzleU.Server.entity.enumSet.UserRoleEnum;
import com.PuzzleU.Server.entity.enumSet.WorkType;
import com.PuzzleU.Server.entity.experience.Experience;
import com.PuzzleU.Server.entity.friendship.FriendShip;
import com.PuzzleU.Server.entity.heart.Heart;
import com.PuzzleU.Server.entity.major.Major;
import com.PuzzleU.Server.entity.profile.Profile;
import com.PuzzleU.Server.entity.relations.UserInterestRelation;
import com.PuzzleU.Server.entity.relations.UserLocationRelation;
import com.PuzzleU.Server.entity.relations.UserPositionRelation;
import com.PuzzleU.Server.entity.relations.UserSkillsetRelation;
import com.PuzzleU.Server.entity.university.University;
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
    @JoinColumn(name = "user_profile",nullable = true)
    private Profile userProfile;

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
    private List<UserPositionRelation> userPositionRelations = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE)
    private List<UserSkillsetRelation> userSkillsetRelations = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Experience> experience = new ArrayList<>();

    @OneToMany(mappedBy = "user1",cascade = CascadeType.REMOVE)
    private List<FriendShip> friendShip1 = new ArrayList<>();

    @OneToMany(mappedBy = "user2",cascade = CascadeType.REMOVE)
    private List<FriendShip> friendShip2 = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Heart> hearts = new ArrayList<>();

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
