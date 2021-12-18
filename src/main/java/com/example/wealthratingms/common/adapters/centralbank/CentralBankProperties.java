package com.example.wealthratingms.common.adapters.centralbank;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Sergey Stol
 * 2021-12-17
 */
@Configuration
@ConfigurationProperties(prefix = "adapters.central-bank")
@Data
public class CentralBankProperties {
    private String host;
    private int port;
    private String endpointEvaluate;
    private String endpointWealthThreshold;
}
