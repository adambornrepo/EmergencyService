package com.tech.repository;

import com.tech.entites.concretes.Admin;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends BaseEmployeeRepository<Admin,Long>{
}
