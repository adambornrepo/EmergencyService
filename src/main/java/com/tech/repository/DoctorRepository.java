package com.tech.repository;

import com.tech.entites.concretes.Doctor;
import com.tech.entites.enums.Zone;
import com.tech.security.role.Role;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends BaseEmployeeRepository<Doctor,Long>{
    List<Doctor> findByIsDisabledOrderByFirstNameAsc(boolean isDisabled);
    List<Doctor> findByIsDisabledAndZoneOrderByZoneAscFirstNameAsc(boolean isDisabled, Zone zone);
    Optional<Doctor> findByRole(Role role);


}
