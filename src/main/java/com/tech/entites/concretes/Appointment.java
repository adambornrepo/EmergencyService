package com.tech.entites.concretes;

import com.tech.entites.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "appointment")
public class Appointment implements Serializable {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "appointment_generator"
    )
    @SequenceGenerator(
            name = "appointment_generator",
            sequenceName = "appointment_sequence",
            allocationSize = 1
    )
    private Long id;

    @Column(length = 100)
    private String symptoms;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private AppointmentStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "representative_id")
    private Representative representative;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    private LocalDate appointmentDate;

    @OneToMany(mappedBy = "appointment", fetch = FetchType.LAZY)
    private List<Procedure> procedures;

    @OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL)
    private Prescription prescription;

    @Column(nullable = false)
    private boolean isDisabled;

    private long createdAt;

    @PrePersist
    public void creationTime() {
        setCreatedAt(System.currentTimeMillis());
    }

    @Override
    public String toString() {
        return "Appointment{ " +
                "Id= " + id +
                ", Symptoms= " + symptoms +
                ", Status= " + status +
                ", Representative Id= " + representative.getId() +
                ", Doctor Id= " + doctor.getId() +
                ", Patient SSN= " + patient.getSsn() +
                ", AppointmentDate= " + appointmentDate +
                ", Prescription Id= " + prescription.getId() +
                ", IsDisabled= " + isDisabled +
                ", CreatedAt= " + createdAt +
                " }";
    }
}
