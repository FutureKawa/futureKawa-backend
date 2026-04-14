package com.futurekawa.backend.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationResponse {
    private String codePays;
    private String pays;
    private Double tempIdeal;
    private Double humiditeIdeal;
    private Double tempMin;
    private Double tempMax;
    private Double humiditeMin;
    private Double humiditeMax;
}
