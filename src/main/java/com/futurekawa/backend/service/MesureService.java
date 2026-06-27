package com.futurekawa.backend.service;

import com.futurekawa.backend.model.dto.MesureDto;

import java.util.List;

public interface MesureService {
    List<MesureDto> getMesuresByLot(String codePays, Long lotId);

    List<MesureDto> getMesuresByEntrepot(String codePays, Integer entrepotId);
}
