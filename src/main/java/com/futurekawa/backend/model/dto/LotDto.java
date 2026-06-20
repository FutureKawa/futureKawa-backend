package com.futurekawa.backend.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.futurekawa.backend.enums.CafeType;
import com.futurekawa.backend.enums.LotStatut;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LotDto {
    private Long id;
    private Integer entrepotId;
    private String entrepotNom;
    private CafeType typeCafe;
    private LocalDate dateStockage;
    private LotStatut statut;
    private Double poidsKg;
    private Integer joursRestants;
    private LocalDateTime dateMaj;
}