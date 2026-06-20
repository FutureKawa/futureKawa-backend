package com.futurekawa.backend.service.impl;

import com.futurekawa.backend.model.dto.EntrepotDto;
import com.futurekawa.backend.model.response.EntrepotONEResponse;
import com.futurekawa.backend.service.EntrepotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class EntrepotServiceImpl implements EntrepotService {

    private final RestTemplate restTemplate;
    private final String brasilUrl;

    private static final List<EntrepotDto> ENTREPOTS_EC = List.of(
        EntrepotDto.builder()
            .id(1L).nom("Quito Central").adresse("Av. Amazonas 1200, Quito")
                .responsable("Carlos Mendez")
            .emailResponsable("c.mendez@futurekawa.ec")
            .latitude(-0.1807).longitude(-78.4678)
            .build(),
        EntrepotDto.builder()
            .id(2L).nom("Guayaquil Sud").adresse("Blvd. 9 de Octubre 800, Guayaquil")
            .responsable("Ana Torres")
            .emailResponsable("a.torres@futurekawa.ec")
            .latitude(-2.1894).longitude(-79.8891)
            .build()
    );

    private static final List<EntrepotDto> ENTREPOTS_CO = List.of(
        EntrepotDto.builder()
            .id(1L).nom("Bogotá Est").adresse("Cra. 7 #45-12, Bogotá")
            .responsable("Miguel Reyes")
            .emailResponsable("m.reyes@futurekawa.co")
            .latitude(4.7110).longitude(-74.0721)
            .build(),
        EntrepotDto.builder()
            .id(2L).nom("Medellín Nord").adresse("Calle 50 #40-30, Medellín")
            .responsable("Laura Gomez")
            .emailResponsable("l.gomez@futurekawa.co")
            .latitude(6.2518).longitude(-75.5636)
            .build()
    );

    public EntrepotServiceImpl(RestTemplate restTemplate,
                               @Value("${backend.brasil.url}") String brasilUrl) {
        this.restTemplate = restTemplate;
        this.brasilUrl = brasilUrl;
    }

    @Override
    public List<EntrepotDto> getAllEntrepots(String codePays) {
        if (codePays.equalsIgnoreCase("BR")) {
            try {
                EntrepotDto[] result = restTemplate.getForObject(
                        brasilUrl + "/api/entrepots", EntrepotDto[].class);
                final List<EntrepotDto> entrepots = result != null ? Arrays.asList(result) : Collections.emptyList();
                return entrepots.stream()
                        .map(e -> EntrepotDto.from(e, codePays))
                        .toList();
            } catch (Exception e) {
                log.error("Backend Brésil indisponible pour getAllEntrepots : {}", e.getMessage());
                return Collections.emptyList();
            }
        } else if (codePays.equalsIgnoreCase("EC")) {
            return ENTREPOTS_EC.stream()
                    .map(e -> EntrepotDto.from(e, codePays))
                    .toList();
        } else if (codePays.equalsIgnoreCase("CO")) {
            return ENTREPOTS_CO.stream()
                    .map(e -> EntrepotDto.from(e, codePays))
                    .toList();
        } else {
            throw new IllegalArgumentException("Pays inconnu : " + codePays);
        }
    }

    @Override
    public EntrepotONEResponse getEntrepotById(String codePays, Long id) {
        if (codePays.equalsIgnoreCase("BR")) {
            try {
                return restTemplate.getForObject(
                        brasilUrl + "/api/entrepots/" + id, EntrepotONEResponse.class);
            } catch (Exception e) {
                log.error("Backend Brésil indisponible pour getEntrepotById({}) : {}", id, e.getMessage());
                return null;
            }
        } else if (codePays.equalsIgnoreCase("EC")) {
            final EntrepotDto entrepot = ENTREPOTS_EC.stream()
                    .filter(e -> e.getId().equals(id))
                    .findFirst()
                    .orElse(null);
            return EntrepotONEResponse.from(entrepot, BigDecimal.valueOf(1500.0), 13, BigDecimal.valueOf(31), BigDecimal.valueOf(60));
        } else if (codePays.equalsIgnoreCase("CO")) {
            final EntrepotDto entrepot = ENTREPOTS_CO.stream()
                    .filter(e -> e.getId().equals(id))
                    .findFirst()
                    .orElse(null);
            return EntrepotONEResponse.from(entrepot, BigDecimal.valueOf(1900.0), 20, BigDecimal.valueOf(26), BigDecimal.valueOf(80));
        } else {
            throw new IllegalArgumentException("Pays inconnu : " + codePays);
        }
    }

    @Override
    public List<EntrepotDto> getAllEntrepotsAllPays() {
        List<EntrepotDto> all = new ArrayList<>();
        all.addAll(getAllEntrepots("BR"));
        all.addAll(getAllEntrepots("EC"));
        all.addAll(getAllEntrepots("CO"));
        return all;
    }
}
