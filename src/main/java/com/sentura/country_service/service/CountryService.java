package com.sentura.country_service.service;

import com.sentura.country_service.dto.CountryDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CountryService {
    private final RestTemplate restTemplate = new RestTemplate();
    private List<CountryDTO> cachedCountries = new ArrayList<>();
    private LocalDateTime lastFetchTime;

    public List<CountryDTO> getAllCountries() {
        if (cachedCountries.isEmpty() || lastFetchTime == null ||
                lastFetchTime.isBefore(LocalDateTime.now().minusMinutes(10))) {
            refreshCache();
        }
        return cachedCountries;
    }

    private void refreshCache() {

        String url = "https://restcountries.com/v3.1/all?fields=name,capital,region,population,flags";

        try {
            // Using List.class to handle the array response from RestCountries
            List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);

            if (response != null) {
                this.cachedCountries = response.stream().map(data -> {
                    // Extracting nested fields from the API JSON structure
                    Map<String, Object> nameMap = (Map<String, Object>) data.get("name");
                    List<String> capitals = (List<String>) data.get("capital");
                    Map<String, Object> flagsMap = (Map<String, Object>) data.get("flags");

                    return new CountryDTO(
                            (String) nameMap.get("common"),
                            (capitals != null && !capitals.isEmpty()) ? capitals.get(0) : "N/A",
                            (String) data.get("region"),
                            data.get("population") != null ? Long.valueOf(data.get("population").toString()) : 0L,
                            (String) flagsMap.get("png")
                    );
                }).collect(Collectors.toList());

                this.lastFetchTime = LocalDateTime.now();
                System.out.println("Cache refreshed at: " + lastFetchTime);
            }
        } catch (Exception e) {
            // Logging the error for visibility during the 40-minute window
            System.err.println("Failed to fetch from RestCountries: " + e.getMessage());
        }
    }

    public List<CountryDTO> search(String query) {
        //  Filters from the local cache rather than re-fetching .
        return getAllCountries().stream()
                .filter(c -> c.getName().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }
}