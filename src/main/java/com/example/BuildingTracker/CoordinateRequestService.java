package com.example.BuildingTracker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;

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

    private static final Logger log = LoggerFactory.getLogger(CoordinateRequestService.class);
    private final  HttpClient client;
    private final String apiUrl;
    private final String apiKey;


    @Autowired
    public CoordinateRequestService(
            HttpClient client,
            @Value("${location.api.url}") String apiUrl,
            @Value("${location.api.key}") String apiKey) {
        this.client = client;
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
    }

    public void setCoordinatesForBuilding(BuildingDTO building) throws CoordinateRequestException {
        String address = getAddress(building);
        String encodedAddress = encodeAddress(address);
        URI uri = createUri(encodedAddress);
        HttpRequest request = createApiRequest(uri);

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            log.warn(response.toString());
            log.warn(response.body());
            Map<String, Double> coordinates = getCoordinatesFromResponse(response);
            building.setLongitude(coordinates.get("longitude"));
            building.setLatitude(coordinates.get("latitude"));
        } catch (Exception e) {
            throw new CoordinateRequestException("Requesting coordinates failed, please retry");
        }
    }

    // don't use the name, number and street gets better results
    public String getAddress(BuildingDTO building) {
        return building.getNumber() + " " +
                building.getStreet() + ", " +
                building.getCity() + ", " +
                building.getPostCode() + ", " +
                building.getCountry();
    }

    public String encodeAddress(String address) {
        return UriUtils.encode(address, StandardCharsets.UTF_8);
    }

    public URI createUri(String encodedAddress) {
        log.warn(apiUrl);
        log.warn(encodedAddress);
        String uri = apiUrl + encodedAddress + "&format=json&apiKey=" + apiKey;
        log.warn(uri);
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
        JsonNode buildingDetails = mapper.readTree(response.body()).path("results").path(0);

        Map<String, Double> coordinates = new HashMap<>();
        coordinates.put("longitude", buildingDetails.get("lon").asDouble());
        coordinates.put("latitude", buildingDetails.get("lat").asDouble());

        return coordinates;
    }





}
