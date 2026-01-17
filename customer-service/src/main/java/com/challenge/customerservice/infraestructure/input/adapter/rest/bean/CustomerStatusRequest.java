package com.challenge.customerservice.infraestructure.input.adapter.rest.bean;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CustomerStatusRequest {

    @NotNull(message = "Status is required")
    private Boolean status;
}
