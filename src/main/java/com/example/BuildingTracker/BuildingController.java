package com.example.BuildingTracker;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
public class BuildingController {

    private final BuildingService service;

    @Autowired
    public BuildingController(BuildingService service) {
        this.service = service;
    }


    @GetMapping("/building/{name}")
    public ResponseEntity<BuildingDTO> getBuildingByName(@PathVariable String name) {
        return service.findBuildingByName(name)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/building/{uuid}")
    public ResponseEntity<BuildingDTO> getBuildingByUuid(@PathVariable UUID uuid) {
        return service.findBuildingByUUID(uuid)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/building/save")
    public ResponseEntity<BuildingDTO> newBuilding(@RequestBody BuildingDTO buildingDTO, HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        return service.newBuilding(buildingDTO)
                .map((dto) -> {
                    URI location = getNewLocationUri(requestUri, dto.getUuid());
                    return ResponseEntity.created(location).body(dto);
                    })
                .orElseGet(() -> ResponseEntity.internalServerError().build());
    }

    private URI getNewLocationUri(String requestUri, UUID uuid) {
        return URI.create(
                requestUri.substring(0, requestUri.length() -4)
                .concat(uuid.toString())
        );
    }

}
