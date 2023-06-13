package com.tech.entites.concretes;

import com.tech.entites.abstracts.Employee;
import com.tech.entites.abstracts.MedicalEmployee;
import com.tech.entites.enums.Zone;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "nurse")
public class Nurse  extends MedicalEmployee implements Serializable {

    @Enumerated(EnumType.STRING)
    @Column(length = 16, nullable = false)
    private Zone zone;


    @PrePersist
    private void createdTime() {
        setCreatedAt(System.currentTimeMillis());
    }

    @PreUpdate
    private void updatedTime() {
        setUpdatedAt(System.currentTimeMillis());
    }

}
