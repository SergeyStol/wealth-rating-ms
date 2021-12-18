package com.example.wealthratingms.slices.clients.api;

import com.example.wealthratingms.common.adapters.centralbank.CentralBankAdapter;
import com.example.wealthratingms.slices.clients.api.dtos.ClientNewDto;
import com.example.wealthratingms.slices.clients.api.dtos.ClientRichDto;
import com.example.wealthratingms.slices.clients.service.ClientsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.wealthratingms.common.api.ApiConstants.API_PREFIX_CLIENTS;
import static com.example.wealthratingms.common.api.ApiConstants.API_PREFIX_VERSION;

/**
 * @author Sergey Stol
 * 2021-12-17
 */
@RestController
@RequestMapping(API_PREFIX_VERSION + API_PREFIX_CLIENTS)
@RequiredArgsConstructor
public class ClientsController {
    private final ClientsService service;
    private final CentralBankAdapter centralBankAdapter;

    // GET /v1/clients/:id
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ClientRichDto getClient(@PathVariable long id) {
        return service.getClient(id);
    }

    // GET /v1/clients
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ClientRichDto> getClients() {
        return service.getClients();
    }

    // POST /v1/clients
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addClient(@RequestBody ClientNewDto clientDto) {
        return service.addClient(clientDto)
                ? ResponseEntity.status(201).build()
                : ResponseEntity.status(200).build();
    }
}