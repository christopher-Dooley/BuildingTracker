package com.example.BuildingTracker;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.Collection;
import java.util.UUID;

@RestController
public class BuildingController {

    private final BuildingService service;

    @Autowired
    public BuildingController(BuildingService service) {
        this.service = service;
    }


    @GetMapping("/building/name/{name}")
    public ResponseEntity<BuildingDTO> getBuildingByName(@PathVariable String name) {
        return service.findBuildingByName(name)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/building/id/{uuid}")
    public ResponseEntity<BuildingDTO> getBuildingByUuid(@PathVariable UUID uuid) {
        return service.findBuildingByUUID(uuid)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/building/save")
    public ResponseEntity<BuildingDTO> saveBuilding(@RequestBody BuildingDTO buildingDTO, HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        try {
            BuildingDTO dto = service.saveBuilding(buildingDTO);
            URI location = getNewLocationUri(requestUri, dto.getUuid());
            return ResponseEntity.created(location).body(dto);
        } catch (CoordinateRequestException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private URI getNewLocationUri(String requestUri, UUID uuid) {
        return URI.create(
                requestUri.substring(0, requestUri.length() -4)
                .concat(uuid.toString())
        );
    }

    @DeleteMapping("/building/{uuid}/delete")
    public ResponseEntity<Void> deleteBuilding(@PathVariable UUID uuid) {
        service.deleteBuilding(uuid);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/building/update")
    public ResponseEntity<BuildingDTO> updateBuilding(@RequestBody BuildingDTO buildingDTO) {
        try {
            return ResponseEntity.ok(service.updateBuilding(buildingDTO));
        } catch (CoordinateRequestException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/buildings/save")
    public ResponseEntity<Void> saveManyBuildings(@RequestBody Collection<BuildingDTO> buildings) {
        service.saveManyBuildings(buildings);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/buildings/all")
    public ResponseEntity<Collection<BuildingDTO>> getAllBuildings() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/buildings/paged")
    public ResponseEntity<Page<BuildingDTO>> getAllBuildingsPaged(@RequestBody PagedRequest pagedRequest) {
        return ResponseEntity.ok(service.findAllPaged(
                pagedRequest.getPage(),
                pagedRequest.getPageSize(),
                pagedRequest.isSort(),
                pagedRequest.getSortBy(),
                pagedRequest.isSortAscending()));
    }

}
