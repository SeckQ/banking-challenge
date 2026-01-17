package com.challenge.customerservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {

    private Long id;

    private String identification;

    private String name;

    private Gender gender;

    private String address;

    private String phone;
}
