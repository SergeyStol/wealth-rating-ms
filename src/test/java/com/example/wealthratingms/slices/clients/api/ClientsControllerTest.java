package com.example.wealthratingms.slices.clients.api;

import com.example.wealthratingms.common.adapters.centralbank.CentralBankAdapter;
import com.example.wealthratingms.common.api.ApiConstants;
import com.example.wealthratingms.common.exceptions.ExceptionMessages;
import com.example.wealthratingms.common.exceptions.api.NotFoundException;
import com.example.wealthratingms.slices.clients.api.dtos.ClientNewDto;
import com.example.wealthratingms.slices.clients.api.dtos.ClientRichDto;
import com.example.wealthratingms.slices.clients.api.dtos.FinancialInfoDto;
import com.example.wealthratingms.slices.clients.api.dtos.PersonalInfoDto;
import com.example.wealthratingms.slices.clients.service.ClientsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static com.example.wealthratingms.common.api.ApiConstants.API_PREFIX_CLIENTS;
import static com.example.wealthratingms.common.api.ApiConstants.API_PREFIX_VERSION;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ClientsController.class)
class ClientsControllerTest {
    private ResultMatcher _200_OK = MockMvcResultMatchers.status().isOk();
    private ResultMatcher _201_CREATED = MockMvcResultMatchers.status().isCreated();
    private ResultMatcher _404_NOT_FOUND = MockMvcResultMatchers.status().isNotFound();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientsService service;

    @MockBean
    private CentralBankAdapter centralBankAdapter;

    ClientRichDto clientRichDtoExpected;
    ClientNewDto clientNewDtoRich;
    ClientNewDto clientNewDtoPoor;

    ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        clientRichDtoExpected = new ClientRichDto(
                123L,
                "firstName1",
                "lastName1",
                123456789123456L);

        clientNewDtoRich = ClientNewDto.builder()
                .id(123L)
                .personalInfoDto(new PersonalInfoDto("firstName2", "lastName2", "city2"))
                .financialInfoDto(new FinancialInfoDto(160000000000000010L, 50))
                .build();

        clientNewDtoPoor = ClientNewDto.builder()
                .id(123L)
                .personalInfoDto(new PersonalInfoDto("firstName3", "lastName3", "city3"))
                .financialInfoDto(new FinancialInfoDto(160000000L, 10))
                .build();

    }

    @Test
    void getClient() throws Exception{
        when(service.getClient(clientRichDtoExpected.getId())).thenReturn(clientRichDtoExpected);

        mockMvc.perform(get(API_PREFIX_VERSION + API_PREFIX_CLIENTS
                        + "/" + clientRichDtoExpected.getId()))
                .andExpect(_200_OK)
                .andExpect(content().json(mapper.writeValueAsString(clientRichDtoExpected)));

        when(service.getClient(clientRichDtoExpected.getId()))
                .thenThrow(new NotFoundException(ExceptionMessages.CANT_FIND_CLIENT_WITH_ID));

        mockMvc.perform(get(API_PREFIX_VERSION + API_PREFIX_CLIENTS
                        + "/" + "123"))
                .andExpect(_404_NOT_FOUND)
                .andExpect(content().string(ExceptionMessages.CANT_FIND_CLIENT_WITH_ID));
    }

    @Test
    void getClients() throws Exception {
        when(service.getClients()).thenReturn(List.of(clientRichDtoExpected));

        mockMvc.perform(get(API_PREFIX_VERSION + API_PREFIX_CLIENTS))
                .andExpect(_200_OK)
                .andExpect(content().json(mapper.writeValueAsString(List.of(clientRichDtoExpected))));
    }

    @Test
    void addClient() throws Exception {
        when(service.addClient(clientNewDtoRich)).thenReturn(true);
        when(service.addClient(clientNewDtoPoor)).thenReturn(false);

        mockMvc.perform(post(API_PREFIX_VERSION + API_PREFIX_CLIENTS)
                .content(mapper.writeValueAsString(clientNewDtoRich))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(_201_CREATED);

        mockMvc.perform(post(API_PREFIX_VERSION + API_PREFIX_CLIENTS)
                        .content(mapper.writeValueAsString(clientNewDtoPoor))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(_200_OK);
    }
}