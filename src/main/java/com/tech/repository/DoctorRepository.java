package com.tech.repository;

import com.tech.entites.concretes.Doctor;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends BaseEmployeeRepository<Doctor,Long>{

}
