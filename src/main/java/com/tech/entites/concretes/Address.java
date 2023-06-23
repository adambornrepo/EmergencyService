package com.tech.entites.concretes;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address implements Serializable {

    @Size(min = 2, max = 100,message = "{validation.size.field}")
    @Column(length = 100)
    private String street;

    @Size(min = 2, max = 50,message = "{validation.size.field}")
    @Column(length = 50)
    private String city;

    @Size(min = 2, max = 50,message = "{validation.size.field}")
    @Column(length = 50)
    private String state;

    @Size(min = 3, max = 16,message ="{validation.size.field}" )
    @Column(length = 16)
    private String zipCode;

    @Override
    public String toString() {
        return "{ " +
                "Street= " + street +
                ", City= " + city +
                ", State= " + state +
                ", ZipCode= " + zipCode +
                " }";
    }
}
