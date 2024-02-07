package com.PuzzleU.Server.location.entity;

import com.PuzzleU.Server.relations.entity.UserLocationRelation;
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
    private Long locationId;

    @Column(name = "location_name")
    private String locationName;

    @OneToMany(mappedBy = "location",cascade = CascadeType.REMOVE)
    private List<UserLocationRelation> userLocationRelation = new ArrayList<>();

}
