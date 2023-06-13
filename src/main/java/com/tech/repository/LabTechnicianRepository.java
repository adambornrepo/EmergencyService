package com.tech.repository;

import com.tech.entites.concretes.LabTechnician;
import org.springframework.stereotype.Repository;

@Repository
public interface LabTechnicianRepository extends BaseEmployeeRepository<LabTechnician,Long>{
}
