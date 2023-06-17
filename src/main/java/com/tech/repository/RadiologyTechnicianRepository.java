package com.tech.repository;

import com.tech.entites.concretes.RadiologyTechnician;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RadiologyTechnicianRepository extends BaseEmployeeRepository<RadiologyTechnician,Long>{
    List<RadiologyTechnician> findByIsDisabled(boolean isDisabled);
}
