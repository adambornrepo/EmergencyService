package com.tech.repository;

import com.tech.entites.concretes.Appointment;
import com.tech.entites.enums.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Page<Appointment> findAppointmentByPatient_Ssn(String ssn, Pageable pageable);

    Page<Appointment> findByAppointmentDate(LocalDate appointmentDate, Pageable pageable);

    Page<Appointment> findByDoctor_IdAndStatus(Long doctorId, AppointmentStatus status, Pageable pageable);

    Page<Appointment> findByDoctor_Id(Long doctorId, Pageable pageable);
}
