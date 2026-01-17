package com.challenge.accountservice.infraestructure.output.repository.mapper;

import com.challenge.accountservice.domain.model.Account;
import com.challenge.accountservice.infraestructure.output.repository.entity.AccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapperRepository {

    @Mapping(source = "accountType", target = "accountType")
    Account toDomain(AccountEntity accountEntity);

    @Mapping(source = "accountType", target = "accountType")
    AccountEntity toEntity(Account account);
}
