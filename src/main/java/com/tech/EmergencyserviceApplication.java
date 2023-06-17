package com.tech;

import com.tech.entites.concretes.Address;
import com.tech.entites.enums.Gender;
import com.tech.payload.request.register.SuperAdminRegistrationRequest;
import com.tech.payload.response.detailed.DetailedSuperAdminResponse;
import com.tech.service.SuperAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

@SpringBootApplication
@RequiredArgsConstructor
public class EmergencyserviceApplication implements CommandLineRunner {
    private final SuperAdminService superAdminService;

    public static void main(String[] args) {
        SpringApplication.run(EmergencyserviceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        if (!superAdminService.isExistsAtLeastOneSuperAdmin()) {
            var request = SuperAdminRegistrationRequest.builder()
                    .firstName("Adam")
                    .lastName("Born")
                    .ssn("123-45-6789")
                    .address(new Address("12312", "Main St", "NY", "NY"))
                    .birthDate(LocalDate.of(1991, 1, 1))
                    .gender(Gender.MALE)
                    .phoneNumber("+1-123-456-7890")
                    .password("super123")
                    .build();
            var saved = superAdminService.saveSuperAdmin(request);
        }

    }


}
