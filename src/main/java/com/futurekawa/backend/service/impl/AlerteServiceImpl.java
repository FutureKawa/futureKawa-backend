package com.futurekawa.backend.service.impl;

import com.futurekawa.backend.enums.AlerteType;
import com.futurekawa.backend.model.dto.AlerteDto;
import com.futurekawa.backend.service.AlerteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
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
                        .id(10)
                        .entrepotId(null)
                        .lotId(101L)
                        .type(AlerteType.LOT_PERIME)
                        .dateAlerte(LocalDateTime.of(2024, 11, 3, 8, 0))
                        .validation(false)
                        .build(),

                AlerteDto.builder()
                        .id(11)
                        .entrepotId(1)
                        .lotId(null)
                        .type(AlerteType.TEMPERATURE_ENTREPOT)
                        .dateAlerte(LocalDateTime.of(2024, 11, 1, 8, 0))
                        .validation(false)
                        .build()
        );

        ALERTES_CO = List.of(
                AlerteDto.builder()
                        .id(20)
                        .entrepotId(2)
                        .lotId(null)
                        .type(AlerteType.HUMIDITE_ENTREPOT)
                        .dateAlerte(LocalDateTime.of(2024, 11, 3, 8, 0))
                        .validation(false)
                        .build(),

                AlerteDto.builder()
                        .id(21)
                        .entrepotId(1)
                        .lotId(null)
                        .type(AlerteType.MESURE_ENTREPOT)
                        .dateAlerte(LocalDateTime.of(2024, 11, 1, 8, 0))
                        .validation(false)
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
                    .filter(a -> !ObjectUtils.isEmpty(a.getLotId()) && a.getLotId().equals(lotId))
                    .collect(Collectors.toList());
        } else if (codePays.equalsIgnoreCase("CO")) {
            return ALERTES_CO.stream()
                    .filter(a -> !ObjectUtils.isEmpty(a.getLotId()) && a.getLotId().equals(lotId))
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

    @Override
    public List<AlerteDto> getAlertesByEntrepot(String codePays, Integer entrepotId) {
        if (codePays.equalsIgnoreCase("BR")) {
            try {
                AlerteDto[] result = restTemplate.getForObject(
                        brasilUrl + "/api/alertes/entrepot/" + entrepotId, AlerteDto[].class);
                return result != null ? Arrays.asList(result) : Collections.emptyList();
            } catch (Exception e) {
                log.error("Backend Brésil indisponible pour getAlertesByLot({}) : {}", entrepotId, e.getMessage());
                return Collections.emptyList();
            }
        } else if (codePays.equalsIgnoreCase("EC")) {
            return ALERTES_EC.stream()
                    .filter(a -> !ObjectUtils.isEmpty(a.getEntrepotId()) && a.getEntrepotId().equals(entrepotId))
                    .collect(Collectors.toList());
        } else if (codePays.equalsIgnoreCase("CO")) {
            return ALERTES_CO.stream()
                    .filter(a -> !ObjectUtils.isEmpty(a.getEntrepotId()) && a.getEntrepotId().equals(entrepotId))
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("Pays inconnu : " + codePays);
        }
    }
}
