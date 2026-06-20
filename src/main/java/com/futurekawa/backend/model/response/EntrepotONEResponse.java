package com.futurekawa.backend.model.response;

import com.futurekawa.backend.model.dto.EntrepotDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntrepotONEResponse {
    private Long id;
    private String nom;
    private String adresse;
    private String responsable;
    private String emailResponsable;
    private Double latitude;
    private Double longitude;
    private Integer nombreLots;
    private BigDecimal stockTotal;
    private BigDecimal lastTemperature;
    private BigDecimal lastHumidity;

    public static EntrepotONEResponse from(EntrepotDto entrepot, BigDecimal stockTotal, int nbLots, BigDecimal temperature, BigDecimal humidity) {
        return EntrepotONEResponse.builder()
                .id(entrepot.getId())
                .nom(entrepot.getNom())
                .adresse(entrepot.getAdresse())
                .responsable(entrepot.getResponsable())
                .emailResponsable(entrepot.getEmailResponsable())
                .latitude(entrepot.getLatitude())
                .longitude(entrepot.getLongitude())
                .nombreLots(nbLots)
                .stockTotal(stockTotal)
                .lastTemperature(temperature)
                .lastHumidity(humidity)
                .build();
    }
}