package com.futurekawa.backend.service.impl;

import com.futurekawa.backend.model.dto.AlerteDto;
import com.futurekawa.backend.service.AlerteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AlerteServiceImpl implements AlerteService {

    private final RestTemplate restTemplate;
    private final String brasilUrl;

    private static final List<AlerteDto> ALERTES_EC;
    private static final List<AlerteDto> ALERTES_CO;

    static {
        ALERTES_EC = List.of(
            AlerteDto.builder()
                .id(10L).lotId(101L).lotIdFonctionnel("LOT-ECU-2024-002").codePays("EC")
                .typeAlerte("IOT").traitee(false)
                .dateAlerte(LocalDateTime.of(2024, 11, 1, 10, 0))
                .message("Température hors plage : 35.2°C (seuil max : 34°C)")
                .build(),
            AlerteDto.builder()
                .id(11L).lotId(102L).lotIdFonctionnel("LOT-ECU-2023-001").codePays("EC")
                .typeAlerte("ANCIENNETE").traitee(false)
                .dateAlerte(LocalDateTime.of(2024, 11, 1, 8, 0))
                .message("Lot stocké depuis plus de 365 jours")
                .build()
        );

        ALERTES_CO = List.of(
            AlerteDto.builder()
                .id(20L).lotId(201L).lotIdFonctionnel("LOT-COL-2024-002").codePays("CO")
                .typeAlerte("IOT").traitee(false)
                .dateAlerte(LocalDateTime.of(2024, 11, 1, 10, 0))
                .message("Température hors plage : 22.1°C (seuil min : 23°C)")
                .build(),
            AlerteDto.builder()
                .id(21L).lotId(202L).lotIdFonctionnel("LOT-COL-2023-001").codePays("CO")
                .typeAlerte("ANCIENNETE").traitee(false)
                .dateAlerte(LocalDateTime.of(2024, 11, 1, 8, 0))
                .message("Lot stocké depuis plus de 365 jours")
                .build()
        );
    }

    public AlerteServiceImpl(RestTemplate restTemplate,
                             @Value("${backend.brasil.url}") String brasilUrl) {
        this.restTemplate = restTemplate;
        this.brasilUrl = brasilUrl;
    }

    @Override
    public List<AlerteDto> getAllAlertes(String codePays) {
        if (codePays.equalsIgnoreCase("BR")) {
            try {
                AlerteDto[] result = restTemplate.getForObject(
                        brasilUrl + "/api/alertes", AlerteDto[].class);
                return result != null ? Arrays.asList(result) : Collections.emptyList();
            } catch (Exception e) {
                log.error("Backend Brésil indisponible pour getAllAlertes : {}", e.getMessage());
                return Collections.emptyList();
            }
        } else if (codePays.equalsIgnoreCase("EC")) {
            return ALERTES_EC;
        } else if (codePays.equalsIgnoreCase("CO")) {
            return ALERTES_CO;
        } else {
            throw new IllegalArgumentException("Pays inconnu : " + codePays);
        }
    }

    @Override
    public List<AlerteDto> getAlertesByLot(String codePays, Long lotId) {
        if (codePays.equalsIgnoreCase("BR")) {
            try {
                AlerteDto[] result = restTemplate.getForObject(
                        brasilUrl + "/api/alertes/lot/" + lotId, AlerteDto[].class);
                return result != null ? Arrays.asList(result) : Collections.emptyList();
            } catch (Exception e) {
                log.error("Backend Brésil indisponible pour getAlertesByLot({}) : {}", lotId, e.getMessage());
                return Collections.emptyList();
            }
        } else if (codePays.equalsIgnoreCase("EC")) {
            return ALERTES_EC.stream()
                    .filter(a -> a.getLotId().equals(lotId))
                    .collect(Collectors.toList());
        } else if (codePays.equalsIgnoreCase("CO")) {
            return ALERTES_CO.stream()
                    .filter(a -> a.getLotId().equals(lotId))
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("Pays inconnu : " + codePays);
        }
    }

    @Override
    public List<AlerteDto> getAllAlertesAllPays() {
        List<AlerteDto> all = new ArrayList<>();
        all.addAll(getAllAlertes("BR"));
        all.addAll(getAllAlertes("EC"));
        all.addAll(getAllAlertes("CO"));
        return all;
    }
}
