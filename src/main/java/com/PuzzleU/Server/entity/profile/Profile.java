package com.PuzzleU.Server.entity.profile;

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
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ProfileId;

    @Column(name = "profile_url")
    private String ProfielUrl;

    @OneToOne
    private User user;
}
