package com.example.BuildingTracker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class CoordinateRequestService {

    private HttpClient client;

    private String apiUrl;

    private String apiKey;

    private String apiParam;

    @Autowired
    public CoordinateRequestService(
            HttpClient client,
            @Value("${location-api.url}") String apiUrl,
            @Value("${location-api.api-key}")String apiKey,
            @Value("${location-api.api-param}")String apiParam) {
        this.client = client;
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
        this.apiParam = apiParam;
    }

    public void setCoordinatesForBuilding(BuildingDTO building) throws CoordinateRequestException {
        String address = getAddress(building);
        String encodedAddress = encodeAddress(address);
        URI uri = createUri(encodedAddress);
        HttpRequest request = createApiRequest(uri);

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Map<String, Double> coordinates = getCoordinatesFromResponse(response);
            building.setLongitude(coordinates.get("longitude"));
            building.setLatitude(coordinates.get("latitude"));
        } catch (Exception e) {
            throw new CoordinateRequestException("Requesting coordinates failed, please retry");
        }
    }

    public String getAddress(BuildingDTO building) {
        return building.getName() + ", " +
                building.getNumber() + ", " +
                building.getStreet() + ", " +
                building.getCity() + ", " +
                building.getPostCode() + ", " +
                building.getCountry();
    }

    public String encodeAddress(String address) {
        return URLEncoder.encode(address, StandardCharsets.UTF_8);
    }

    public URI createUri(String encodedAddress) {
        String uri = apiUrl + encodedAddress + apiParam + apiKey;
        return URI.create(uri);
    }

    public HttpRequest createApiRequest(URI uri) {
        return HttpRequest.newBuilder()
                .GET()
                .header("Content-Type", "application/json")
                .uri(uri)
                .build();

    }

    public Map<String, Double> getCoordinatesFromResponse(HttpResponse<String> response) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode buildingDetails = mapper.readTree(response.body()).path("results").path("0");

        Map<String, Double> coordinates = new HashMap<>();
        coordinates.put("longitude", buildingDetails.get("lon").asDouble());
        coordinates.put("latitude", buildingDetails.get("lat").asDouble());

        return coordinates;
    }





}
