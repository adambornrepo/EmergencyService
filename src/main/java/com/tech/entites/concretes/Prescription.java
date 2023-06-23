package com.tech.entites.concretes;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "prescription")
public class Prescription implements Serializable {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "prescription_generator"
    )
    @SequenceGenerator(
            name = "prescription_generator",
            sequenceName = "prescription_sequence",
            allocationSize = 1
    )
    private Long id;

    private String medicines;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    private long createdAt;

    @PrePersist
    public void creationTime() {
        setCreatedAt(System.currentTimeMillis());
    }

    @Override
    public String toString() {
        return "Prescription{ " +
                "Id= " + id +
                ", Medicines={ " + medicines + " }" +
                ", Doctor Id= " + doctor.getId() +
                ", Appointment Id= " + appointment.getId() +
                ", CreatedAt= " + createdAt +
                '}';
    }
}
