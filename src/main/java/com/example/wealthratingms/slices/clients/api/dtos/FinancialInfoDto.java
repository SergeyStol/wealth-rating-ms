package com.example.wealthratingms.slices.clients.api.dtos;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Sergey Stol
 * 2021-12-17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinancialInfoDto {
    private long cash;
    private int numberOfAssets;
}
