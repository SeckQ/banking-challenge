package com.challenge.accountservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movement {

    private Long id;

    private LocalDate date;

    private MovementType movementType;

    private Double value;

    private Double balance;

    private Account account;
}
