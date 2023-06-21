package com.tech.repository;

import com.tech.entites.concretes.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    boolean existsByEmail(String email);
    Optional<Patient> findByPhoneNumberEquals(String phoneNumber);

    Optional<Patient> findBySsnEquals(String ssn);

    boolean existsBySsn(String ssn);

    boolean existsByPhoneNumber(String phoneNumber);
}
