package com.PuzzleU.Server.entity.location;

import com.PuzzleU.Server.entity.relations.UserLocationRelation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "location")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long LocationId;

    @Column(name = "location_name")
    private String LocationName;

    @OneToMany(mappedBy = "Location")
    private List<UserLocationRelation> userLocationRelation = new ArrayList<>();

}
