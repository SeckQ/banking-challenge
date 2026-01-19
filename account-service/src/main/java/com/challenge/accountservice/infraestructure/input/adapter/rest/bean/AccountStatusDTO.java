package com.challenge.accountservice.infraestructure.input.adapter.rest.bean;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountStatusDTO {
    
    @NotNull(message = "Status is mandatory")
    private Boolean status;
}
