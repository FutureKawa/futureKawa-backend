package com.futurekawa.backend.controller;

import com.futurekawa.backend.model.response.ConfigurationResponse;
import com.futurekawa.backend.service.ConfigurationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ConfigurationController {

    private final ConfigurationService configurationService;

    public ConfigurationController(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @GetMapping("/{codePays}/configuration")
    public ResponseEntity<ConfigurationResponse> getConfiguration(@PathVariable String codePays) {
        return ResponseEntity.ok(configurationService.getConfiguration(codePays));
    }
}
