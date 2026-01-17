package com.challenge.accountservice.infraestructure.output.repository.entity;

import com.challenge.accountservice.domain.model.AccountStatus;
import com.challenge.accountservice.domain.model.AccountType;
import com.challenge.accountservice.domain.model.MovementType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "account")
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    private Double initialBalance;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    private Long clientId;

}
