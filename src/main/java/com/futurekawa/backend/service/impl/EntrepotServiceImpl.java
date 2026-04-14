package com.futurekawa.backend.service.impl;

import com.futurekawa.backend.model.dto.EntrepotDto;
import com.futurekawa.backend.service.EntrepotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class EntrepotServiceImpl implements EntrepotService {

    private final RestTemplate restTemplate;
    private final String brasilUrl;

    private static final List<EntrepotDto> ENTREPOTS_EC = List.of(
        EntrepotDto.builder()
            .id(10L).nom("Quito Central").adresse("Av. Amazonas 1200, Quito")
            .codePays("EC").responsable("Carlos Mendez")
            .emailResponsable("c.mendez@futurekawa.ec")
            .latitude(-0.1807).longitude(-78.4678).nombreLots(4)
            .build(),
        EntrepotDto.builder()
            .id(11L).nom("Guayaquil Sud").adresse("Blvd. 9 de Octubre 800, Guayaquil")
            .codePays("EC").responsable("Ana Torres")
            .emailResponsable("a.torres@futurekawa.ec")
            .latitude(-2.1894).longitude(-79.8891).nombreLots(3)
            .build()
    );

    private static final List<EntrepotDto> ENTREPOTS_CO = List.of(
        EntrepotDto.builder()
            .id(20L).nom("Bogotá Est").adresse("Cra. 7 #45-12, Bogotá")
            .codePays("CO").responsable("Miguel Reyes")
            .emailResponsable("m.reyes@futurekawa.co")
            .latitude(4.7110).longitude(-74.0721).nombreLots(4)
            .build(),
        EntrepotDto.builder()
            .id(21L).nom("Medellín Nord").adresse("Calle 50 #40-30, Medellín")
            .codePays("CO").responsable("Laura Gomez")
            .emailResponsable("l.gomez@futurekawa.co")
            .latitude(6.2518).longitude(-75.5636).nombreLots(3)
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
                return result != null ? Arrays.asList(result) : Collections.emptyList();
            } catch (Exception e) {
                log.error("Backend Brésil indisponible pour getAllEntrepots : {}", e.getMessage());
                return Collections.emptyList();
            }
        } else if (codePays.equalsIgnoreCase("EC")) {
            return ENTREPOTS_EC;
        } else if (codePays.equalsIgnoreCase("CO")) {
            return ENTREPOTS_CO;
        } else {
            throw new IllegalArgumentException("Pays inconnu : " + codePays);
        }
    }

    @Override
    public EntrepotDto getEntrepotById(String codePays, Long id) {
        if (codePays.equalsIgnoreCase("BR")) {
            try {
                return restTemplate.getForObject(
                        brasilUrl + "/api/entrepots/" + id, EntrepotDto.class);
            } catch (Exception e) {
                log.error("Backend Brésil indisponible pour getEntrepotById({}) : {}", id, e.getMessage());
                return null;
            }
        } else if (codePays.equalsIgnoreCase("EC")) {
            return ENTREPOTS_EC.stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null);
        } else if (codePays.equalsIgnoreCase("CO")) {
            return ENTREPOTS_CO.stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null);
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
