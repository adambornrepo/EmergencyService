package com.tech.entites.concretes;

import com.tech.entites.abstracts.Employee;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "admin")
public class Admin extends Employee implements Serializable {

    @PrePersist
    private void createdTime() {
        setCreatedAt(System.currentTimeMillis());
    }

    @PreUpdate
    private void updatedTime() {
        setUpdatedAt(System.currentTimeMillis());
    }


}
