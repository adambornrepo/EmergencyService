package com.tech.repository;

import com.tech.entites.concretes.Appointment;
import com.tech.entites.enums.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByStatusAndCreatedAtLessThanAndProceduresNullAndPrescriptionNull(AppointmentStatus status, long createdAt);

    @Query("SELECT a FROM Appointment a WHERE a.status = :status AND a.createdAt < :createdAt AND (a.procedures IS NOT NULL OR a.prescription IS NOT NULL)")
    List<Appointment> findByStatusAndCreatedAtLessThanAndProceduresNotNullORPrescriptionNotNull(
            @Param("status") AppointmentStatus appointmentStatus,
            @Param("createdAt") long aDayBefore
    );

    Page<Appointment> findAppointmentByPatient_Ssn(String ssn, Pageable pageable);

    Page<Appointment> findByAppointmentDate(LocalDate appointmentDate, Pageable pageable);

    Page<Appointment> findByDoctor_IdAndStatus(Long doctorId, AppointmentStatus status, Pageable pageable);

    Page<Appointment> findByDoctor_Id(Long doctorId, Pageable pageable);



}
