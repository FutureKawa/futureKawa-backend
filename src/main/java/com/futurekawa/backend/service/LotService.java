package com.futurekawa.backend.service;

import com.futurekawa.backend.model.dto.LotDto;
import com.futurekawa.backend.model.dto.LotPageFromBrazil;

import java.util.List;

public interface LotService {
    List<LotDto> getAllLots(String codePays);
    LotDto getLotByFunctionalId(String codePays, Long lotId);
    List<LotDto> getLotsByEntrepot(String codePays, Integer entrepotId);

    LotPageFromBrazil getLotsByEntrepotPaged(String codePays, Integer entrepotId, int page, int size);
}
