package com.futurekawa.backend.service.impl;

import com.futurekawa.backend.model.dto.MesureDto;
import com.futurekawa.backend.service.MesureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class MesureServiceImpl implements MesureService {

    private final RestTemplate restTemplate;
    private final String brasilUrl;

    private static final Map<Long, List<MesureDto>> MESURES_EC;
    private static final Map<Long, List<MesureDto>> MESURES_CO;

    static {
        final LocalDateTime base = LocalDateTime.of(2024, 11, 1, 8, 0);

        Map<Long, List<MesureDto>> ec = new HashMap<>();

        ec.put(100L, List.of(
            MesureDto.builder().id(10001L).lotId(100L).temperature(30.5).humidite(59.5).timestamp(base).build(),
            MesureDto.builder().id(10002L).lotId(100L).temperature(31.2).humidite(60.3).timestamp(base.plusHours(1)).build(),
            MesureDto.builder().id(10003L).lotId(100L).temperature(29.8).humidite(58.8).timestamp(base.plusHours(2)).build(),
            MesureDto.builder().id(10004L).lotId(100L).temperature(32.1).humidite(61.2).timestamp(base.plusHours(3)).build(),
            MesureDto.builder().id(10005L).lotId(100L).temperature(31.7).humidite(60.7).timestamp(base.plusHours(4)).build()
        ));
        // LOT-ECU-2024-002 (EN_ALERTE) — 3e mesure à 35.2°C
        ec.put(101L, List.of(
            MesureDto.builder().id(10101L).lotId(101L).temperature(31.3).humidite(60.1).timestamp(base).build(),
            MesureDto.builder().id(10102L).lotId(101L).temperature(30.8).humidite(59.7).timestamp(base.plusHours(1)).build(),
            MesureDto.builder().id(10103L).lotId(101L).temperature(35.2).humidite(60.5).timestamp(base.plusHours(2)).build(),
            MesureDto.builder().id(10104L).lotId(101L).temperature(31.5).humidite(59.3).timestamp(base.plusHours(3)).build(),
            MesureDto.builder().id(10105L).lotId(101L).temperature(30.9).humidite(61.0).timestamp(base.plusHours(4)).build()
        ));
        ec.put(102L, List.of(
            MesureDto.builder().id(10201L).lotId(102L).temperature(29.9).humidite(58.5).timestamp(base).build(),
            MesureDto.builder().id(10202L).lotId(102L).temperature(31.4).humidite(60.8).timestamp(base.plusHours(1)).build(),
            MesureDto.builder().id(10203L).lotId(102L).temperature(32.0).humidite(61.5).timestamp(base.plusHours(2)).build(),
            MesureDto.builder().id(10204L).lotId(102L).temperature(30.6).humidite(59.2).timestamp(base.plusHours(3)).build(),
            MesureDto.builder().id(10205L).lotId(102L).temperature(31.8).humidite(60.4).timestamp(base.plusHours(4)).build()
        ));
        ec.put(103L, List.of(
            MesureDto.builder().id(10301L).lotId(103L).temperature(31.0).humidite(60.0).timestamp(base).build(),
            MesureDto.builder().id(10302L).lotId(103L).temperature(30.3).humidite(58.9).timestamp(base.plusHours(1)).build(),
            MesureDto.builder().id(10303L).lotId(103L).temperature(32.5).humidite(61.3).timestamp(base.plusHours(2)).build(),
            MesureDto.builder().id(10304L).lotId(103L).temperature(31.7).humidite(59.8).timestamp(base.plusHours(3)).build(),
            MesureDto.builder().id(10305L).lotId(103L).temperature(29.6).humidite(60.6).timestamp(base.plusHours(4)).build()
        ));
        ec.put(104L, List.of(
            MesureDto.builder().id(10401L).lotId(104L).temperature(30.7).humidite(61.1).timestamp(base).build(),
            MesureDto.builder().id(10402L).lotId(104L).temperature(32.3).humidite(59.4).timestamp(base.plusHours(1)).build(),
            MesureDto.builder().id(10403L).lotId(104L).temperature(31.1).humidite(60.2).timestamp(base.plusHours(2)).build(),
            MesureDto.builder().id(10404L).lotId(104L).temperature(29.5).humidite(58.7).timestamp(base.plusHours(3)).build(),
            MesureDto.builder().id(10405L).lotId(104L).temperature(31.9).humidite(61.5).timestamp(base.plusHours(4)).build()
        ));
        ec.put(105L, List.of(
            MesureDto.builder().id(10501L).lotId(105L).temperature(31.6).humidite(59.9).timestamp(base).build(),
            MesureDto.builder().id(10502L).lotId(105L).temperature(30.2).humidite(61.2).timestamp(base.plusHours(1)).build(),
            MesureDto.builder().id(10503L).lotId(105L).temperature(31.8).humidite(58.6).timestamp(base.plusHours(2)).build(),
            MesureDto.builder().id(10504L).lotId(105L).temperature(32.4).humidite(60.9).timestamp(base.plusHours(3)).build(),
            MesureDto.builder().id(10505L).lotId(105L).temperature(30.0).humidite(59.1).timestamp(base.plusHours(4)).build()
        ));
        MESURES_EC = Collections.unmodifiableMap(ec);

        Map<Long, List<MesureDto>> co = new HashMap<>();

        co.put(200L, List.of(
            MesureDto.builder().id(20001L).lotId(200L).temperature(25.8).humidite(79.5).timestamp(base).build(),
            MesureDto.builder().id(20002L).lotId(200L).temperature(26.4).humidite(80.3).timestamp(base.plusHours(1)).build(),
            MesureDto.builder().id(20003L).lotId(200L).temperature(27.1).humidite(81.2).timestamp(base.plusHours(2)).build(),
            MesureDto.builder().id(20004L).lotId(200L).temperature(25.5).humidite(79.8).timestamp(base.plusHours(3)).build(),
            MesureDto.builder().id(20005L).lotId(200L).temperature(26.9).humidite(80.7).timestamp(base.plusHours(4)).build()
        ));
        // LOT-COL-2024-002 (EN_ALERTE) — 3e mesure à 22.1°C
        co.put(201L, List.of(
            MesureDto.builder().id(20101L).lotId(201L).temperature(26.2).humidite(80.1).timestamp(base).build(),
            MesureDto.builder().id(20102L).lotId(201L).temperature(25.9).humidite(79.6).timestamp(base.plusHours(1)).build(),
            MesureDto.builder().id(20103L).lotId(201L).temperature(22.1).humidite(80.4).timestamp(base.plusHours(2)).build(),
            MesureDto.builder().id(20104L).lotId(201L).temperature(26.7).humidite(79.2).timestamp(base.plusHours(3)).build(),
            MesureDto.builder().id(20105L).lotId(201L).temperature(25.4).humidite(81.0).timestamp(base.plusHours(4)).build()
        ));
        co.put(202L, List.of(
            MesureDto.builder().id(20201L).lotId(202L).temperature(24.9).humidite(78.5).timestamp(base).build(),
            MesureDto.builder().id(20202L).lotId(202L).temperature(26.5).humidite(80.8).timestamp(base.plusHours(1)).build(),
            MesureDto.builder().id(20203L).lotId(202L).temperature(27.3).humidite(81.5).timestamp(base.plusHours(2)).build(),
            MesureDto.builder().id(20204L).lotId(202L).temperature(25.1).humidite(79.1).timestamp(base.plusHours(3)).build(),
            MesureDto.builder().id(20205L).lotId(202L).temperature(26.8).humidite(80.4).timestamp(base.plusHours(4)).build()
        ));
        co.put(203L, List.of(
            MesureDto.builder().id(20301L).lotId(203L).temperature(26.0).humidite(80.0).timestamp(base).build(),
            MesureDto.builder().id(20302L).lotId(203L).temperature(25.3).humidite(78.9).timestamp(base.plusHours(1)).build(),
            MesureDto.builder().id(20303L).lotId(203L).temperature(27.5).humidite(81.3).timestamp(base.plusHours(2)).build(),
            MesureDto.builder().id(20304L).lotId(203L).temperature(26.7).humidite(79.8).timestamp(base.plusHours(3)).build(),
            MesureDto.builder().id(20305L).lotId(203L).temperature(24.6).humidite(80.6).timestamp(base.plusHours(4)).build()
        ));
        co.put(204L, List.of(
            MesureDto.builder().id(20401L).lotId(204L).temperature(25.7).humidite(81.1).timestamp(base).build(),
            MesureDto.builder().id(20402L).lotId(204L).temperature(27.3).humidite(79.4).timestamp(base.plusHours(1)).build(),
            MesureDto.builder().id(20403L).lotId(204L).temperature(26.1).humidite(80.2).timestamp(base.plusHours(2)).build(),
            MesureDto.builder().id(20404L).lotId(204L).temperature(24.5).humidite(78.7).timestamp(base.plusHours(3)).build(),
            MesureDto.builder().id(20405L).lotId(204L).temperature(26.9).humidite(81.5).timestamp(base.plusHours(4)).build()
        ));
        co.put(205L, List.of(
            MesureDto.builder().id(20501L).lotId(205L).temperature(26.6).humidite(79.9).timestamp(base).build(),
            MesureDto.builder().id(20502L).lotId(205L).temperature(25.2).humidite(81.2).timestamp(base.plusHours(1)).build(),
            MesureDto.builder().id(20503L).lotId(205L).temperature(26.8).humidite(78.6).timestamp(base.plusHours(2)).build(),
            MesureDto.builder().id(20504L).lotId(205L).temperature(27.4).humidite(80.9).timestamp(base.plusHours(3)).build(),
            MesureDto.builder().id(20505L).lotId(205L).temperature(25.0).humidite(79.1).timestamp(base.plusHours(4)).build()
        ));
        MESURES_CO = Collections.unmodifiableMap(co);
    }

    public MesureServiceImpl(RestTemplate restTemplate,
                             @Value("${backend.brasil.url}") String brasilUrl) {
        this.restTemplate = restTemplate;
        this.brasilUrl = brasilUrl;
    }

    @Override
    public List<MesureDto> getMesuresByLot(String codePays, Long lotId) {
        if (codePays.equalsIgnoreCase("BR")) {
            try {
                MesureDto[] result = restTemplate.getForObject(
                        brasilUrl + "/api/mesures/lot/" + lotId, MesureDto[].class);
                return result != null ? Arrays.asList(result) : Collections.emptyList();
            } catch (Exception e) {
                log.error("Backend Brésil indisponible pour getMesuresByLot({}) : {}", lotId, e.getMessage());
                return Collections.emptyList();
            }
        } else if (codePays.equalsIgnoreCase("EC")) {
            return MESURES_EC.getOrDefault(lotId, Collections.emptyList());
        } else if (codePays.equalsIgnoreCase("CO")) {
            return MESURES_CO.getOrDefault(lotId, Collections.emptyList());
        } else {
            throw new IllegalArgumentException("Pays inconnu : " + codePays);
        }
    }
}
