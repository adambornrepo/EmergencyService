package com.tech.security.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    @Query(value = "SELECT t FROM Token t WHERE t.employee.Id = :employeeId AND (t.revoked = false OR t.expired = false)")
    List<Token> findByEmployee_IdAndRevokedFalseOrExpiredFalse(@Param(value = "employeeId") Long id);

    Optional<Token> findByToken(String token);
}
