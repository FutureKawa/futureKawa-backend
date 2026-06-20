package com.futurekawa.backend.service;

import com.futurekawa.backend.model.dto.EntrepotDto;
import com.futurekawa.backend.model.response.EntrepotONEResponse;

import java.util.List;

public interface EntrepotService {
    List<EntrepotDto> getAllEntrepots(String codePays);
    EntrepotONEResponse getEntrepotById(String codePays, Long id);
    List<EntrepotDto> getAllEntrepotsAllPays();
}
