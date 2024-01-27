package com.PuzzleU.Server.entity.location;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name ="location")
@Getter
@Setter
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long LocationId;

    @Column(name = "LocationName")
    private String LocationName;

}
