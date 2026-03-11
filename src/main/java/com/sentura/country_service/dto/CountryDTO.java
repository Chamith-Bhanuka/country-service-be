package com.sentura.country_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryDTO {
    private String name;
    private String capital;
    private String region;
    private Long population;
    private String flag;
}
