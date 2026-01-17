package com.challenge.accountservice.infraestructure.input.adapter.rest.mapper;

import com.challenge.accountservice.domain.model.Account;
import com.challenge.accountservice.domain.model.AccountStatement;
import com.challenge.accountservice.domain.model.AccountType;
import com.challenge.accountservice.domain.model.Movement;
import com.challenge.accountservice.infraestructure.input.adapter.rest.bean.AccountRequestDTO;
import com.challenge.accountservice.infraestructure.input.adapter.rest.bean.AccountResponseDTO;
import com.challenge.accountservice.infraestructure.input.adapter.rest.bean.AccountStatementDTO;
import com.challenge.accountservice.infraestructure.input.adapter.rest.bean.MovementDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapperRest {

    @Mapping(target = "id", ignore = true)
    Account toDomain(AccountRequestDTO requestDTO);

    @Mapping(source = "clientName", target = "clientName")
    AccountResponseDTO toResponseDTO(Account account, String clientName);

    @Mapping(source = "accountNumber", target = "account.accountNumber")
    Movement toDomain(MovementDTO dto);

    @Mapping(source = "account.accountNumber", target = "accountNumber")
    MovementDTO toDto(Movement movement);

    AccountStatementDTO toDtoFromReport(AccountStatement report);

    default String map(AccountType accountType) {
        return accountType.name();
    }
}
