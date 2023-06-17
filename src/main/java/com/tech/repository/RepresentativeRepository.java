package com.tech.repository;

import com.tech.entites.concretes.Representative;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepresentativeRepository extends BaseEmployeeRepository<Representative,Long>{
    List<Representative> findByIsDisabled(boolean isDisabled);
}
