package com.dreamgames.backendengineeringcasestudy.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateLevelResponse {
    private int level;
    private int coin;
}
