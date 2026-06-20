package com.futurekawa.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationDto {
    private Long totalEntrepots;
    private Long alertesNonTraitees;
    private String codePays;
    private String nomPays;
    private BigDecimal temperatureIdeale;
    private BigDecimal temperatureTolerance;
    private BigDecimal humiditeIdeale;
    private BigDecimal humiditeTolerance;
    private int dureeConservation;
}
