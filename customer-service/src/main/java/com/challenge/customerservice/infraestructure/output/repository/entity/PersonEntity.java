package com.challenge.customerservice.infraestructure.output.repository.entity;

import com.challenge.customerservice.domain.model.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "person")
@Inheritance(strategy = InheritanceType.JOINED)
public class PersonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String identification;

    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String address;

    private String phone;
}
