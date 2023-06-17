package com.tech.repository;

import com.tech.entites.concretes.LabTechnician;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabTechnicianRepository extends BaseEmployeeRepository<LabTechnician,Long>{
    List<LabTechnician> findByIsDisabled(boolean isDisabled);
}
