package com.futurekawa.backend.model.response;

import com.futurekawa.backend.model.dto.LotDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LotResponse {
    private List<LotDto> lots;
    private int total;
    private String codePays;

    public static LotResponse from(List<LotDto> lots, String codePays) {
        return LotResponse.builder()
                .lots(lots)
                .total(lots.size())
                .codePays(codePays)
                .build();
    }
}
