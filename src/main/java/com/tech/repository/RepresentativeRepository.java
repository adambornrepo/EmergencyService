package com.tech.repository;

import com.tech.entites.concretes.Representative;
import org.springframework.stereotype.Repository;

@Repository
public interface RepresentativeRepository extends BaseEmployeeRepository<Representative,Long>{
}
