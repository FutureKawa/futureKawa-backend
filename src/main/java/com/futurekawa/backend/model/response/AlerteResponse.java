package com.futurekawa.backend.model.response;

import com.futurekawa.backend.model.dto.AlerteDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlerteResponse {
    private List<AlerteDto> alertes;
    private int total;
    private String codePays;
}
