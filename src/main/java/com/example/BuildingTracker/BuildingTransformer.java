package com.example.BuildingTracker;

public class BuildingTransformer {

    public static BuildingEntity toEntity(BuildingDTO dto) {
        return new BuildingEntity(
                dto.getName(),
                dto.getStreet(),
                dto.getNumber(),
                dto.getPostCode(),
                dto.getCity(),
                dto.getCountry(),
                dto.getDescription(),
                dto.getLongitude(),
                dto.getLatitude());
    }

    public static BuildingDTO toDto(BuildingEntity entity) {
        return new BuildingDTO(
                entity.getName(),
                entity.getStreet(),
                entity.getNumber(),
                entity.getPostCode(),
                entity.getCity(),
                entity.getCountry(),
                entity.getDescription(),
                entity.getLongitude(),
                entity.getLatitude());
    }


}
