package com.futurekawa.backend.model.response;

import com.futurekawa.backend.model.dto.LotDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LotResponse {
    private List<LotDto> lots;
    private int total;
    private String codePays;
}
