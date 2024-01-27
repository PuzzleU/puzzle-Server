package com.PuzzleU.Server.entity.like;

import com.PuzzleU.Server.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "like")
@Getter
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String LikeId;


    @ManyToOne
    @JoinColumn(name = "UserId")
    private User UserId;


}
