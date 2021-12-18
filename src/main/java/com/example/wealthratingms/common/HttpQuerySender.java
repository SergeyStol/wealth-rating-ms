package com.example.wealthratingms.common;

import com.example.wealthratingms.common.exceptions.ExceptionMessages;
import com.example.wealthratingms.common.exceptions.adapters.HttpQuerySenderException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * @author Sergey Stol
 * 2021-12-17
 */
@Component
@RequiredArgsConstructor
public class HttpQuerySender {

    final Logger log = LoggerFactory.getLogger(HttpQuerySender.class);

    private final RestTemplate rest;

    public <T> ResponseEntity<T> get(
            URI uri,
            ParameterizedTypeReference<T> parameterizedTypeReference,
            String exceptionMessage) {
        ResponseEntity<T> response = null;
        try {
            response = rest.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    parameterizedTypeReference);
        } catch (RestClientException e) {
            log.error("Unsuccessful request  to {}", uri);
            log.error(ExceptionMessages.CANT_CONNECT_TO_CENTRAL_BANK_SERVER);
            throw new HttpQuerySenderException(exceptionMessage, e);
        }
        return response;
    }
}