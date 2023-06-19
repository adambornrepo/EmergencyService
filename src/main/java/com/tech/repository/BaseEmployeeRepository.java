package com.tech.repository;

import com.tech.entites.abstracts.Employee;
import com.tech.entites.concretes.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseEmployeeRepository<T extends Employee, ID> extends JpaRepository<T, ID> {
    boolean existsBySsn(String ssn);
    boolean existsByPhoneNumber(String phoneNumber);

    Optional<T> findByPhoneNumberEquals(String phoneNumber);

    Optional<T> findBySsnEquals(String ssn);
    List<T> findByIsDisabledOrderByIdAsc(boolean isDisabled);


}
