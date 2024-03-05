package com.dreamgames.backendengineeringcasestudy.dtos.responses;

import com.dreamgames.backendengineeringcasestudy.entities.Country;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserResponse {

    private Long id;
    private int level = 1;
    private int coins = 5000;
    @Enumerated(EnumType.STRING)
    private Country country;
}
