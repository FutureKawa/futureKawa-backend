package com.futurekawa.backend.service.impl;

import com.futurekawa.backend.model.dto.LotDto;
import com.futurekawa.backend.service.LotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LotServiceImpl implements LotService {

    private final RestTemplate restTemplate;
    private final String brasilUrl;

    private static final List<LotDto> LOTS_EC;
    private static final List<LotDto> LOTS_CO;

    static {
        LOTS_EC = List.of(
            LotDto.builder().id(100L).lotId("LOT-ECU-2024-001").codePays("EC")
                .entrepotId(10L).nomEntrepot("Quito Central").typeCafe("ARABICA")
                .poids(850.0).dateStockage(LocalDateTime.of(2024, 1, 15, 8, 0)).statut("CONFORME").build(),
            LotDto.builder().id(101L).lotId("LOT-ECU-2024-002").codePays("EC")
                .entrepotId(10L).nomEntrepot("Quito Central").typeCafe("ROBUSTA")
                .poids(1200.0).dateStockage(LocalDateTime.of(2024, 3, 20, 8, 0)).statut("EN_ALERTE").build(),
            LotDto.builder().id(102L).lotId("LOT-ECU-2023-001").codePays("EC")
                .entrepotId(10L).nomEntrepot("Quito Central").typeCafe("PREMIUM")
                .poids(600.0).dateStockage(LocalDateTime.of(2023, 6, 10, 8, 0)).statut("PERIME").build(),
            LotDto.builder().id(103L).lotId("LOT-ECU-2024-003").codePays("EC")
                .entrepotId(11L).nomEntrepot("Guayaquil Sud").typeCafe("ARABICA")
                .poids(950.0).dateStockage(LocalDateTime.of(2024, 5, 1, 8, 0)).statut("CONFORME").build(),
            LotDto.builder().id(104L).lotId("LOT-ECU-2024-004").codePays("EC")
                .entrepotId(11L).nomEntrepot("Guayaquil Sud").typeCafe("ROBUSTA")
                .poids(750.0).dateStockage(LocalDateTime.of(2024, 8, 15, 8, 0)).statut("CONFORME").build(),
            LotDto.builder().id(105L).lotId("LOT-ECU-2023-002").codePays("EC")
                .entrepotId(11L).nomEntrepot("Guayaquil Sud").typeCafe("PREMIUM")
                .poids(500.0).dateStockage(LocalDateTime.of(2023, 9, 20, 8, 0)).statut("PERIME").build()
        );

        LOTS_CO = List.of(
            LotDto.builder().id(200L).lotId("LOT-COL-2024-001").codePays("CO")
                .entrepotId(20L).nomEntrepot("Bogotá Est").typeCafe("ARABICA")
                .poids(1100.0).dateStockage(LocalDateTime.of(2024, 2, 10, 8, 0)).statut("CONFORME").build(),
            LotDto.builder().id(201L).lotId("LOT-COL-2024-002").codePays("CO")
                .entrepotId(20L).nomEntrepot("Bogotá Est").typeCafe("PREMIUM")
                .poids(800.0).dateStockage(LocalDateTime.of(2024, 4, 25, 8, 0)).statut("EN_ALERTE").build(),
            LotDto.builder().id(202L).lotId("LOT-COL-2023-001").codePays("CO")
                .entrepotId(20L).nomEntrepot("Bogotá Est").typeCafe("ROBUSTA")
                .poids(650.0).dateStockage(LocalDateTime.of(2023, 7, 14, 8, 0)).statut("PERIME").build(),
            LotDto.builder().id(203L).lotId("LOT-COL-2024-003").codePays("CO")
                .entrepotId(21L).nomEntrepot("Medellín Nord").typeCafe("ROBUSTA")
                .poids(900.0).dateStockage(LocalDateTime.of(2024, 6, 1, 8, 0)).statut("CONFORME").build(),
            LotDto.builder().id(204L).lotId("LOT-COL-2024-004").codePays("CO")
                .entrepotId(21L).nomEntrepot("Medellín Nord").typeCafe("ARABICA")
                .poids(1050.0).dateStockage(LocalDateTime.of(2024, 9, 10, 8, 0)).statut("CONFORME").build(),
            LotDto.builder().id(205L).lotId("LOT-COL-2023-002").codePays("CO")
                .entrepotId(21L).nomEntrepot("Medellín Nord").typeCafe("PREMIUM")
                .poids(700.0).dateStockage(LocalDateTime.of(2023, 10, 5, 8, 0)).statut("PERIME").build()
        );
    }

    public LotServiceImpl(RestTemplate restTemplate,
                          @Value("${backend.brasil.url}") String brasilUrl) {
        this.restTemplate = restTemplate;
        this.brasilUrl = brasilUrl;
    }

    @Override
    public List<LotDto> getAllLots(String codePays) {
        if (codePays.equalsIgnoreCase("BR")) {
            try {
                LotDto[] result = restTemplate.getForObject(brasilUrl + "/api/lots", LotDto[].class);
                return result != null ? Arrays.asList(result) : Collections.emptyList();
            } catch (Exception e) {
                log.error("Backend Brésil indisponible pour getAllLots : {}", e.getMessage());
                return Collections.emptyList();
            }
        } else if (codePays.equalsIgnoreCase("EC")) {
            return LOTS_EC;
        } else if (codePays.equalsIgnoreCase("CO")) {
            return LOTS_CO;
        } else {
            throw new IllegalArgumentException("Pays inconnu : " + codePays);
        }
    }

    @Override
    public LotDto getLotByFunctionalId(String codePays, String lotId) {
        if (codePays.equalsIgnoreCase("BR")) {
            try {
                String url = UriComponentsBuilder.fromUriString(brasilUrl + "/api/lots/search")
                        .queryParam("lotId", lotId)
                        .toUriString();
                return restTemplate.getForObject(url, LotDto.class);
            } catch (Exception e) {
                log.error("Backend Brésil indisponible pour getLotByFunctionalId({}) : {}", lotId, e.getMessage());
                return null;
            }
        } else if (codePays.equalsIgnoreCase("EC")) {
            return LOTS_EC.stream().filter(l -> l.getLotId().equals(lotId)).findFirst().orElse(null);
        } else if (codePays.equalsIgnoreCase("CO")) {
            return LOTS_CO.stream().filter(l -> l.getLotId().equals(lotId)).findFirst().orElse(null);
        } else {
            throw new IllegalArgumentException("Pays inconnu : " + codePays);
        }
    }

    @Override
    public List<LotDto> getLotsByEntrepot(String codePays, Long entrepotId) {
        if (codePays.equalsIgnoreCase("BR")) {
            try {
                LotDto[] result = restTemplate.getForObject(
                        brasilUrl + "/api/lots/entrepot/" + entrepotId, LotDto[].class);
                return result != null ? Arrays.asList(result) : Collections.emptyList();
            } catch (Exception e) {
                log.error("Backend Brésil indisponible pour getLotsByEntrepot({}) : {}", entrepotId, e.getMessage());
                return Collections.emptyList();
            }
        } else if (codePays.equalsIgnoreCase("EC")) {
            return LOTS_EC.stream()
                    .filter(l -> l.getEntrepotId().equals(entrepotId))
                    .collect(Collectors.toList());
        } else if (codePays.equalsIgnoreCase("CO")) {
            return LOTS_CO.stream()
                    .filter(l -> l.getEntrepotId().equals(entrepotId))
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("Pays inconnu : " + codePays);
        }
    }

    @Override
    public List<LotDto> getAllLotsAllPays() {
        List<LotDto> all = new ArrayList<>();
        all.addAll(getAllLots("BR"));
        all.addAll(getAllLots("EC"));
        all.addAll(getAllLots("CO"));
        return all;
    }
}
