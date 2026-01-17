package com.challenge.accountservice.infraestructure.input.adapter.rest.bean;

import com.challenge.accountservice.domain.model.AccountStatus;
import com.challenge.accountservice.domain.model.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequestDTO {

    @NotBlank(message = "Number account cannot be empty")
    private String accountNumber;

    @NotNull(message = "Account type is mandatory")
    private AccountType accountType;

    @NotNull(message = "Initial balance is mandatory")
    @PositiveOrZero(message = "Initial balance cannot be negative")
    private Double initialBalance;

    @NotNull(message = "Status is mandatory")
    private AccountStatus status;

    @NotNull(message = "Client ID is mandatory")
    @Positive(message = "Client ID must be greater than zero")
    private Long clientId;
}
