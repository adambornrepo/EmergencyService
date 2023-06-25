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
        Long doctorId = doctor == null ? null : doctor.getId();
        Long appointmentId = appointment == null ? null : appointment.getId();
        return "Prescription{ " +
                "Id= " + id +
                ", Medicines={ " + medicines + " }" +
                ", Doctor Id= " + doctorId +
                ", Appointment Id= " + appointmentId +
                ", CreatedAt= " + createdAt +
                '}';
    }
}
