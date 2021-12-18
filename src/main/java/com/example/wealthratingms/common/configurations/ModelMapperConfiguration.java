package com.example.wealthratingms.common.configurations;

import com.example.wealthratingms.slices.clients.api.dtos.ClientNewDto;
import com.example.wealthratingms.slices.clients.db.daos.ClientDao;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Sergey Stol
 * 2021-12-17
 */
@Configuration
public class ModelMapperConfiguration {
    @Bean
    ModelMapper modelMapper() {
        PropertyMap<ClientNewDto, ClientDao> clientDtoToDao =
                new PropertyMap<>() {
                    @Override
                    protected void configure() {
                        map().setFirstName(source.getPersonalInfoDto().getFirstName());
                        map().setLastName(source.getPersonalInfoDto().getLastName());
                    }
                };
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(clientDtoToDao);
        return modelMapper;
    }
}