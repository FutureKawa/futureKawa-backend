package com.futurekawa.backend.service.impl;

import com.futurekawa.backend.model.dto.LotDto;
import com.futurekawa.backend.model.dto.LotPageFromBrazil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitaires - LotServiceImpl")
class LotServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    private LotServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new LotServiceImpl(restTemplate, "http://localhost:8081");
    }

    @Nested
    @DisplayName("getAllLots")
    class GetAllLotsTests {

        @Test
        @DisplayName("Doit retourner les lots du Brésil si le backend répond")
        void shouldReturnBrazilLots() {
            LotPageFromBrazil page = new LotPageFromBrazil();
            page.setContent(List.of(
                    LotDto.builder().id(1L).build(),
                    LotDto.builder().id(2L).build()
            ));

            when(restTemplate.exchange(
                    eq("http://localhost:8081/api/lots?page=0&size=1000"),
                    eq(HttpMethod.GET),
                    isNull(),
                    any(ParameterizedTypeReference.class)
            )).thenReturn(ResponseEntity.ok(page));

            List<LotDto> result = service.getAllLots("BR");

            assertNotNull(result);
            assertEquals(2, result.size());
        }

        @Test
        @DisplayName("Doit retourner une liste vide si le backend Brésil échoue")
        void shouldReturnEmptyListWhenBrazilBackendFails() {
            when(restTemplate.exchange(
                    eq("http://localhost:8081/api/lots?page=0&size=1000"),
                    eq(HttpMethod.GET),
                    isNull(),
                    any(ParameterizedTypeReference.class)
            )).thenThrow(new RuntimeException("Backend down"));

            List<LotDto> result = service.getAllLots("BR");

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Doit retourner les lots statiques de l'Équateur")
        void shouldReturnEcuadorLots() {
            List<LotDto> result = service.getAllLots("EC");

            assertNotNull(result);
            assertEquals(6, result.size());
        }

        @Test
        @DisplayName("Doit retourner les lots statiques de la Colombie")
        void shouldReturnColombiaLots() {
            List<LotDto> result = service.getAllLots("CO");

            assertNotNull(result);
            assertEquals(6, result.size());
        }
    }

    @Nested
    @DisplayName("getLotByFunctionalId")
    class GetLotByFunctionalIdTests {

        @Test
        @DisplayName("Doit retourner un lot Brésil par identifiant fonctionnel")
        void shouldReturnBrazilLotByFunctionalId() {
            LotDto dto = LotDto.builder().id(15L).build();

            when(restTemplate.getForObject(
                    "http://localhost:8081/api/lots/search?lotId=15",
                    LotDto.class
            )).thenReturn(dto);

            LotDto result = service.getLotByFunctionalId("BR", 15L);

            assertNotNull(result);
            assertEquals(15L, result.getId());
        }

        @Test
        @DisplayName("Doit retourner un lot équatorien par identifiant fonctionnel")
        void shouldReturnEcuadorLotByFunctionalId() {
            LotDto result = service.getLotByFunctionalId("EC", 100L);

            assertNotNull(result);
            assertEquals(100L, result.getId());
        }

        @Test
        @DisplayName("Doit retourner null si aucun lot équatorien ne correspond")
        void shouldReturnNullWhenNoEcuadorLotMatches() {
            LotDto result = service.getLotByFunctionalId("EC", 999L);

            assertNull(result);
        }
    }

    @Nested
    @DisplayName("getLotsByEntrepot")
    class GetLotsByEntrepotTests {

        @Test
        @DisplayName("Doit retourner les lots d'un entrepôt équatorien")
        void shouldReturnEcuadorLotsByWarehouse() {
            List<LotDto> result = service.getLotsByEntrepot("EC", 1);

            assertNotNull(result);
            assertEquals(3, result.size());
        }

        @Test
        @DisplayName("Doit retourner les lots d'un entrepôt colombien")
        void shouldReturnColombiaLotsByWarehouse() {
            List<LotDto> result = service.getLotsByEntrepot("CO", 2);

            assertNotNull(result);
            assertEquals(3, result.size());
        }
    }

    @Nested
    @DisplayName("getLotsPaged")
    class GetLotsPagedTests {

        @Test
        @DisplayName("Doit retourner une page de lots Brésil")
        void shouldReturnBrazilPagedLots() {
            LotPageFromBrazil page = new LotPageFromBrazil();
            page.setContent(List.of(LotDto.builder().id(1L).dateStockage(LocalDate.now()).build()));
            page.setNumber(0);
            page.setSize(10);
            page.setTotalElements(1);
            page.setTotalPages(1);
            page.setFirst(true);
            page.setLast(true);

            when(restTemplate.exchange(
                    eq("http://localhost:8081/api/lots?page=0&size=10&sort=dateStockage,asc"),
                    eq(HttpMethod.GET),
                    isNull(),
                    any(ParameterizedTypeReference.class)
            )).thenReturn(ResponseEntity.ok(page));

            LotPageFromBrazil result = service.getLotsPaged("BR", 0, 10);

            assertNotNull(result);
            assertEquals(1, result.getContent().size());
        }
    }
}