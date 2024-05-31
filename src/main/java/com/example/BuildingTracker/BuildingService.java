package com.example.BuildingTracker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.BuildingTracker.BuildingTransformer.toDto;
import static com.example.BuildingTracker.BuildingTransformer.toEntity;

@Service
public class BuildingService {

    private static final Logger log = LoggerFactory.getLogger(BuildingService.class);
    private final BuildingRepository repository;
    private final CoordinateRequestService coordinateRequestService;
    private final ThreadPool threadPool;

    @Autowired
    public BuildingService(BuildingRepository repository, CoordinateRequestService coordinateRequestService, ThreadPool threadPool) {
        this.repository = repository;
        this.coordinateRequestService = coordinateRequestService;
        this.threadPool = threadPool;
    }

    public Optional<BuildingDTO> findBuildingByUUID(UUID uuid) {
        return repository.findById(uuid).map(BuildingTransformer::toDto);
    }

    public Optional<BuildingDTO> findBuildingByName(String name) {
        return repository.findByName(name).map(BuildingTransformer::toDto);
    }

    public BuildingDTO saveBuilding(BuildingDTO dto) {
        try {
            setUuidIfNull(dto);
            setCoordinatesIfZero(dto);
            BuildingEntity savedEntity = repository.save(toEntity(dto));
            return toDto(savedEntity);
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

    // 0N, 0E is fine for a check since it's in the atlantic ocean with no buildings
    private void setCoordinatesIfZero(BuildingDTO dto) {
        if (dto.getLongitude() == 0 ||  dto.getLatitude() == 0) {
            coordinateRequestService.limitedSetCoordinatesForBuilding(dto);
        }
    }

    public void deleteBuilding(UUID uuid) {
        repository.deleteById(uuid);
    }

    // always update coordinates based on new address
    public BuildingDTO updateBuilding(BuildingDTO buildingDTO) {
        coordinateRequestService.limitedSetCoordinatesForBuilding(buildingDTO);
        BuildingEntity toUpdate = toEntity(buildingDTO);
        BuildingEntity updated = repository.save(toUpdate);
        return toDto(updated);
    }

    public void saveManyBuildings(Collection<BuildingDTO> buildings) {
        buildings.stream().map(dto -> (Runnable) () -> saveBuilding(dto)).forEach(threadPool::submitTask);
    }

    public Collection<BuildingDTO> findAll() {
        return repository.findAll().stream().map(BuildingTransformer::toDto).collect(Collectors.toSet());
    }

    public Page<BuildingDTO> findAllPaged(int page, int pageSize, boolean sort, String sortBy, boolean sortAscending) {
        Pageable pageRequest;
        if (sort) {
            Sort.Direction direction;
            if (sortAscending) {
                direction = Sort.Direction.ASC;
            } else {
                direction = Sort.Direction.DESC;
            }
            pageRequest = PageRequest.of(page, pageSize, Sort.by(direction, sortBy));
        } else {
            pageRequest = PageRequest.of(page, pageSize);
        }
        Page<BuildingEntity> entityPage = repository.findAll(pageRequest);
        return new PageImpl<>(
                entityPage.stream().map(BuildingTransformer::toDto).collect(Collectors.toList()),
                pageRequest,
                entityPage.getTotalPages()
        );
    }
}
