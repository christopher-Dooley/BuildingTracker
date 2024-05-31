package com.example.BuildingTracker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.ratelimiter.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Service
public class CoordinateRequestService {

    private final  HttpClient client;
    private final String apiUrl;
    private final String apiKey;
    private final RateLimiter rateLimiter;;


    @Autowired
    public CoordinateRequestService(
            HttpClient client,
            @Value("${location.api.url}") String apiUrl,
            @Value("${location.api.key}") String apiKey,
            RateLimiter rateLimiter) {
        this.client = client;
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
        this.rateLimiter = rateLimiter;
    }

    // api rate limited on free tier, no point going faster
    public void limitedSetCoordinatesForBuilding(BuildingDTO building) {
        RateLimiter.decorateConsumer(rateLimiter, (buildingDto) -> setCoordinatesForBuilding(building)).accept(building);
    }

    private void setCoordinatesForBuilding(BuildingDTO building) {
        String address = getAddress(building);
        String encodedAddress = encodeAddress(address);
        URI uri = createUri(encodedAddress);
        HttpRequest request = createApiRequest(uri);

        try {
            HttpResponse<String> response = requestCoordinates(request);
            Map<String, Double> coordinates = getCoordinatesFromResponse(response);
            building.setLongitude(coordinates.get("longitude"));
            building.setLatitude(coordinates.get("latitude"));
        } catch (Exception e) {
            throw new CoordinateRequestException("Requesting coordinates failed, please retry");
        }
    }

    private HttpResponse<String> requestCoordinates(HttpRequest request) throws IOException, InterruptedException {
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    // don't use the name, number and street gets better results
    private String getAddress(BuildingDTO building) {
        return building.getNumber() + " " +
                building.getStreet() + ", " +
                building.getCity() + ", " +
                building.getPostCode() + ", " +
                building.getCountry();
    }

    private String encodeAddress(String address) {
        return UriUtils.encode(address, StandardCharsets.UTF_8);
    }

    private URI createUri(String encodedAddress) {
        String uri = apiUrl + encodedAddress + "&format=json&apiKey=" + apiKey;
        return URI.create(uri);
    }

    private HttpRequest createApiRequest(URI uri) {
        return HttpRequest.newBuilder()
                .GET()
                .header("Content-Type", "application/json")
                .uri(uri)
                .build();

    }

    private Map<String, Double> getCoordinatesFromResponse(HttpResponse<String> response) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode buildingDetails = mapper.readTree(response.body()).path("results").path(0);

        Map<String, Double> coordinates = new HashMap<>();
        coordinates.put("longitude", buildingDetails.get("lon").asDouble());
        coordinates.put("latitude", buildingDetails.get("lat").asDouble());

        return coordinates;
    }





}
