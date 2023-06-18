package com.tech.repository;

import com.tech.entites.concretes.Nurse;
import com.tech.entites.enums.Zone;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NurseRepository extends BaseEmployeeRepository<Nurse,Long>{
    List<Nurse> findByIsDisabledAndZoneOrderByZoneAscFirstNameAsc(boolean isDisabled, Zone zone);
    List<Nurse> findByIsDisabledOrderByFirstNameAsc(boolean isDisabled);
}
