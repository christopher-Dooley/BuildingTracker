package com.example.BuildingTracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static com.example.BuildingTracker.BuildingTransformer.toDto;
import static com.example.BuildingTracker.BuildingTransformer.toEntity;

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

    public Optional<BuildingDTO> findBuildingByUUID(UUID uuid) {
        return repository.findById(uuid).map(BuildingTransformer::toDto);
    }

    public Optional<BuildingDTO> findBuildingByName(String name) {
        return repository.findByName(name).map(BuildingTransformer::toDto);
    }

    public Optional<BuildingDTO> newBuilding(BuildingDTO dto) {
        try {
            fillUuidIfNull(dto);
            fillCoordsIfNull(dto);
            BuildingEntity savedEntity = repository.save(toEntity(dto));
            return Optional.of(toDto(savedEntity));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private void fillUuidIfNull(BuildingDTO dto) {
        if (dto.getUuid() == null) {
            dto.setUuid(UUID.randomUUID());
        }
    }

    private void fillCoordsIfNull(BuildingDTO dto) {
        if (dto.getLongitude() == null ||  dto.getLatitude() == null) {
            //TODO add coord request here
            dto.setLongitude(1);
            dto.setLatitude(1);
        }
    }
}
