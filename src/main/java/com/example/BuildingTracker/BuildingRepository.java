package com.example.BuildingTracker;

import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.UUID;

public interface BuildingRepository extends Repository<BuildingEntity, UUID> {

    BuildingEntity save(BuildingEntity building);

    Optional<BuildingEntity> findById(UUID uuid);

    Optional<BuildingEntity> findByName(String name);

    void deleteById(UUID uuid);
}
