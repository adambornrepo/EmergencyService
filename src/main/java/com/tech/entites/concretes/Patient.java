package com.tech.entites.concretes;

import com.tech.entites.abstracts.Person;
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
@Table(name = "patient")
public class Patient extends Person implements Serializable {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "patient_generator"
    )
    @SequenceGenerator(
            name = "patient_generator",
            sequenceName = "patient_sequence",
            allocationSize = 1
    )
    private Long id;

    @Column(unique = true, length = 100)
    private String email;

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY)
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
