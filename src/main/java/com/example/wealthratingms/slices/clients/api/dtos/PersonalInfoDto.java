package com.example.wealthratingms.slices.clients.api.dtos;

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
public class PersonalInfoDto {
    private String firstName;
    private String lastName;
    private String city;
}
