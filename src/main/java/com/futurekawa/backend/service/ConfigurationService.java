package com.futurekawa.backend.service;

import com.futurekawa.backend.model.response.ConfigurationResponse;

public interface ConfigurationService {
    ConfigurationResponse getConfiguration(String codePays);
}
