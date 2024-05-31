package com.example.BuildingTracker;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface BuildingRepository extends PagingAndSortingRepository<BuildingEntity, UUID> {

    BuildingEntity save(BuildingEntity building);

    Optional<BuildingEntity> findById(UUID uuid);

    Optional<BuildingEntity> findByName(String name);

    void deleteById(UUID uuid);

    Collection<BuildingEntity> findAll();
}
