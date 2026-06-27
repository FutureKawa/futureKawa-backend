package com.futurekawa.backend.controller;

import com.futurekawa.backend.model.response.MesureResponse;
import com.futurekawa.backend.service.MesureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import com.futurekawa.backend.model.dto.MesureDto;

@RestController
@RequestMapping("/api")
public class MesureController {

    private final MesureService mesureService;

    public MesureController(MesureService mesureService) {
        this.mesureService = mesureService;
    }

    @GetMapping("/{codePays}/mesures/lot/{lotId}")
    public ResponseEntity<MesureResponse> getMesuresByLot(@PathVariable String codePays,
                                                           @PathVariable Long lotId) {
        List<MesureDto> mesures = mesureService.getMesuresByLot(codePays, lotId);
        return ResponseEntity.ok(MesureResponse.builder()
                .mesures(mesures)
                .total(mesures.size())
                .lotId(lotId)
                .build());
    }

    @GetMapping("/{codePays}/mesures/entrepot/{entrepotId}")
    public ResponseEntity<List<MesureDto>> getMesuresByEntrepot(@PathVariable String codePays,
                                                                @PathVariable Integer entrepotId) {
        return ResponseEntity.ok(mesureService.getMesuresByEntrepot(codePays, entrepotId));
    }
}
