package com.challenge.accountservice.infraestructure.input.adapter.rest.bean;

import com.challenge.accountservice.domain.model.MovementType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovementDTO {
    private Long id;
    private LocalDate date;

    @NotNull(message = "Movement Type is mandatory")
    private MovementType movementType;

    @NotNull(message = "Movement value is mandatory")
    private Double value;

    private Double balance;

    @NotBlank(message = "Account number is mandatory")
    private String accountNumber;
}
