package com.PuzzleU.Server.entity.position;

import com.PuzzleU.Server.entity.relations.UserPositionRelation;
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
    private Long PositionId;

    @Column(name = "position_name", length = 10)
    private String PositionName; // 포지션 이름

    @OneToMany(mappedBy = "Position",cascade = CascadeType.ALL)
    private List<UserPositionRelation> userPositionRelation = new ArrayList<>();
}
