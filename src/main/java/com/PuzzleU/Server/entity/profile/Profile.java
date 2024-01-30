package com.PuzzleU.Server.entity.profile;

import com.PuzzleU.Server.entity.relations.UserInterestRelation;
import com.PuzzleU.Server.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "userProfile")
    private List<User> user = new ArrayList<>();
}
