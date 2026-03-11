package com.sentura.country_service.service;

import com.sentura.country_service.dto.CountryDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
        // Fetching from external API
        String url = "https://restcountries.com/v3.1/all";
        Object[] rawData = restTemplate.getForObject(url, Object[].class);

        // Logic to map rawData to List<CountryDTO>...
        this.lastFetchTime = LocalDateTime.now();
    }

    public List<CountryDTO> search(String query) {
        // Search logic filters results from the cached list.
        return getAllCountries().stream()
                .filter(c -> c.getName().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }
}
