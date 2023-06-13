package com.tech;

import com.tech.entites.concretes.Address;
import com.tech.entites.concretes.Doctor;
import com.tech.entites.concretes.Nurse;
import com.tech.entites.concretes.Procedure;
import com.tech.entites.enums.Gender;
import com.tech.entites.enums.Zone;
import com.tech.payload.request.register.DoctorRegistrationRequest;
import com.tech.payload.response.detailed.DetailedDoctorResponse;
import com.tech.repository.NurseRepository;
import com.tech.repository.ProcedureRepository;
import com.tech.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

@SpringBootApplication
@RequiredArgsConstructor
public class EmergencyserviceApplication implements CommandLineRunner {
    private final DoctorService doctorService;
    private final NurseRepository nurseRepository;
    private final ProcedureRepository procedureRepository;

    public static void main(String[] args) {
        SpringApplication.run(EmergencyserviceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        var doctor = DoctorRegistrationRequest.builder()
//                .firstName("Adam")
//                .lastName("Born")
//                .gender(Gender.MALE)
//                .birthDate(LocalDate.of(2000, 1, 1))
//                .ssn("44411199444")
//                .phoneNumber("+9054112322222")
//                .address(
//                        Address.builder()
//                                .city("NY")
//                                .country("US")
//                                .street("Main 123")
//                                .zipCode("12312")
//                                .build()
//                )
//                .zone(Zone.GREEN)
//                .password("123456789")
//                .build();
//
//        var saved = doctorService.saveDoctor(doctor);
//        Doctor found = doctorService.getOneDoctorById(1L);
//
//        Procedure pros = Procedure.builder()
//                .applied("Bu yapildi")
//                .createdAt(System.currentTimeMillis())
//                .employee(found)
//                .build();
//
//        procedureRepository.save(pros);
//        // TODO: 5.06.2023 Add
    }


}
