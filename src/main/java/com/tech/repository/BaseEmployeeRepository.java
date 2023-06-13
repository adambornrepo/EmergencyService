package com.tech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface BaseEmployeeRepository<T, ID> extends JpaRepository<T, ID> {
    boolean existsBySsn(String ssn);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<T> findByPhoneNumberEquals(String phoneNumber);

    Optional<T> findBySsnEquals(String ssn);


}
