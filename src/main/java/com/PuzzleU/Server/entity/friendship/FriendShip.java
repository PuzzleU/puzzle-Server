package com.PuzzleU.Server.entity.friendship;

import com.PuzzleU.Server.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "friendship")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FriendShip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long friendShipId;


    @ManyToOne
    @JoinColumn(name = "users1")
    private User user1;

    @ManyToOne
    @JoinColumn(name = "users2")
    private User user2;

    @Column(name = "user_status")
    private Boolean userStatus;


}
