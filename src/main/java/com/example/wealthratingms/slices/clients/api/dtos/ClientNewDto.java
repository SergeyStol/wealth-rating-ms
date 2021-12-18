package com.example.wealthratingms.slices.clients.api.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Sergey Stol
 * 2021-12-17
 */
@Data
@Builder
public class ClientNewDto {
    private Long id;
    @JsonAlias("personalInfo")
    @JsonProperty("personalInfo")
    private PersonalInfoDto personalInfoDto;
    @JsonAlias("financialInfo")
    @JsonProperty("financialInfo")
    private FinancialInfoDto financialInfoDto;

    @JsonIgnore
    public String getClientCity() {
        return personalInfoDto.getCity();
    }

    @JsonIgnore
    public long getCash() {
        return financialInfoDto.getCash();
    }

    @JsonIgnore
    public int getNumberOfAssets() {
        return financialInfoDto.getNumberOfAssets();
    }
}