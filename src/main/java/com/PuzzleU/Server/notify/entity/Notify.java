package com.PuzzleU.Server.notify.entity;

import com.PuzzleU.Server.common.enumSet.BaseEntity;
import com.PuzzleU.Server.common.enumSet.NotificationType;
import com.PuzzleU.Server.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
public class Notify extends BaseEntity {
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


    @Builder
    public Notify(User user, NotificationType notificationType, String content, String url, Boolean isRead) {
        this.user = user;
        this.notificationType = notificationType;
        this.content = content;
        this.url = url;
        this.isRead = isRead;

    }

    public Notify() {

    }
}
