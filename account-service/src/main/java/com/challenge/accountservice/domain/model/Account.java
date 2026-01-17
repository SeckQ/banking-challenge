package com.challenge.accountservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    private Long id;

    private String accountNumber;

    private AccountType accountType;

    private Double initialBalance;

    private AccountStatus status;

    private Long clientId;
}
