package com.futurekawa.backend.service.impl;

import com.futurekawa.backend.model.dto.LotDto;
import com.futurekawa.backend.model.dto.MesureDto;
import com.futurekawa.backend.service.LotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitaires - MesureServiceImpl")
class MesureServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private LotService lotService;

    private MesureServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new MesureServiceImpl(restTemplate, "http://localhost:8081", lotService);
    }

    @Nested
    @DisplayName("getMesuresByLot")
    class GetMesuresByLotTests {

        @Test
        @DisplayName("Doit retourner les mesures du Brésil si le backend répond")
        void shouldReturnBrazilMeasures() {
            MesureDto[] response = new MesureDto[]{
                    MesureDto.builder().id(1L).build(),
                    MesureDto.builder().id(2L).build()
            };

            when(restTemplate.getForObject(
                    "http://localhost:8081/api/mesures/lots/10",
                    MesureDto[].class
            )).thenReturn(response);

            List<MesureDto> result = service.getMesuresByLot("BR", 10L);

            assertNotNull(result);
            assertEquals(2, result.size());
        }

        @Test
        @DisplayName("Doit retourner une liste vide si le backend Brésil échoue")
        void shouldReturnEmptyListWhenBrazilBackendFails() {
            when(restTemplate.getForObject(
                    "http://localhost:8081/api/mesures/lots/10",
                    MesureDto[].class
            )).thenThrow(new RuntimeException("Backend down"));

            List<MesureDto> result = service.getMesuresByLot("BR", 10L);

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Doit retourner les mesures EC du lot à partir de l'entrepôt et de la date de stockage")
        void shouldReturnEcuadorMeasuresFromLot() {
            LotDto lot = LotDto.builder()
                    .id(100L)
                    .entrepotId(1)
                    .dateStockage(LocalDate.of(2024, 11, 1))
                    .build();

            when(lotService.getLotByFunctionalId("EC", 100L)).thenReturn(lot);

            List<MesureDto> result = service.getMesuresByLot("EC", 100L);

            assertNotNull(result);
            assertFalse(result.isEmpty());
        }

        @Test
        @DisplayName("Doit retourner une liste vide si le lot EC est introuvable")
        void shouldReturnEmptyListWhenEcuadorLotIsNull() {
            when(lotService.getLotByFunctionalId("EC", 100L)).thenReturn(null);

            List<MesureDto> result = service.getMesuresByLot("EC", 100L);

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("getMesuresByEntrepot")
    class GetMesuresByEntrepotTests {

        @Test
        @DisplayName("Doit retourner les mesures statiques de l'entrepôt équatorien")
        void shouldReturnEcuadorMeasuresByWarehouse() {
            List<MesureDto> result = service.getMesuresByEntrepot("EC", 1);

            assertNotNull(result);
            assertEquals(5, result.size());
        }

        @Test
        @DisplayName("Doit retourner les mesures statiques de l'entrepôt colombien")
        void shouldReturnColombiaMeasuresByWarehouse() {
            List<MesureDto> result = service.getMesuresByEntrepot("CO", 2);

            assertNotNull(result);
            assertEquals(5, result.size());
        }
    }
}