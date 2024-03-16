package com.PuzzleU.Server.user.entity;

import com.PuzzleU.Server.apply.entity.Apply;
import com.PuzzleU.Server.common.enumSet.LoginType;
import com.PuzzleU.Server.common.enumSet.UserRoleEnum;
import com.PuzzleU.Server.common.enumSet.WorkType;
import com.PuzzleU.Server.experience.entity.Experience;
import com.PuzzleU.Server.friendship.entity.FriendShip;
import com.PuzzleU.Server.heart.entity.Heart;
import com.PuzzleU.Server.major.entity.Major;
import com.PuzzleU.Server.notify.entity.Notification;
import com.PuzzleU.Server.position.entity.Position;
import com.PuzzleU.Server.profile.entity.Profile;
import com.PuzzleU.Server.relations.entity.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

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

    @Column(nullable = true)
    @Enumerated(value = EnumType.STRING)
    private LoginType loginType;



    // 카카오 로그인 refresh 토큰
    private String kakaoRefreshToken;

    // 혜택 수신 동의
    @Column(nullable = true)
    @ColumnDefault("false")
    private Boolean consentMarketing;

    // 필수 정보

    @Column(nullable = true, length = 20)
    private String userPuzzleId;

    @Column(nullable = true, length = 6)
    private String userKoreaName;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_profile_id",nullable = true)
    private Profile userProfile;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_position_id1")
    private Position userPosition1;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_position_id2")
    private Position userPosition2;

    private String refreshToken;
    // 선택 정보


    @Column(nullable = true)
    @Enumerated(value = EnumType.STRING)
    private WorkType workType;

    @Column(nullable = true, length = 100)
    private String userRepresentativeExperience;

    @Column(nullable = true, length = 100)
    private String userRepresentativeProfileSentence;




    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<UserUniversityRelation> userUniversities = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="major_id")
    private Major major;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<UserInterestRelation> userInterestRelations = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE)
    private List<UserLocationRelation> userLocationRelations = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE)
    private List<UserSkillsetRelation> userSkillsetRelations = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE)
    private List<Experience> experience = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user1",cascade = CascadeType.REMOVE)
    private List<FriendShip> friendShip1 = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user2",cascade = CascadeType.REMOVE)
    private List<FriendShip> friendShip2 = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE)
    private List<Heart> hearts = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Apply> applies = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<TeamUserRelation> teamUserRelations = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE)
    private List<Notification> notifies = new ArrayList<>();

    @Builder
    public User(LoginType socialType, String socialId) {
        this.loginType = socialType;
        this.username = socialId;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void resetRefreshToken() {
        this.refreshToken = null;
    }

    public static User of(LoginType loginType,  String username, String password, UserRoleEnum role) {
        User user = new User(loginType, username); // 일반 로그인 타입으로 사용자 생성
        user.setPassword(password); // 패스워드 설정
        user.setRole(role); // 역할 설정
        return user; // 사용자 반환
    }

}
