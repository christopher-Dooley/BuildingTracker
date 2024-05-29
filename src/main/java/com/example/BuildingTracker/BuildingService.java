package com.example.BuildingTracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class BuildingService {

    private final BuildingRepository repository;

    @Autowired
    public BuildingService(BuildingRepository repository) {
        this.repository = repository;
    }

    public BuildingEntity saveBuilding(BuildingEntity building) {
        return repository.save(building);
    }

    public Optional<BuildingEntity> findBuildingByUUID(UUID uuid) {
        return repository.findById(uuid);
    }

    public Optional<BuildingEntity> findBuildingByName(String name) {
        return repository.findByName(name);
    }
}
