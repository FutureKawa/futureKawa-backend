package com.futurekawa.backend.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationResponse {
    private String codePays;
    private String pays;
    private Long totalEntrepots;
    private Long alertes;
    private Double tempIdeal;
    private Double humiditeIdeal;
    private Double tempMin;
    private Double tempMax;
    private Double humiditeMin;
    private Double humiditeMax;
    private int dureeConservation;
}