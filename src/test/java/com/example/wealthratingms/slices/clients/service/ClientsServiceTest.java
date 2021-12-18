package com.example.wealthratingms.slices.clients.service;

import com.example.wealthratingms.common.adapters.centralbank.CentralBankAdapter;
import com.example.wealthratingms.common.exceptions.ExceptionMessages;
import com.example.wealthratingms.common.exceptions.api.NotFoundException;
import com.example.wealthratingms.slices.clients.api.dtos.ClientNewDto;
import com.example.wealthratingms.slices.clients.api.dtos.ClientRichDto;
import com.example.wealthratingms.slices.clients.api.dtos.FinancialInfoDto;
import com.example.wealthratingms.slices.clients.api.dtos.PersonalInfoDto;
import com.example.wealthratingms.slices.clients.db.ClientsRepository;
import com.example.wealthratingms.slices.clients.db.daos.ClientDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientsServiceTest {

    @Mock
    ClientsRepository repo;
    @Mock
    ModelMapper modelMapper;
    @Mock
    CentralBankAdapter centralBankAdapter;
    @InjectMocks
    ClientsService clientsService;

    ClientDao clientDaoExpected;
    ClientRichDto clientDtoExpected;
    ClientNewDto clientNewDto;

    @BeforeEach
    void beforeEach() {
        clientDaoExpected = new ClientDao(1L, "firstName1", "lastName1", 111L);
        clientDtoExpected = new ClientRichDto(1L, "firstName1", "lastName1", 111L);
        clientNewDto = ClientNewDto.builder()
                .id(123L)
                .personalInfoDto(new PersonalInfoDto("firstName2", "lastName2", "city2"))
                .financialInfoDto(new FinancialInfoDto(100L, 50))
                .build();
    }

    @Test
    void getClient() {
        when(repo.findById(1L)).thenReturn(Optional.of(clientDaoExpected));
        when(repo.findById(2L)).thenReturn(Optional.empty());
        when(clientsService.convertToClientRichDto(clientDaoExpected)).thenReturn(clientDtoExpected);

        assertThat(clientsService.getClient(1L)).isEqualTo(clientDtoExpected);
        try {
            clientsService.getClient(2L);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(NotFoundException.class);
            assertThat(e.getMessage()).isEqualTo(ExceptionMessages.CANT_FIND_CLIENT_WITH_ID + "2");
        }
    }

    @Test
    void getClientShouldThrowNotFoundExceptionWhenConverterThrowNPE() {
        lenient().when(repo.findById(1L)).thenReturn(Optional.of(clientDaoExpected));

        lenient().when(clientsService.convertToClientRichDto(clientDaoExpected))
                .thenThrow(NullPointerException.class);
        try {
            clientsService.getClient(2L);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(NotFoundException.class);
            assertThat(e.getMessage()).isEqualTo(ExceptionMessages.CANT_FIND_CLIENT_WITH_ID + "2");
        }
    }

    // *************************************************************************
    // List<ClientRichDto> getClients()
    // *************************************************************************
    @Test
    void getClientsShouldReturnListOfClientsWhenRichClientsPresentInDB() {
        lenient().when(repo.findAll()).thenReturn(List.of(clientDaoExpected));
        when(clientsService.convertToClientRichDto(clientDaoExpected))
                .thenReturn(clientDtoExpected);

        assertThat(clientsService.getClients()).isEqualTo(List.of(clientDtoExpected));
    }

    @Test
    void getClientsShouldReturnEmptyListWhenDBIsEmpty() {
        lenient().when(repo.findAll()).thenReturn(List.of());

        assertThat(clientsService.getClients()).isEqualTo(List.of());
    }

    // *************************************************************************
    // boolean addClient(ClientNewDto clientNewDto)
    // *************************************************************************
    @Test
    void addClient() {
        when(centralBankAdapter.getWealthThreshold()).thenReturn(1000L);
        when(modelMapper.map(any(), any())).thenReturn(clientDaoExpected);
        when(repo.save(any())).thenReturn(null);

        lenient().when(centralBankAdapter.getAssetEvaluation(clientNewDto.getClientCity())).thenReturn(100);
        assertThat(clientsService.addClient(clientNewDto)).isTrue();

        lenient().when(centralBankAdapter.getAssetEvaluation(clientNewDto.getClientCity())).thenReturn(10);
        assertThat(clientsService.addClient(clientNewDto)).isFalse();
    }
}