package com.example.wealthratingms.common.adapters.centralbank;

import com.example.wealthratingms.common.HttpQuerySender;
import com.example.wealthratingms.common.adapters.Adapter;
import com.example.wealthratingms.common.exceptions.ExceptionMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * @author Sergey Stol
 * 2021-12-17
 */
@Adapter
@RequiredArgsConstructor
public class CentralBankAdapter {
    private final CentralBankProperties properties;
    private final HttpQuerySender querySender;

    public int getAssetEvaluation(String city) {
        URI uri = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(properties.getHost())
                .port(properties.getPort())
                .path(properties.getEndpointEvaluate())
                .queryParam("city", city)
                .build().toUri();

        // {host}:{port}/central-bank/regional-info/evaluate?city={city}
        ResponseEntity<Integer> response =
                querySender.get(uri, new ParameterizedTypeReference<Integer>() {},
                        ExceptionMessages.CANT_CONNECT_TO_CENTRAL_BANK_SERVER);
        return response.getBody() == null ? -1 : response.getBody();
    }

    public long getWealthThreshold() {
        URI uri = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(properties.getHost())
                .port(properties.getPort())
                .path(properties.getEndpointWealthThreshold())
                .build().toUri();

        // {host}:{port}/central-bank/wealth-threshold
        ResponseEntity<Long> response =
                querySender.get(uri, new ParameterizedTypeReference<Long>() {},
                        ExceptionMessages.CANT_CONNECT_TO_CENTRAL_BANK_SERVER);
        return response.getBody() == null ? -1 : response.getBody();
    }
}