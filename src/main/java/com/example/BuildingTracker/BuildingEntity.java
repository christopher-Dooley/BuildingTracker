package com.example.BuildingTracker;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;


import java.util.Objects;
import java.util.UUID;

@Entity
public class BuildingEntity {

    @Id
    private UUID uuid;
    private String name;
    private String street;
    private String number;
    private String postCode;
    private String city;
    private String country;
    private String description;
    private double longitude;
    private double latitude;

    protected BuildingEntity() {}

    public BuildingEntity(String name, String street, String number, String postCode, String city, String country, String description, float longitude, float latitude) {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.street = street;
        this.number = number;
        this.postCode = postCode;
        this.city = city;
        this.country = country;
        this.description = description;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BuildingEntity that = (BuildingEntity) o;
        return Double.compare(longitude, that.longitude) == 0 && Double.compare(latitude, that.latitude) == 0 && Objects.equals(uuid, that.uuid) && Objects.equals(name, that.name) && Objects.equals(street, that.street) && Objects.equals(number, that.number) && Objects.equals(postCode, that.postCode) && Objects.equals(city, that.city) && Objects.equals(country, that.country) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, name, street, number, postCode, city, country, description, longitude, latitude);
    }

    @Override
    public String toString() {
        return "BuildingEntity{" +
                "uuid=" + uuid +
                ", name='" + name + '\'' +
                ", street='" + street + '\'' +
                ", number='" + number + '\'' +
                ", postCode='" + postCode + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", description='" + description + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
