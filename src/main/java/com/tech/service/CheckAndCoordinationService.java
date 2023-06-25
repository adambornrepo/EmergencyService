package com.tech.service;

import com.tech.configuration.ApiMessages;
import com.tech.entites.abstracts.Employee;
import com.tech.entites.abstracts.MedicalEmployee;
import com.tech.entites.concretes.Appointment;
import com.tech.entites.concretes.Procedure;
import com.tech.entites.enums.AppointmentStatus;
import com.tech.entites.enums.ProcedureStatus;
import com.tech.exception.custom.ConflictException;
import com.tech.exception.custom.ResourceNotFoundException;
import com.tech.repository.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
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

    private final AppointmentRepository appointmentRepository;
    private final ProcedureRepository procedureRepository;

    private final ApiMessages apiMessages;

    @Value("${scheduler.certain.time.before}")
    private long certainTime;


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


    public Optional<? extends Employee> getOneEmployeeBySsn(String username) {
        var doctor = doctorRepository.findBySsnEquals(username);
        if (doctor.isPresent()) return doctor;
        var nurse = nurseRepository.findBySsnEquals(username);
        if (nurse.isPresent()) return nurse;
        var labTechnician = labTechnicianRepository.findBySsnEquals(username);
        if (labTechnician.isPresent()) return labTechnician;
        var radiologyTechnician = radiologyTechnicianRepository.findBySsnEquals(username);
        if (radiologyTechnician.isPresent()) return radiologyTechnician;
        var representative = representativeRepository.findBySsnEquals(username);
        if (representative.isPresent()) return representative;
        var admin = adminRepository.findBySsnEquals(username);
        if (admin.isPresent()) return admin;
        var superAdmin = superAdminRepository.findBySsnEquals(username);
        if (superAdmin.isPresent()) return superAdmin;
        return Optional.empty();
    }

    public Employee getOneEmployeeByUserDetails(UserDetails userDetails) {
        return getOneEmployeeBySsn(userDetails.getUsername()) // Username = SSN
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(apiMessages.getMessage("error.not.found.employee.unexpected"), userDetails.getUsername())
                ));
    }

    public void updateStatusForExpiredAppointmentsAndProcedures() {
        long timeRange = System.currentTimeMillis() - certainTime;

        List<Procedure> expiredProcedures = procedureRepository
                .findByStatusAndCreatedAtLessThanAndAppliedNull(ProcedureStatus.NOT_APPLIED, timeRange);
        for (Procedure expiredProcedure : expiredProcedures) {
            expiredProcedure.setStatus(ProcedureStatus.NOT_ARRIVED);
        }
        List<Procedure> notArrivedProcedures = procedureRepository.saveAll(expiredProcedures);
        log.info("Updated status to 'NOT ARRIVED' for {} expired procedures", notArrivedProcedures.size());

        List<Appointment> expiredAppointments = appointmentRepository
                .findByStatusAndCreatedAtLessThan(AppointmentStatus.IN_PROGRESS, timeRange);
        for (Appointment expiredAppointment : expiredAppointments) {
            expiredAppointment.setStatus(AppointmentStatus.AUTO_CLOSED);
        }
        List<Appointment> notArrivedAppointments = appointmentRepository.saveAll(expiredAppointments);
        log.info("Updated status to 'AUTO CLOSED' for {} expired appointments", notArrivedAppointments.size());

    }

    public void updateStatusForAppliedProcedures() {
        long timeRange = System.currentTimeMillis() - certainTime;

        List<Procedure> appliedProcedures = procedureRepository
                .findByStatusAndCreatedAtLessThanAndAppliedNotNull(ProcedureStatus.NOT_APPLIED, timeRange);
        for (Procedure appliedProcedure : appliedProcedures) {
            appliedProcedure.setStatus(ProcedureStatus.APPLIED);
        }
        List<Procedure> completedProcedures = procedureRepository.saveAll(appliedProcedures);
        log.info("Status updated to 'APPLIED' for {} procedures with a status of 'NOT APPLIED' even though the action has been taken", completedProcedures.size());
    }


    public List<Employee> getAllActiveEmployeeList() {
        List<Employee> employees = new ArrayList<>();
        for (BaseEmployeeRepository<?, ?> repository : employeeRepositories) {
            employees.addAll(repository.findByIsDisabledOrderByIdAsc(false));
        }
        return employees;
    }

}
