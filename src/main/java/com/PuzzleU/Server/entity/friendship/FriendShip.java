package com.PuzzleU.Server.entity.friendship;

import com.PuzzleU.Server.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "friendship")
public class FriendShip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long FriendShipId;

    @ManyToOne
    @JoinColumn(name = "users1")
    private User user1;

    @ManyToOne
    @JoinColumn(name = "users2")
    private User user2;

    @Column(name = "UserStatus")
    private Boolean UserStatus;


}
