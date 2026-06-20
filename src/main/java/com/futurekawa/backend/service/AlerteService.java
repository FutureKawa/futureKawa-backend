package com.futurekawa.backend.service;

import com.futurekawa.backend.model.dto.AlerteDto;

import java.util.List;

public interface AlerteService {
    List<AlerteDto> getAllAlertes(String codePays);
    List<AlerteDto> getAlertesByLot(String codePays, Long lotId);
    List<AlerteDto> getAllAlertesAllPays();

    List<AlerteDto> getAlertesByEntrepot(String codePays, Integer entrepotId);
}
