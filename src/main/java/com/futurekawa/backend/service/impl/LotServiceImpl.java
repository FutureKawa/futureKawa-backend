package com.futurekawa.backend.service.impl;

import com.futurekawa.backend.enums.CafeType;
import com.futurekawa.backend.enums.LotStatut;
import com.futurekawa.backend.model.dto.LotDto;
import com.futurekawa.backend.model.dto.LotPageFromBrazil;
import com.futurekawa.backend.model.response.LotResponse;
import com.futurekawa.backend.service.LotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
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
            LotDto.builder().id(100L)
                .entrepotId(1).entrepotNom("Quito Central").typeCafe(CafeType.ARABICA)
                .poidsKg(850.0).dateStockage(LocalDate.of(2024, 1, 15)).statut(LotStatut.CONFORME).build(),
            LotDto.builder().id(101L)
                .entrepotId(1).entrepotNom("Quito Central").typeCafe(CafeType.ROBUSTA)
                .poidsKg(1200.0).dateStockage(LocalDate.of(2024, 3, 20)).statut(LotStatut.PERIME).build(),
            LotDto.builder().id(102L)
                .entrepotId(1).entrepotNom("Quito Central").typeCafe(CafeType.PREMIUM)
                .poidsKg(600.0).dateStockage(LocalDate.of(2023, 6, 10)).statut(LotStatut.PERIME).build(),
            LotDto.builder().id(103L)
                .entrepotId(2).entrepotNom("Guayaquil Sud").typeCafe(CafeType.ARABICA)
                .poidsKg(950.0).dateStockage(LocalDate.of(2024, 5, 10)).statut(LotStatut.CONFORME).build(),
            LotDto.builder().id(104L)
                .entrepotId(2).entrepotNom("Guayaquil Sud").typeCafe(CafeType.ROBUSTA)
                .poidsKg(750.0).dateStockage(LocalDate.of(2024, 8, 15)).statut(LotStatut.CONFORME).build(),
            LotDto.builder().id(105L)
                .entrepotId(2).entrepotNom("Guayaquil Sud").typeCafe(CafeType.PREMIUM)
                .poidsKg(500.0).dateStockage(LocalDate.of(2023, 9, 20)).statut(LotStatut.PERIME).build()
        );

        LOTS_CO = List.of(
            LotDto.builder().id(200L)
                .entrepotId(1).entrepotNom("Bogotá Est").typeCafe(CafeType.ARABICA)
                .poidsKg(1100.0).dateStockage(LocalDate.of(2024, 2, 10)).statut(LotStatut.CONFORME).build(),
            LotDto.builder().id(201L)
                .entrepotId(1).entrepotNom("Bogotá Est").typeCafe(CafeType.PREMIUM)
                .poidsKg(800.0).dateStockage(LocalDate.of(2024, 4, 25)).statut(LotStatut.A_EXPEDIER).build(),
            LotDto.builder().id(202L)
                .entrepotId(1).entrepotNom("Bogotá Est").typeCafe(CafeType.ROBUSTA)
                .poidsKg(650.0).dateStockage(LocalDate.of(2023, 7, 14)).statut(LotStatut.PERIME).build(),
            LotDto.builder().id(203L)
                .entrepotId(2).entrepotNom("Medellín Nord").typeCafe(CafeType.ROBUSTA)
                .poidsKg(900.0).dateStockage(LocalDate.of(2024, 6, 1)).statut(LotStatut.CONFORME).build(),
            LotDto.builder().id(204L)
                .entrepotId(2).entrepotNom("Medellín Nord").typeCafe(CafeType.ARABICA)
                .poidsKg(1050.0).dateStockage(LocalDate.of(2024, 9, 10)).statut(LotStatut.CONFORME).build(),
            LotDto.builder().id(205L)
                .entrepotId(2).entrepotNom("Medellín Nord").typeCafe(CafeType.PREMIUM)
                .poidsKg(700.0).dateStockage(LocalDate.of(2023, 10, 5)).statut(LotStatut.PERIME).build()
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
    public LotDto getLotByFunctionalId(String codePays, Long lotId) {
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
            return LOTS_EC.stream().filter(l -> l.getId().equals(lotId)).findFirst().orElse(null);
        } else if (codePays.equalsIgnoreCase("CO")) {
            return LOTS_CO.stream().filter(l -> l.getId().equals(lotId)).findFirst().orElse(null);
        } else {
            throw new IllegalArgumentException("Pays inconnu : " + codePays);
        }
    }

    @Override
    public List<LotDto> getLotsByEntrepot(String codePays, Integer entrepotId) {
        if (codePays.equalsIgnoreCase("BR")) {
            try {
                LotDto[] result = restTemplate.getForObject(
                        brasilUrl + "/api/lots/fifo/" + entrepotId, LotDto[].class);
                final List<LotDto> lots = result != null ? Arrays.asList(result) : Collections.emptyList();
                return LotResponse.from(lots, codePays).getLots();
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
    public LotPageFromBrazil getLotsByEntrepotPaged(String codePays, Integer entrepotId, int page, int size) {
        if (codePays.equalsIgnoreCase("BR")) {
            try {
                String url = brasilUrl
                        + "/api/lots/entrepot/" + entrepotId
                        + "?page=" + page
                        + "&size=" + size;

                ResponseEntity<LotPageFromBrazil> response = restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<LotPageFromBrazil>() {}
                );

                return response.getBody() != null ? response.getBody() : emptyPage();
            } catch (Exception e) {
                log.error("Backend Brésil indisponible pour getLotsByEntrepotPaged(entrepotId={}, page={}, size={}) : {}",
                        entrepotId, page, size, e.getMessage());
                return emptyPage();
            }
        }
        throw new IllegalArgumentException("Pagination par entrepôt non supportée pour le pays : " + codePays);
    }

    private LotPageFromBrazil emptyPage() {
        LotPageFromBrazil p = new LotPageFromBrazil();
        p.setContent(List.of());
        p.setNumber(0);
        p.setSize(0);
        p.setTotalElements(0);
        p.setTotalPages(0);
        p.setFirst(true);
        p.setLast(true);
        return p;
    }
}
