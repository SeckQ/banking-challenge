package com.challenge.accountservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountStatement {
    private LocalDate date;
    private String clientName;
    private String accountNumber;
    private String accountType;
    private Double initialBalance;
    private Boolean status;
    private String movementType;
    private Double movementValue;
    private Double availableBalance;
}
