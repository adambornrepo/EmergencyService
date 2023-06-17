package com.tech.repository;

import com.tech.entites.concretes.Procedure;
import com.tech.entites.enums.AppointmentStatus;
import com.tech.entites.enums.ProcedureStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcedureRepository extends JpaRepository<Procedure, Long> {
    List<Procedure> findByStatusAndCreatedAtLessThanAndAppliedNotNull(ProcedureStatus status, long createdAt);
    List<Procedure> findByStatusAndCreatedAtLessThanAndAppliedNull(ProcedureStatus status, long createdAt);
    List<Procedure> findByEmployee_IdAndStatus(Long id, ProcedureStatus status);
    Page<Procedure> findByEmployee_Id(Long id, PageRequest pageable);
    List<Procedure> findByAppointment_Id(Long id);
}
