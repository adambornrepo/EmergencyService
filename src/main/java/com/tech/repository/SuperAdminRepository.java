package com.tech.repository;

import com.tech.entites.concretes.SuperAdmin;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SuperAdminRepository extends BaseEmployeeRepository<SuperAdmin, Long> {
    @Query("SELECT (COUNT(s)=1) FROM SuperAdmin s WHERE s.isDisabled=false")
    boolean isLastSuperAdmin();
    @Query("SELECT (COUNT(s)>0) FROM SuperAdmin s WHERE s.isDisabled=false")
    boolean checkSuperAdminExistence();
}
