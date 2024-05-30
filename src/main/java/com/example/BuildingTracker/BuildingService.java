package com.example.BuildingTracker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static com.example.BuildingTracker.BuildingTransformer.toDto;
import static com.example.BuildingTracker.BuildingTransformer.toEntity;

@Service
public class BuildingService {

    private static final Logger log = LoggerFactory.getLogger(BuildingService.class);
    private final BuildingRepository repository;
    private final CoordinateRequestService coordinateRequestService;

    @Autowired
    public BuildingService(BuildingRepository repository, CoordinateRequestService coordinateRequestService) {
        this.repository = repository;
        this.coordinateRequestService = coordinateRequestService;
    }

    public Optional<BuildingDTO> findBuildingByUUID(UUID uuid) {
        return repository.findById(uuid).map(BuildingTransformer::toDto);
    }

    public Optional<BuildingDTO> findBuildingByName(String name) {
        return repository.findByName(name).map(BuildingTransformer::toDto);
    }

    public Optional<BuildingDTO> newBuilding(BuildingDTO dto) throws CoordinateRequestException {
        try {
            setUuidIfNull(dto);
            setCoordinatesIfNull(dto);
            BuildingEntity savedEntity = repository.save(toEntity(dto));
            return Optional.of(toDto(savedEntity));
        } catch (Exception e) {
            log.error("Saving new building failed", e);
            throw e;
        }
    }

    private void setUuidIfNull(BuildingDTO dto) {
        if (dto.getUuid() == null) {
            dto.setUuid(UUID.randomUUID());
        }
    }

    private void setCoordinatesIfNull(BuildingDTO dto) throws CoordinateRequestException {
        if (dto.getLongitude() == null ||  dto.getLatitude() == null) {
            coordinateRequestService.setCoordinatesForBuilding(dto);
        }
    }
}
