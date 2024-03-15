package com.PuzzleU.Server.notify.entity;

import com.PuzzleU.Server.common.enumSet.BaseEntity;
import com.PuzzleU.Server.common.enumSet.NotificationType;
import com.PuzzleU.Server.friendship.entity.FriendShip;
import com.PuzzleU.Server.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private String url;
    @Column(nullable = false)
    private Boolean isRead;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType notificationType;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;



    public Notification() {

    }

    @Builder
    public Notification(Long id, String content, String url, Boolean isRead, NotificationType notificationType, User user) {
        this.id = id;
        this.content = content;
        this.url = url;
        this.isRead = isRead;
        this.notificationType = notificationType;
        this.user = user;
    }
    public void read()
    {
        this.isRead = true;
    }
}
