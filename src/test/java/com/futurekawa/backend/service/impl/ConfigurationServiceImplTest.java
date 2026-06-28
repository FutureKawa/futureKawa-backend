package com.futurekawa.backend.service.impl;

import com.futurekawa.backend.model.dto.ConfigurationDto;
import com.futurekawa.backend.model.response.ConfigurationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitaires - ConfigurationServiceImpl")
class ConfigurationServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    private ConfigurationServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ConfigurationServiceImpl(restTemplate, "http://localhost:8081");
    }

    @Nested
    @DisplayName("getConfiguration")
    class GetConfigurationTests {

        @Test
        @DisplayName("Doit retourner la configuration du Brésil si le backend répond")
        void shouldReturnBrazilConfigurationWhenBackendResponds() {
            ConfigurationDto dto = ConfigurationDto.builder()
                    .totalEntrepots(3L)
                    .alertesNonTraitees(2L)
                    .temperatureIdeale(BigDecimal.valueOf(25))
                    .temperatureTolerance(BigDecimal.valueOf(2))
                    .humiditeIdeale(BigDecimal.valueOf(70))
                    .humiditeTolerance(BigDecimal.valueOf(5))
                    .dureeConservation(365)
                    .build();

            when(restTemplate.getForObject(
                    "http://localhost:8081/api/configuration",
                    ConfigurationDto.class
            )).thenReturn(dto);

            ConfigurationResponse result = service.getConfiguration("BR");

            assertNotNull(result);
            assertEquals("BR", result.getCodePays());
            assertEquals("Brésil", result.getPays());
            assertEquals(3L, result.getTotalEntrepots());
            assertEquals(2L, result.getAlertes());
            assertEquals(25.0, result.getTempIdeal());
            assertEquals(23.0, result.getTempMin());
            assertEquals(27.0, result.getTempMax());
            assertEquals(70.0, result.getHumiditeIdeal());
            assertEquals(65.0, result.getHumiditeMin());
            assertEquals(75.0, result.getHumiditeMax());
            assertEquals(365, result.getDureeConservation());
        }

        @Test
        @DisplayName("Doit retourner une configuration fallback si le backend Brésil échoue")
        void shouldReturnFallbackConfigurationWhenBrazilBackendFails() {
            when(restTemplate.getForObject(
                    "http://localhost:8081/api/configuration",
                    ConfigurationDto.class
            )).thenThrow(new RuntimeException("Backend down"));

            ConfigurationResponse result = service.getConfiguration("BR");

            assertNotNull(result);
            assertEquals("BR", result.getCodePays());
            assertEquals("Brésil", result.getPays());
            assertEquals(0.0, result.getTempIdeal());
            assertEquals(0.0, result.getHumiditeIdeal());
            assertEquals(0.0, result.getTempMin());
            assertEquals(0.0, result.getTempMax());
            assertEquals(0.0, result.getHumiditeMin());
            assertEquals(0.0, result.getHumiditeMax());
        }

        @Test
        @DisplayName("Doit retourner la configuration statique de l'Équateur")
        void shouldReturnEcuadorConfiguration() {
            ConfigurationResponse result = service.getConfiguration("EC");

            assertNotNull(result);
            assertEquals("EC", result.getCodePays());
            assertEquals("Équateur", result.getPays());
            assertEquals(31.0, result.getTempIdeal());
            assertEquals(60.0, result.getHumiditeIdeal());
            assertEquals(365, result.getDureeConservation());
        }

        @Test
        @DisplayName("Doit retourner la configuration statique de la Colombie")
        void shouldReturnColombiaConfiguration() {
            ConfigurationResponse result = service.getConfiguration("CO");

            assertNotNull(result);
            assertEquals("CO", result.getCodePays());
            assertEquals("Colombie", result.getPays());
            assertEquals(26.0, result.getTempIdeal());
            assertEquals(80.0, result.getHumiditeIdeal());
            assertEquals(365, result.getDureeConservation());
        }

        @Test
        @DisplayName("Doit lever une exception si le pays est inconnu")
        void shouldThrowExceptionWhenCountryIsUnknown() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> service.getConfiguration("XX")
            );

            assertTrue(exception.getMessage().contains("Pays inconnu"));
        }
    }
}