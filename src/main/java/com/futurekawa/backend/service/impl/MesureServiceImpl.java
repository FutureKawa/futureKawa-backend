package com.futurekawa.backend.service.impl;

import com.futurekawa.backend.model.dto.LotDto;
import com.futurekawa.backend.model.dto.MesureDto;
import com.futurekawa.backend.service.LotService;
import com.futurekawa.backend.service.MesureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
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

        ec.put(1L, List.of(
                MesureDto.builder().id(10001L).espId(1L).entrepotId(1).temperature(BigDecimal.valueOf(30.5)).humidity(BigDecimal.valueOf(59.5)).timestamp(base).conforme(true).build(),
                MesureDto.builder().id(10002L).espId(1L).entrepotId(1).temperature(BigDecimal.valueOf(31.2)).humidity(BigDecimal.valueOf(60.3)).timestamp(base.plusDays(1)).conforme(true).build(),
                MesureDto.builder().id(10003L).espId(1L).entrepotId(1).temperature(BigDecimal.valueOf(29.8)).humidity(BigDecimal.valueOf(58.8)).timestamp(base.plusDays(2)).conforme(true).build(),
                MesureDto.builder().id(10004L).espId(1L).entrepotId(1).temperature(BigDecimal.valueOf(32.1)).humidity(BigDecimal.valueOf(61.2)).timestamp(base.plusDays(3)).conforme(true).build(),
                MesureDto.builder().id(10005L).espId(1L).entrepotId(1).temperature(BigDecimal.valueOf(31.7)).humidity(BigDecimal.valueOf(60.7)).timestamp(base.plusDays(4)).conforme(true).build()
        ));

        ec.put(2L, List.of(
                MesureDto.builder().id(10101L).espId(2L).entrepotId(2).temperature(BigDecimal.valueOf(31.3)).humidity(BigDecimal.valueOf(60.1)).timestamp(base).conforme(true).build(),
                MesureDto.builder().id(10102L).espId(2L).entrepotId(2).temperature(BigDecimal.valueOf(30.8)).humidity(BigDecimal.valueOf(59.7)).timestamp(base.plusDays(1)).conforme(true).build(),
                MesureDto.builder().id(10103L).espId(2L).entrepotId(2).temperature(BigDecimal.valueOf(35.2)).humidity(BigDecimal.valueOf(60.5)).timestamp(base.plusDays(2)).conforme(false).build(),
                MesureDto.builder().id(10104L).espId(2L).entrepotId(2).temperature(BigDecimal.valueOf(31.5)).humidity(BigDecimal.valueOf(59.3)).timestamp(base.plusDays(3)).conforme(true).build(),
                MesureDto.builder().id(10105L).espId(2L).entrepotId(2).temperature(BigDecimal.valueOf(30.9)).humidity(BigDecimal.valueOf(61.0)).timestamp(base.plusDays(4)).conforme(true).build()
        ));

        MESURES_EC = Collections.unmodifiableMap(ec);

        Map<Long, List<MesureDto>> co = new HashMap<>();

        co.put(1L, List.of(
                MesureDto.builder().id(20001L).espId(1L).entrepotId(1).temperature(BigDecimal.valueOf(25.8)).humidity(BigDecimal.valueOf(79.5)).timestamp(base).conforme(true).build(),
                MesureDto.builder().id(20002L).espId(1L).entrepotId(1).temperature(BigDecimal.valueOf(26.4)).humidity(BigDecimal.valueOf(80.3)).timestamp(base.plusDays(1)).conforme(true).build(),
                MesureDto.builder().id(20003L).espId(1L).entrepotId(1).temperature(BigDecimal.valueOf(27.1)).humidity(BigDecimal.valueOf(81.2)).timestamp(base.plusDays(2)).conforme(true).build(),
                MesureDto.builder().id(20004L).espId(1L).entrepotId(1).temperature(BigDecimal.valueOf(25.5)).humidity(BigDecimal.valueOf(79.8)).timestamp(base.plusDays(3)).conforme(true).build(),
                MesureDto.builder().id(20005L).espId(1L).entrepotId(1).temperature(BigDecimal.valueOf(26.9)).humidity(BigDecimal.valueOf(80.7)).timestamp(base.plusDays(4)).conforme(true).build()
        ));

        co.put(2L, List.of(
                MesureDto.builder().id(20101L).espId(2L).entrepotId(2).temperature(BigDecimal.valueOf(26.2)).humidity(BigDecimal.valueOf(80.1)).timestamp(base).conforme(true).build(),
                MesureDto.builder().id(20102L).espId(2L).entrepotId(2).temperature(BigDecimal.valueOf(25.9)).humidity(BigDecimal.valueOf(79.6)).timestamp(base.plusDays(1)).conforme(true).build(),
                MesureDto.builder().id(20103L).espId(2L).entrepotId(2).temperature(BigDecimal.valueOf(22.1)).humidity(BigDecimal.valueOf(80.4)).timestamp(base.plusDays(2)).conforme(false).build(),
                MesureDto.builder().id(20104L).espId(2L).entrepotId(2).temperature(BigDecimal.valueOf(26.7)).humidity(BigDecimal.valueOf(79.2)).timestamp(base.plusDays(3)).conforme(true).build(),
                MesureDto.builder().id(20105L).espId(2L).entrepotId(2).temperature(BigDecimal.valueOf(25.4)).humidity(BigDecimal.valueOf(81.0)).timestamp(base.plusDays(4)).conforme(true).build()
        ));

        MESURES_CO = Collections.unmodifiableMap(co);
    }

    private final LotService lotService;

    public MesureServiceImpl(RestTemplate restTemplate,
                             @Value("${backend.brasil.url}") String brasilUrl, LotService lotService) {
        this.restTemplate = restTemplate;
        this.brasilUrl = brasilUrl;
        this.lotService = lotService;
    }

    @Override
    public List<MesureDto> getMesuresByLot(String codePays, Long lotId) {
        if (codePays.equalsIgnoreCase("BR")) {
            try {
                MesureDto[] result = restTemplate.getForObject(
                        brasilUrl + "/api/mesures/lots/" + lotId, MesureDto[].class);
                return result != null ? Arrays.asList(result) : Collections.emptyList();
            } catch (Exception e) {
                log.error("Backend Brésil indisponible pour getMesuresByLot({}) : {}", lotId, e.getMessage());
                return Collections.emptyList();
            }
        } else if (codePays.equalsIgnoreCase("EC")) {
            final LotDto lot = lotService.getLotByFunctionalId(codePays, lotId);

            if (lot == null || lot.getEntrepotId() == null || lot.getDateStockage() == null) {
                return Collections.emptyList();
            }

            final List<MesureDto> mesureDtos = MESURES_EC.getOrDefault(
                    lot.getEntrepotId().longValue(),
                    Collections.emptyList()
            );

            return mesureDtos.stream()
                    .filter(Objects::nonNull)
                    .filter(m -> m.getEntrepotId() != null)
                    .filter(m -> Objects.equals(m.getEntrepotId(), lot.getEntrepotId()))
                    .filter(m -> m.getTimestamp() != null)
                    .filter(m -> !m.getTimestamp().isBefore(lot.getDateStockage().atStartOfDay()))
                    .toList();

        } else if (codePays.equalsIgnoreCase("CO")) {
            final LotDto lot = lotService.getLotByFunctionalId(codePays, lotId);

            if (lot == null || lot.getEntrepotId() == null || lot.getDateStockage() == null) {
                return Collections.emptyList();
            }

            final List<MesureDto> mesureDtos = MESURES_CO.getOrDefault(
                    lot.getEntrepotId().longValue(),
                    Collections.emptyList()
            );

            return mesureDtos.stream()
                    .filter(Objects::nonNull)
                    .filter(m -> m.getEntrepotId() != null)
                    .filter(m -> Objects.equals(m.getEntrepotId(), lot.getEntrepotId()))
                    .filter(m -> m.getTimestamp() != null)
                    .filter(m -> !m.getTimestamp().isBefore(lot.getDateStockage().atStartOfDay()))
                    .toList();
        } else {
            throw new IllegalArgumentException("Pays inconnu : " + codePays);
        }
    }
}
