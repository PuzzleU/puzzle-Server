package com.PuzzleU.Server.entity.heart;

import com.PuzzleU.Server.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "heart")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Heart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String HeartId;


    @ManyToOne
    @JoinColumn(name = "id")
    private User user;


}
