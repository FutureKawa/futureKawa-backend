package com.futurekawa.backend.service;

import com.futurekawa.backend.model.dto.EntrepotDto;

import java.util.List;

public interface EntrepotService {
    List<EntrepotDto> getAllEntrepots(String codePays);
    EntrepotDto getEntrepotById(String codePays, Long id);
    List<EntrepotDto> getAllEntrepotsAllPays();
}
