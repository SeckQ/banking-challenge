package com.challenge.customerservice.infraestructure.input.adapter.rest.bean;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerRequest {

    @NotBlank(message = "Identification is required")
    @Size(min = 10, max = 13, message = "Identification must be between 10 and 13 digits")
    private String identification;

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Gender is required")
    private String gender;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Phone is required")
    private String phone;

    @NotBlank(message = "Password is required")
    @Size(min = 4, message = "Password must be at least 4 characters")
    private String password;

    @NotNull(message = "Status is required")
    private Boolean status;
}
