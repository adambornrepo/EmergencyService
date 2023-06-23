package com.tech.service;

import com.tech.configuration.ApiMessages;
import com.tech.entites.abstracts.Employee;
import com.tech.entites.concretes.Appointment;
import com.tech.entites.concretes.Doctor;
import com.tech.entites.concretes.Patient;
import com.tech.entites.concretes.Representative;
import com.tech.entites.enums.AppointmentStatus;
import com.tech.exception.custom.ForbiddenAccessException;
import com.tech.exception.custom.ResourceNotFoundException;
import com.tech.exception.custom.UnsuitableRequestException;
import com.tech.mapper.AppointmentMapper;
import com.tech.payload.request.AppointmentCreationRequest;
import com.tech.payload.request.update.AppointmentUpdateRequest;
import com.tech.payload.resource.AppointmentExcelResource;
import com.tech.payload.response.ApiResponse;
import com.tech.payload.response.detailed.DetailedAppointmentResponse;
import com.tech.payload.response.simple.SimpleAppointmentResponse;
import com.tech.repository.AppointmentRepository;
import com.tech.security.role.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final CheckAndCoordinationService coordinationService;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final RepresentativeService representativeService;
    private final AppointmentMapper appointmentMapper;
    private final ApiMessages apiMessages;
    private final ExcelWriteService excelWriteService;


    public ResponseEntity<SimpleAppointmentResponse> saveAppointment(AppointmentCreationRequest request) {
        Representative foundPSR = representativeService.getOneRepresentativeById(request.getRepresentativeId());
        Doctor foundDoctor = doctorService.getOneDoctorById(request.getDoctorId());
        Patient foundPatient = patientService.getOnePatientBySsn(request.getPatientSsn());
        var appointment = Appointment.builder()
                .representative(foundPSR)
                .doctor(foundDoctor)
                .patient(foundPatient)
                .symptoms(request.getSymptoms())
                .status(AppointmentStatus.IN_PROGRESS)
                .appointmentDate(LocalDate.now())
                .isDisabled(false)
                .build();
        Appointment saved = appointmentRepository.save(appointment);
        return new ResponseEntity<>(appointmentMapper.buildSimpleAppointmentResponse(saved), HttpStatus.CREATED);
    }


    public ResponseEntity<DetailedAppointmentResponse> updateAppointment(AppointmentUpdateRequest request, Long id) {
        Appointment updateAppointment = getOneAppointmentById(id);
        if (updateAppointment.isDisabled()) {
            throw new ResourceNotFoundException(String.format(apiMessages.getMessage("error.not.exists.id"), id));
        }
        if (!updateAppointment.getStatus().equals(AppointmentStatus.IN_PROGRESS)) {
            throw new UnsuitableRequestException(
                    String.format(apiMessages.getMessage("error.no.action.appointment.status"), updateAppointment.getStatus())
            );
        }
        if (updateAppointment.getProcedures() != null || updateAppointment.getPrescription() != null) {
            throw new UnsuitableRequestException(apiMessages.getMessage("error.no.action.appointment.change"));
        }

        if (request.getDoctorId() != null) {
            Doctor foundDoctor = doctorService.getOneDoctorById(request.getDoctorId());
            updateAppointment.setDoctor(foundDoctor);
        }

        if (StringUtils.hasText(request.getSymptoms())) {
            updateAppointment.setSymptoms(request.getSymptoms());
        }
        Appointment updated = appointmentRepository.save(updateAppointment);
        return new ResponseEntity<>(appointmentMapper.buildDetailedAppointmentResponse(updated), HttpStatus.ACCEPTED);
    }

    public ResponseEntity<ApiResponse> cancelAppointment(Long id) {
        Appointment cancel = getOneAppointmentById(id);
        if (cancel.isDisabled()) {
            throw new ResourceNotFoundException(String.format(apiMessages.getMessage("error.not.exists.id"), id));
        }
        if (!cancel.getStatus().equals(AppointmentStatus.IN_PROGRESS)) {
            throw new UnsuitableRequestException(
                    String.format(apiMessages.getMessage("error.no.action.appointment.status"), cancel.getStatus())
            );
        }
        if (cancel.getProcedures() != null || cancel.getPrescription() != null) {
            throw new UnsuitableRequestException(apiMessages.getMessage("error.no.action.appointment.change"));
        }
        cancel.setStatus(AppointmentStatus.CANCELED);

        Appointment canceled = appointmentRepository.save(cancel);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message(apiMessages.getMessage("success.appointment.cancel"))
                        .build()
        );
    }

    public ResponseEntity<ApiResponse> completeAppointment(Long id, UserDetails userDetails) {
        Appointment complete = getOneAppointmentById(id);
        Employee employee = coordinationService.getOneEmployeeByUserDetails(userDetails);

        if ((employee.getRole().equals(Role.DOCTOR) || employee.getRole().equals(Role.CHIEF)) &&
                !employee.getId().equals(complete.getDoctor().getId())
        ) {
            throw new ForbiddenAccessException(apiMessages.getMessage("error.forbidden.doctor.complete"));
        }

        if (complete.isDisabled()) {
            throw new ResourceNotFoundException(String.format(apiMessages.getMessage("error.not.exists.id"), id));
        }
        if (!complete.getStatus().equals(AppointmentStatus.IN_PROGRESS)) {
            throw new UnsuitableRequestException(
                    String.format(apiMessages.getMessage("error.no.action.appointment.status"), complete.getStatus())
            );
        }
        complete.setStatus(AppointmentStatus.COMPLETED);

        Appointment completed = appointmentRepository.save(complete);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message(apiMessages.getMessage("success.appointment.complete"))
                        .build()
        );
    }

    public Appointment getOneAppointmentById(Long appointmentId) {
        return appointmentRepository
                .findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(apiMessages.getMessage("error.not.found.appointment.id"), appointmentId)
                ));
    }


    public ResponseEntity<ApiResponse> deleteAppointment(Long id) {
        Appointment foundAppointment = getOneAppointmentById(id);
        if (foundAppointment.isDisabled()) {
            throw new ResourceNotFoundException(String.format(apiMessages.getMessage("error.not.exists.id"), id));
        }
        if (!foundAppointment.getStatus().equals(AppointmentStatus.IN_PROGRESS)) {
            throw new UnsuitableRequestException(
                    String.format(apiMessages.getMessage("error.no.action.appointment.status"), foundAppointment.getStatus())
            );
        }
        if (foundAppointment.getProcedures() != null || foundAppointment.getPrescription() != null) {
            throw new UnsuitableRequestException(apiMessages.getMessage("error.no.action.appointment.change"));
        }

        foundAppointment.setDoctor(null);
        foundAppointment.setStatus(AppointmentStatus.DELETED);
        foundAppointment.setDisabled(true);

        Appointment deleted = appointmentRepository.save(foundAppointment);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message(apiMessages.getMessage("success.appointment.delete"))
                        .build()
        );
    }


    public ResponseEntity<DetailedAppointmentResponse> getOneDetailedAppointmentById(Long id) {
        return new ResponseEntity<>(appointmentMapper.buildDetailedAppointmentResponse(getOneAppointmentById(id)), HttpStatus.OK);
    }


    public Page<SimpleAppointmentResponse> getAllAppointment(Pageable pageable) {
        return appointmentRepository
                .findAll(pageable)
                .map(appointmentMapper::buildSimpleAppointmentResponse);
    }

    public Page<SimpleAppointmentResponse> getAllAppointmentByDoctorId(Long doctorId, Pageable pageable) {
        return appointmentRepository
                .findByDoctor_Id(doctorId, pageable)
                .map(appointmentMapper::buildSimpleAppointmentResponse);
    }


    public Page<SimpleAppointmentResponse> getAllInProgressAppointmentByDoctorId(Long doctorId, Pageable pageable) {
        return appointmentRepository
                .findByDoctor_IdAndStatus(doctorId, AppointmentStatus.IN_PROGRESS, pageable)
                .map(appointmentMapper::buildSimpleAppointmentResponse);
    }

    public Page<SimpleAppointmentResponse> getAllAppointmentByPatientSsn(String patientSsn, Pageable pageable) {
        return appointmentRepository
                .findAppointmentByPatient_Ssn(patientSsn, pageable)
                .map(appointmentMapper::buildSimpleAppointmentResponse);
    }

    public Page<SimpleAppointmentResponse> getAllAppointmentByDate(LocalDate date, Pageable pageable) {
        return appointmentRepository
                .findByAppointmentDate(date, pageable)
                .map(appointmentMapper::buildSimpleAppointmentResponse);
    }

    public List<SimpleAppointmentResponse> getAllInProgressAppointmentListByDoctorId(Long doctorId) {
        return appointmentRepository
                .findByDoctor_IdAndStatusOrderByCreatedAtAsc(doctorId, AppointmentStatus.IN_PROGRESS)
                .stream()
                .map(appointmentMapper::buildSimpleAppointmentResponse)
                .collect(Collectors.toList());
    }

    private List<AppointmentExcelResource> getAllInProgressAppointmentListByDoctorIdForExport(Long doctorId) {
        return appointmentRepository
                .findByDoctor_IdAndStatusOrderByCreatedAtAsc(doctorId, AppointmentStatus.IN_PROGRESS)
                .stream()
                .map(appointmentMapper::buildAppointmentExcelResource)
                .collect(Collectors.toList());
    }

    public ResponseEntity<ApiResponse> getAllInProgressAppointmentByDoctorIdForExport(Long doctorId) {
        var exportData = getAllInProgressAppointmentListByDoctorIdForExport(doctorId);
        Doctor doctor = doctorService.getOneDoctorById(doctorId);
        String fileName = String
                .join("_", doctor.getFirstName(), doctor.getLastName(), "appointments")
                .replace(" ", "_").toLowerCase();

        excelWriteService.writeToExcel(
                exportData,
                fileName,
                "DR ID = " + doctor.getId()
        );
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message(apiMessages.getMessage("success.export.completed"))
                        .build()
        );
    }


    public ResponseEntity<ApiResponse> getAllAppointmentByPatientSsnForExport(String ssn) {
        var exportData = getAllAppointmentListByPatientSsnForExport(ssn);
        Patient patient = patientService.getOnePatientBySsn(ssn);
        String patientFullName = String
                .join("_", patient.getFirstName(), patient.getLastName())
                .replace(" ", "_").toLowerCase();

        excelWriteService.writeToExcel(
                exportData,
                patientFullName,
                "PATIENT SSN = " + patient.getSsn()
        );
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message(apiMessages.getMessage("success.export.completed"))
                        .build()
        );
    }

    public ResponseEntity<ApiResponse> getAllAppointmentByDateForExport(LocalDate date) {
        var exportData = getAllAppointmentListByDateForExport(date);
        String sheetName = String.join(" = ", "APPTS", date.toString());
        excelWriteService.writeToExcel(
                exportData,
                date.toString(),
                sheetName
        );

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message(apiMessages.getMessage("success.export.completed"))
                        .build()
        );
    }

    public List<SimpleAppointmentResponse> getAllAppointmentListByPatientSsn(String ssn) {
        return appointmentRepository
                .findByPatient_SsnOrderByCreatedAtDesc(ssn)
                .stream()
                .map(appointmentMapper::buildSimpleAppointmentResponse)
                .collect(Collectors.toList());
    }

    private List<AppointmentExcelResource> getAllAppointmentListByPatientSsnForExport(String ssn) {
        return appointmentRepository
                .findByPatient_SsnOrderByCreatedAtDesc(ssn)
                .stream()
                .map(appointmentMapper::buildAppointmentExcelResource)
                .collect(Collectors.toList());
    }

    public List<SimpleAppointmentResponse> getAllAppointmentListByDate(LocalDate date) {
        return appointmentRepository
                .findByAppointmentDateOrderByDoctor_FirstNameAscCreatedAtAsc(date)
                .stream()
                .map(appointmentMapper::buildSimpleAppointmentResponse)
                .collect(Collectors.toList());
    }

    private List<AppointmentExcelResource> getAllAppointmentListByDateForExport(LocalDate date) {
        return appointmentRepository
                .findByAppointmentDateOrderByDoctor_FirstNameAscCreatedAtAsc(date)
                .stream()
                .map(appointmentMapper::buildAppointmentExcelResource)
                .collect(Collectors.toList());
    }

}
