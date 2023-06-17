package com.tech.repository;

import com.tech.entites.concretes.Admin;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository extends BaseEmployeeRepository<Admin,Long>{
    List<Admin> findByIsDisabled(boolean isDisabled);
}
