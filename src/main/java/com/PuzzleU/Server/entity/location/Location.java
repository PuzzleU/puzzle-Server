package com.PuzzleU.Server.entity.location;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name ="location")
@Getter
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long LocationId;

    @Column(name = "LocationName")
    private String LocationName;

}
