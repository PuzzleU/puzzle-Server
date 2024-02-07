package com.PuzzleU.Server.friendship.entity;

import com.PuzzleU.Server.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

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


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users1")
    private User user1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users2")
    private User user2;

    @ColumnDefault("false")
    @Column(name = "user_status")
    private Boolean userStatus;


}
