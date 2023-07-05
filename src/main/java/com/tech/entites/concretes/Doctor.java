package com.tech.entites.concretes;

import com.tech.entites.abstracts.MedicalEmployee;
import com.tech.entites.enums.Zone;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "doctor")
public class Doctor extends MedicalEmployee implements Serializable {

    @Enumerated(EnumType.STRING)
    @Column(length = 16, nullable = false)
    private Zone zone;

    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY)
    private List<Prescription> prescription;

    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY)
    private List<Appointment> appointment;

    @PrePersist
    private void createdTime() {
        setCreatedAt(System.currentTimeMillis());
    }

    @PreUpdate
    private void updatedTime() {
        setUpdatedAt(System.currentTimeMillis());
    }

}
