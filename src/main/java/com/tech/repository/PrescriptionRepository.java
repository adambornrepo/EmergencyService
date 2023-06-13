package com.tech.repository;

import com.tech.entites.concretes.Prescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    Optional<Prescription> findByAppointment_Id(Long id);
    Page<Prescription> findByAppointment_Patient_Ssn(String ssn, PageRequest pageable);

    Page<Prescription> findByAppointment_Patient_Id(Long id, PageRequest pageable);

    Page<Prescription> findByDoctor_Id(Long doctorId, PageRequest pageable);
}
