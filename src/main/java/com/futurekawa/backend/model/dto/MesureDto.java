package com.futurekawa.backend.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MesureDto {
    private Long id;
    private Long espId;
    private Integer entrepotId;
    private BigDecimal temperature;
    private BigDecimal humidity;
    private LocalDateTime timestamp;
    private Boolean conforme;
}
