package com.PuzzleU.Server.position.entity;

import com.PuzzleU.Server.team.entity.Team;
import com.PuzzleU.Server.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "position")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long positionId;

    @Column(name = "position_name", length = 10)
    private String positionName; // 포지션 이름

    @OneToMany(mappedBy = "userPosition1")
    private List<User> userList1 = new ArrayList<>();

    @OneToMany(mappedBy = "userPosition2")
    private List<User> userList2 = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team")
    private Team team;
}
