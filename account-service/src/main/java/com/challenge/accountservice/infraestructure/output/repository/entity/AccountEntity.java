package com.challenge.accountservice.infraestructure.output.repository.entity;

import com.challenge.accountservice.domain.model.AccountType;
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

    private Boolean status;

    private Long clientId;

}
