package com.tech.service;

import com.tech.configuration.ApiMessages;
import com.tech.entites.abstracts.MedicalEmployee;
import com.tech.entites.concretes.Doctor;
import com.tech.entites.concretes.Nurse;
import com.tech.exception.custom.ConflictException;
import com.tech.repository.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CheckAndCoordinationService {

    private List<BaseEmployeeRepository<?, ?>> employeeRepositories;

    private final SuperAdminRepository superAdminRepository;
    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final NurseRepository nurseRepository;
    private final LabTechnicianRepository labTechnicianRepository;
    private final RadiologyTechnicianRepository radiologyTechnicianRepository;
    private final RepresentativeRepository representativeRepository;

    private final ApiMessages apiMessages;


    @PostConstruct
    private void init() {
        employeeRepositories = new ArrayList<>();

        employeeRepositories.add(superAdminRepository);
        employeeRepositories.add(adminRepository);
        employeeRepositories.add(doctorRepository);
        employeeRepositories.add(nurseRepository);
        employeeRepositories.add(labTechnicianRepository);
        employeeRepositories.add(radiologyTechnicianRepository);
        employeeRepositories.add(representativeRepository);

    }


    public void checkDuplicate(String ssn, String phoneNumber) {
        if (StringUtils.hasText(ssn)) {
            for (BaseEmployeeRepository<?, ?> repository : employeeRepositories) {
                if (repository.existsBySsn(ssn)) {
                    throw new ConflictException(String.format(apiMessages.getMessage("error.conflict.ssn"), ssn));
                }
            }
        }
        if (StringUtils.hasText(phoneNumber)) {
            for (BaseEmployeeRepository<?, ?> repository : employeeRepositories) {
                if (repository.existsByPhoneNumber(phoneNumber)) {
                    throw new ConflictException(String.format(apiMessages.getMessage("error.conflict.phone"), phoneNumber));
                }
            }
        }

    }

    public MedicalEmployee getOneMedicalEmployeeById(Long id) {
        var doctor = doctorRepository.findById(id).orElse(null);
        if (doctor != null) return doctor;
        var nurse = nurseRepository.findById(id).orElse(null);
        if (nurse != null) return nurse;
        var labTechnician = labTechnicianRepository.findById(id).orElse(null);
        if (labTechnician != null) return labTechnician;
        var radiologyTechnician = radiologyTechnicianRepository.findById(id).orElse(null);
        if (radiologyTechnician != null) return radiologyTechnician;
        return null;
    }


}
