package com.futurekawa.backend.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.futurekawa.backend.enums.AlerteType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlerteDto {
    private Integer id;
    private Integer entrepotId;
    private Long lotId;
    private AlerteType type;
    private LocalDateTime dateAlerte;
    private Boolean validation;
}
