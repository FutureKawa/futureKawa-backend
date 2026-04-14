package com.futurekawa.backend.model.response;

import com.futurekawa.backend.model.dto.MesureDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MesureResponse {
    private List<MesureDto> mesures;
    private int total;
    private Long lotId;
}
