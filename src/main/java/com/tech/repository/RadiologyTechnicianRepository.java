package com.tech.repository;

import com.tech.entites.concretes.RadiologyTechnician;
import org.springframework.stereotype.Repository;

@Repository
public interface RadiologyTechnicianRepository extends BaseEmployeeRepository<RadiologyTechnician,Long>{
}
