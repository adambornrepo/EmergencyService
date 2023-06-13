package com.tech.entites.concretes;

import com.tech.entites.abstracts.Employee;
import com.tech.entites.abstracts.MedicalEmployee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "procedure")
public class Procedure implements Serializable {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "procedure_generator"
    )
    @SequenceGenerator(
            name = "procedure_generator",
            sequenceName = "procedure_sequence",
            allocationSize = 1
    )
    private Long id;

    @Column(nullable = false, length = 150)
    private String applied;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @Column(nullable = false)
    private boolean isDisabled;

    private long createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private MedicalEmployee employee;

    @PrePersist
    public void creationTime() {
        setCreatedAt(System.currentTimeMillis());
    }

}
