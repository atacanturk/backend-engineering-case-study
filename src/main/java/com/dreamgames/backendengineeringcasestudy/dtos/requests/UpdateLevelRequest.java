package com.dreamgames.backendengineeringcasestudy.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateLevelRequest {
    @NotBlank(message = "Id must be given!")
    private Long id;
}
