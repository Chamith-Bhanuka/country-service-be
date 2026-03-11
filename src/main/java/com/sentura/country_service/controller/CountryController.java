package com.sentura.country_service.controller;

import com.sentura.country_service.dto.CountryDTO;
import com.sentura.country_service.service.CountryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/countries")
@CrossOrigin(origins = "http://localhost:5173")
public class CountryController {
    private final CountryService countryService;

    //Constructor injection is the industry standard
    // for dependency injection in Spring Boot.
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    public List<CountryDTO> getCountries() {
        // This triggers the 10-minute caching logic in the service.
        return countryService.getAllCountries();
    }

    @GetMapping("/search")
    public List<CountryDTO> search(@RequestParam String query) {
        return countryService.search(query);
    }
}
