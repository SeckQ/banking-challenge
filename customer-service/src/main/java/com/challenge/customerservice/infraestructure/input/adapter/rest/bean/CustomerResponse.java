package com.challenge.customerservice.infraestructure.input.adapter.rest.bean;

import lombok.Data;

@Data
public class CustomerResponse {
    private Long id;
    private String identification;
    private String name;
    private String gender;
    private String address;
    private String phone;
    private String password;
    private Boolean status;
}
