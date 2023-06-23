package com.tech.entites.abstracts;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tech.entites.concretes.Address;
import com.tech.entites.enums.Gender;
import com.tech.security.role.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@SuperBuilder
public abstract class Person implements Serializable {

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @JsonFormat(
            pattern = "yyyy-MM-dd",
            shape = JsonFormat.Shape.STRING
    )
    @Column(nullable = false)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 6)
    private Gender gender;

    @Column(
            nullable = false,
            unique = true,
            length = 40
    )
    private String ssn;

    @Column(
            nullable = false,
            length = 50,
            unique = true
    )
    private String phoneNumber;

    @Embedded
    private Address address;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private Role role;

    @Column(nullable = false)
    private long createdAt;

    private long updatedAt;

    @Override
    public String toString() {
        return "{ " +
                "FirstName= " + firstName +
                ", LastName= " + lastName +
                ", BirthDate= " + birthDate +
                ", Gender= " + gender +
                ", Ssn= " + ssn +
                ", PhoneNumber= " + phoneNumber +
                ", Address= " + address +
                ", Role= " + role +
                ", CreatedAt= " + createdAt +
                ", UpdatedAt= " + updatedAt +
                 " }";
    }
}
