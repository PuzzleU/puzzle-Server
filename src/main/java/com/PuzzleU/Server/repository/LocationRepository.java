package com.PuzzleU.Server.repository;

import com.PuzzleU.Server.entity.location.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByLocationId(Long locationId);
}
