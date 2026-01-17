package com.challenge.customerservice.infraestructure.output.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer")
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean status;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "identification", referencedColumnName = "identification")
    private PersonEntity person;
}
