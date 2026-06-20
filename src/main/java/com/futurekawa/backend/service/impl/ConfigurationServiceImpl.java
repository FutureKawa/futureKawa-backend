package com.futurekawa.backend.service.impl;

import com.futurekawa.backend.model.dto.ConfigurationDto;
import com.futurekawa.backend.model.response.ConfigurationResponse;
import com.futurekawa.backend.service.ConfigurationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class ConfigurationServiceImpl implements ConfigurationService {

    private final RestTemplate restTemplate;
    private final String brasilUrl;

    public ConfigurationServiceImpl(RestTemplate restTemplate,
                                    @Value("${backend.brasil.url}") String brasilUrl) {
        this.restTemplate = restTemplate;
        this.brasilUrl = brasilUrl;
    }

    @Override
    public ConfigurationResponse getConfiguration(String codePays) {
        if (codePays.equalsIgnoreCase("BR")) {
            try {
                final ConfigurationDto conf =  restTemplate.getForObject(brasilUrl + "/api/configuration", ConfigurationDto.class);
                return ConfigurationResponse.builder()
                        .codePays("BR")
                        .pays("Brésil")
                        .totalEntrepots(conf.getTotalEntrepots())
                        .alertes(conf.getAlertesNonTraitees())
                        .tempIdeal(conf.getTemperatureIdeale().doubleValue())
                        .humiditeIdeal(conf.getHumiditeIdeale().doubleValue())
                        .tempMin(conf.getHumiditeIdeale().doubleValue() - conf.getTemperatureTolerance().doubleValue())
                        .tempMax(conf.getHumiditeIdeale().doubleValue() + conf.getTemperatureTolerance().doubleValue())
                        .humiditeMin(conf.getHumiditeIdeale().doubleValue() - conf.getHumiditeTolerance().doubleValue())
                        .humiditeMax(conf.getHumiditeIdeale().doubleValue() + conf.getHumiditeTolerance().doubleValue())
                        .dureeConservation(conf.getDureeConservation())
                        .build();
            } catch (Exception e) {
                log.error("Backend Brésil indisponible pour getConfiguration : {}", e.getMessage());
                return ConfigurationResponse.builder()
                        .codePays("BR").pays("Brésil")
                        .tempIdeal(0.0).humiditeIdeal(0.0)
                        .tempMin(0.0).tempMax(0.0)
                        .humiditeMin(0.0).humiditeMax(0.0)
                        .build();
            }
        } else if (codePays.equalsIgnoreCase("EC")) {
            return ConfigurationResponse.builder()
                    .codePays("EC")
                    .pays("Équateur")
                    .totalEntrepots(2L)
                    .alertes(0L)
                    .tempIdeal(31.0)
                    .humiditeIdeal(60.0)
                    .tempMin(28.0)
                    .tempMax(34.0)
                    .humiditeMin(58.0)
                    .humiditeMax(62.0)
                    .dureeConservation(365)
                    .build();
        } else if (codePays.equalsIgnoreCase("CO")) {
            return ConfigurationResponse.builder()
                    .codePays("CO")
                    .pays("Colombie")
                    .totalEntrepots(2L)
                    .alertes(0L)
                    .tempIdeal(26.0)
                    .humiditeIdeal(80.0)
                    .tempMin(23.0)
                    .tempMax(29.0)
                    .humiditeMin(78.0)
                    .humiditeMax(82.0)
                    .dureeConservation(365)
                    .build();
        } else {
            throw new IllegalArgumentException("Pays inconnu : " + codePays);
        }
    }
}
