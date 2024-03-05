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
public class GetAllUsersWithoutParticipationsResponse {
    private Long id;
    private String name;
    private int level;
    private int coin;
    @Enumerated(EnumType.STRING)
    private Country country;
}
