package com.tech.entites.abstracts;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Getter
@Setter
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public abstract class Employee extends Person implements Serializable {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "employee_generator"
    )
    @SequenceGenerator(
            name = "employee_generator",
            sequenceName = "employee_sequence",
            allocationSize = 1
    )
    private Long id;

    @Column(nullable = false, length = 50)
    private String password;

    @Column(nullable = false)
    private boolean isDisabled;




}
