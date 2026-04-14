package com.futurekawa.backend.model.response;

import com.futurekawa.backend.model.dto.EntrepotDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntrepotResponse {
    private List<EntrepotDto> entrepots;
    private int total;
    private String codePays;
}
