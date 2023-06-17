package com.tech.security.service;

import com.tech.configuration.ApiMessages;
import com.tech.entites.abstracts.Employee;
import com.tech.exception.custom.ResourceNotFoundException;
import com.tech.payload.request.LoginRequest;
import com.tech.payload.response.LoginResponse;
import com.tech.security.token.Token;
import com.tech.security.token.TokenRepository;
import com.tech.service.CheckAndCoordinationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final CheckAndCoordinationService coordinationService;
    private final TokenRepository tokenRepository;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final ApiMessages apiMessages;

    @Transactional
    public LoginResponse authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getSsn(),
                        request.getPassword()
                )
        );
        var employee = coordinationService
                .getOneEmployeeBySsn(request.getSsn())
                .orElseThrow(() -> new ResourceNotFoundException(apiMessages.getMessage("error.not.found.employee")));
        var jwtToken = tokenService.generateToken(employee);

        revokeAllTokensByEmployeeId(employee.getId());
        saveToken(employee, jwtToken);
        return LoginResponse.builder()
                .ssn(employee.getSsn())
                .token(jwtToken)
                .build();
    }

    private <T extends Employee> void saveToken(T employee, String jwtToken) {
        Token token = Token.builder()
                .employee(employee)
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllTokensByEmployeeId(Long employeeId) {
        var validTokens = tokenRepository.findByEmployee_IdAndRevokedFalseOrExpiredFalse(employeeId);
        if (validTokens.isEmpty()) return;
        validTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validTokens);
    }


}
