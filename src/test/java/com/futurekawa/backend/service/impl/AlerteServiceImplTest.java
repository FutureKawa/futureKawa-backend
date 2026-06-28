package com.futurekawa.backend.service.impl;

import com.futurekawa.backend.model.dto.AlerteDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitaires - AlerteServiceImpl")
class AlerteServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    private AlerteServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new AlerteServiceImpl(restTemplate, "http://localhost:8081");
    }

    @Nested
    @DisplayName("getAllAlertes")
    class GetAllAlertesTests {

        @Test
        @DisplayName("Doit retourner les alertes du Brésil si le backend répond")
        void shouldReturnBrazilAlerts() {
            AlerteDto[] response = new AlerteDto[]{
                    AlerteDto.builder().id(1).build(),
                    AlerteDto.builder().id(2).build()
            };

            when(restTemplate.getForObject(
                    "http://localhost:8081/api/alertes",
                    AlerteDto[].class
            )).thenReturn(response);

            List<AlerteDto> result = service.getAllAlertes("BR");

            assertNotNull(result);
            assertEquals(2, result.size());
        }

        @Test
        @DisplayName("Doit retourner une liste vide si le backend Brésil échoue")
        void shouldReturnEmptyListWhenBrazilBackendFails() {
            when(restTemplate.getForObject(
                    "http://localhost:8081/api/alertes",
                    AlerteDto[].class
            )).thenThrow(new RuntimeException("Backend down"));

            List<AlerteDto> result = service.getAllAlertes("BR");

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Doit retourner les alertes statiques de l'Équateur")
        void shouldReturnEcuadorAlerts() {
            List<AlerteDto> result = service.getAllAlertes("EC");

            assertNotNull(result);
            assertEquals(2, result.size());
        }

        @Test
        @DisplayName("Doit retourner les alertes statiques de la Colombie")
        void shouldReturnColombiaAlerts() {
            List<AlerteDto> result = service.getAllAlertes("CO");

            assertNotNull(result);
            assertEquals(2, result.size());
        }
    }

    @Nested
    @DisplayName("getAlertesByLot")
    class GetAlertesByLotTests {

        @Test
        @DisplayName("Doit filtrer les alertes équatoriennes par lot")
        void shouldFilterEcuadorAlertsByLot() {
            List<AlerteDto> result = service.getAlertesByLot("EC", 101L);

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(101L, result.get(0).getLotId());
        }

        @Test
        @DisplayName("Doit retourner une liste vide si aucune alerte colombienne ne correspond au lot")
        void shouldReturnEmptyListWhenNoColombiaAlertMatchesLot() {
            List<AlerteDto> result = service.getAlertesByLot("CO", 999L);

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("getAlertesByEntrepot")
    class GetAlertesByEntrepotTests {

        @Test
        @DisplayName("Doit filtrer les alertes équatoriennes par entrepôt")
        void shouldFilterEcuadorAlertsByWarehouse() {
            List<AlerteDto> result = service.getAlertesByEntrepot("EC", 1);

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(1, result.get(0).getEntrepotId());
        }

        @Test
        @DisplayName("Doit filtrer les alertes colombiennes par entrepôt")
        void shouldFilterColombiaAlertsByWarehouse() {
            List<AlerteDto> result = service.getAlertesByEntrepot("CO", 2);

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(2, result.get(0).getEntrepotId());
        }
    }

    @Nested
    @DisplayName("getAllAlertesAllPays")
    class GetAllAlertesAllPaysTests {

        @Test
        @DisplayName("Doit agréger toutes les alertes de tous les pays")
        void shouldAggregateAllAlerts() {
            when(restTemplate.getForObject(
                    "http://localhost:8081/api/alertes",
                    AlerteDto[].class
            )).thenReturn(new AlerteDto[]{AlerteDto.builder().id(1).build()});

            List<AlerteDto> result = service.getAllAlertesAllPays();

            assertNotNull(result);
            assertEquals(5, result.size());
        }
    }
}