package com.futurekawa.backend.controller;

import com.futurekawa.backend.model.dto.LotDto;
import com.futurekawa.backend.model.dto.LotPageFromBrazil;
import com.futurekawa.backend.model.response.LotResponse;
import com.futurekawa.backend.service.LotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LotController {

    private final LotService lotService;

    public LotController(LotService lotService) {
        this.lotService = lotService;
    }

    @GetMapping("/{codePays}/lots")
    public ResponseEntity<LotResponse> getAllLots(@PathVariable String codePays) {
        List<LotDto> lots = lotService.getAllLots(codePays);
        return ResponseEntity.ok(LotResponse.builder()
                .lots(lots)
                .total(lots.size())
                .codePays(codePays.toUpperCase())
                .build());
    }

    @GetMapping("/{codePays}/lots/paged")
    public ResponseEntity<LotResponse> getLotsPaged(
            @PathVariable String codePays,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        LotPageFromBrazil pageResult = lotService.getLotsPaged(codePays, page, size);

        return ResponseEntity.ok(
                LotResponse.builder()
                        .codePays(codePays.toUpperCase())
                        .lots(pageResult.getContent())
                        .total((int) pageResult.getTotalElements()) // total global
                        .build()
        );
    }

    @GetMapping("/{codePays}/lots/search")
    public ResponseEntity<LotDto> getLotByFunctionalId(@PathVariable String codePays,
                                                        @RequestParam Long lotId) {
        return ResponseEntity.ok(lotService.getLotByFunctionalId(codePays, lotId));
    }

    @GetMapping("/{codePays}/lots/entrepot/{entrepotId}")
    public ResponseEntity<LotResponse> getLotsByEntrepot(@PathVariable String codePays,
                                                          @PathVariable Integer entrepotId) {
        List<LotDto> lots = lotService.getLotsByEntrepot(codePays, entrepotId);
        return ResponseEntity.ok(LotResponse.builder()
                .lots(lots)
                .total(lots.size())
                .codePays(codePays.toUpperCase())
                .build());
    }
}
