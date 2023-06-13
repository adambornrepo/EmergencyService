package com.tech.repository;

import com.tech.entites.concretes.Nurse;
import org.springframework.stereotype.Repository;

@Repository
public interface NurseRepository extends BaseEmployeeRepository<Nurse,Long>{
}
