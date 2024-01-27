package com.PuzzleU.Server.entity.like;

import com.PuzzleU.Server.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String LikeId;

    @ManyToOne
    @JoinColumn(name = "users")
    private User UserId;


}
