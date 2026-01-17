package com.challenge.accountservice.infraestructure.output.repository.mapper;

import com.challenge.accountservice.domain.model.Movement;
import com.challenge.accountservice.infraestructure.output.repository.entity.MovementEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MovementMapperRepository {

    @Mapping(source = "movementType", target = "movementType")
    @Mapping(source = "account", target = "account")
    Movement toDomain(MovementEntity entity);

    @Mapping(source = "movementType", target = "movementType")
    @Mapping(source = "account", target = "account")
    MovementEntity toEntity(Movement domain);
}
