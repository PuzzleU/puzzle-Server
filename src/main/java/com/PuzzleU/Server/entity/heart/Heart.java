package com.PuzzleU.Server.entity.heart;

import com.PuzzleU.Server.entity.competition.Competition;
import com.PuzzleU.Server.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Heart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long HeartId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "competition_id")
    private Competition competition;


}
