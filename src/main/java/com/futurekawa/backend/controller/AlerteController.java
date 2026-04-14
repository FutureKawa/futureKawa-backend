package com.futurekawa.backend.controller;

import com.futurekawa.backend.model.dto.AlerteDto;
import com.futurekawa.backend.model.response.AlerteResponse;
import com.futurekawa.backend.service.AlerteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AlerteController {

    private final AlerteService alerteService;

    public AlerteController(AlerteService alerteService) {
        this.alerteService = alerteService;
    }

    @GetMapping("/{codePays}/alertes")
    public ResponseEntity<AlerteResponse> getAllAlertes(@PathVariable String codePays) {
        List<AlerteDto> alertes = alerteService.getAllAlertes(codePays);
        return ResponseEntity.ok(AlerteResponse.builder()
                .alertes(alertes)
                .total(alertes.size())
                .codePays(codePays.toUpperCase())
                .build());
    }

    @GetMapping("/{codePays}/alertes/lot/{lotId}")
    public ResponseEntity<AlerteResponse> getAlertesByLot(@PathVariable String codePays,
                                                           @PathVariable Long lotId) {
        List<AlerteDto> alertes = alerteService.getAlertesByLot(codePays, lotId);
        return ResponseEntity.ok(AlerteResponse.builder()
                .alertes(alertes)
                .total(alertes.size())
                .codePays(codePays.toUpperCase())
                .build());
    }

    @GetMapping("/alertes/all")
    public ResponseEntity<List<AlerteDto>> getAllAlertesAllPays() {
        return ResponseEntity.ok(alerteService.getAllAlertesAllPays());
    }
}
