package com.tech.repository;

import com.tech.entites.concretes.Nurse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NurseRepository extends BaseEmployeeRepository<Nurse,Long>{
    List<Nurse> findByIsDisabled(boolean isDisabled);
}
