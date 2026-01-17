package com.challenge.accountservice.infraestructure.input.adapter.rest.bean;

import com.challenge.accountservice.domain.model.AccountStatus;
import com.challenge.accountservice.domain.model.AccountType;
import com.challenge.accountservice.domain.model.MovementType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountStatementDTO {
    private LocalDate date;
    private String clientName;
    private String accountNumber;
    private AccountType accountType;
    private Double initialBalance;
    private AccountStatus status;
    private MovementType movementType;
    private Double movementValue;
    private Double availableBalance;
}