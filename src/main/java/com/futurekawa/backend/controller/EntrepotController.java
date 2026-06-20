package com.futurekawa.backend.controller;

import com.futurekawa.backend.model.dto.EntrepotDto;
import com.futurekawa.backend.model.response.EntrepotONEResponse;
import com.futurekawa.backend.model.response.EntrepotResponse;
import com.futurekawa.backend.service.EntrepotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EntrepotController {

    private final EntrepotService entrepotService;

    public EntrepotController(EntrepotService entrepotService) {
        this.entrepotService = entrepotService;
    }

    @GetMapping("/{codePays}/entrepots")
    public ResponseEntity<EntrepotResponse> getAllEntrepots(@PathVariable String codePays) {
        List<EntrepotDto> entrepots = entrepotService.getAllEntrepots(codePays);
        return ResponseEntity.ok(EntrepotResponse.builder()
                .entrepots(entrepots)
                .total(entrepots.size())
                .codePays(codePays.toUpperCase())
                .build());
    }

    @GetMapping("/{codePays}/entrepots/{id}")
    public ResponseEntity<EntrepotONEResponse> getEntrepotById(@PathVariable String codePays,
                                                               @PathVariable Long id) {
        return ResponseEntity.ok(entrepotService.getEntrepotById(codePays, id));
    }

    @GetMapping("/entrepots/all")
    public ResponseEntity<List<EntrepotDto>> getAllEntrepotsAllPays() {
        return ResponseEntity.ok(entrepotService.getAllEntrepotsAllPays());
    }
}
