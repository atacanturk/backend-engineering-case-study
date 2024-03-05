package com.dreamgames.backendengineeringcasestudy.dtos.responses;

import com.dreamgames.backendengineeringcasestudy.entities.Country;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupLeaderboardResponse {

    private Long id;

    private String userName;

    @Enumerated(EnumType.STRING)
    private Country country;

    private int score;
}
