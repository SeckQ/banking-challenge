package com.challenge.accountservice.infraestructure.input.adapter.rest.bean;

import com.challenge.accountservice.domain.model.AccountStatus;
import com.challenge.accountservice.domain.model.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponseDTO {
    private Long id;
    private String accountNumber;
    private AccountType accountType;
    private Double initialBalance;
    private AccountStatus status;
    private String clientName;
}
