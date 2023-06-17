package com.tech.repository;

import com.tech.entites.concretes.Doctor;
import com.tech.security.role.Role;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends BaseEmployeeRepository<Doctor,Long>{
    List<Doctor> findByIsDisabled(boolean isDisabled);
    Optional<Doctor> findByRole(Role role);


}
