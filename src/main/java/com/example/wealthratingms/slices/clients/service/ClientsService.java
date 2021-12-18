package com.example.wealthratingms.slices.clients.service;

import com.example.wealthratingms.common.adapters.centralbank.CentralBankAdapter;
import com.example.wealthratingms.common.exceptions.api.NotFoundException;
import com.example.wealthratingms.slices.clients.api.dtos.ClientNewDto;
import com.example.wealthratingms.slices.clients.api.dtos.ClientRichDto;
import com.example.wealthratingms.slices.clients.db.ClientsRepository;
import com.example.wealthratingms.slices.clients.db.daos.ClientDao;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.wealthratingms.common.exceptions.ExceptionMessages.CANT_FIND_CLIENT_WITH_ID;

/**
 * @author Sergey Stol
 * 2021-12-17
 */
@Service
@RequiredArgsConstructor
public class ClientsService {
    private final ClientsRepository repo;
    private final ModelMapper modelMapper;
    private final CentralBankAdapter centralBankAdapter;

    public ClientRichDto getClient(long id) {
        return repo.findById(id)
                .map(this::convertToClientRichDto)
                .orElseThrow(() -> new NotFoundException(CANT_FIND_CLIENT_WITH_ID + id));
    }

    public List<ClientRichDto> getClients() {
        return repo.findAll().stream()
                .map(this::convertToClientRichDto)
                .collect(Collectors.toList());
    }

    public boolean addClient(ClientNewDto clientNewDto) {
        int assetValuation = centralBankAdapter.getAssetEvaluation(clientNewDto.getClientCity());
        long wealthThreshold = centralBankAdapter.getWealthThreshold();
        long fortune = clientNewDto.getCash()
                + (long) clientNewDto.getNumberOfAssets()
                * assetValuation;
        if (fortune > wealthThreshold) {
            repo.save(convertToClientDao(clientNewDto, fortune));
            return true;
        }
        return false;
    }

    public ClientRichDto convertToClientRichDto(ClientDao clientDao) {
        return modelMapper.map(clientDao, ClientRichDto.class);
    }

    public ClientDao convertToClientDao(ClientNewDto newClientDto, long fortune) {
        ClientDao clientDao = modelMapper.map(newClientDto, ClientDao.class);
        clientDao.setFortune(fortune);
        return clientDao;
    }
}