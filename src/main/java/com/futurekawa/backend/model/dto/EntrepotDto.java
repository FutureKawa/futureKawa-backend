package com.futurekawa.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntrepotDto {
    private Long id;
    private String nom;
    private String adresse;
    private String codePays;
    private String responsable;
    private String emailResponsable;
    private Double latitude;
    private Double longitude;
    private Integer nombreLots;
}
