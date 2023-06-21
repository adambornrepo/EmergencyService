package com.tech.service;

import com.tech.configuration.ApiMessages;
import com.tech.demo.PrescriptionExportResource;
import com.tech.entites.abstracts.Employee;
import com.tech.entites.concretes.Appointment;
import com.tech.entites.concretes.Doctor;
import com.tech.entites.concretes.Prescription;
import com.tech.entites.enums.AppointmentStatus;
import com.tech.exception.custom.EmailSendingException;
import com.tech.exception.custom.ForbiddenAccessException;
import com.tech.exception.custom.ResourceNotFoundException;
import com.tech.exception.custom.UnsuitableRequestException;
import com.tech.mapper.PrescriptionMapper;
import com.tech.payload.request.PrescriptionCreationRequest;
import com.tech.payload.request.update.PrescriptionUpdateRequest;
import com.tech.payload.response.ApiResponse;
import com.tech.payload.response.detailed.DetailedPrescriptionResponse;
import com.tech.payload.response.simple.SimplePrescriptionResponse;
import com.tech.repository.PrescriptionRepository;
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

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;
    private final PrescriptionMapper prescriptionMapper;
    private final CheckAndCoordinationService coordinationService;
    private final PrescriptionPdfService prescriptionPdfService;
    private final EmailService emailService;
    private final ApiMessages apiMessages;

    public ResponseEntity<?> savePrescription(PrescriptionCreationRequest request, UserDetails userDetails) {
        Appointment appointmentFound = appointmentService.getOneAppointmentById(request.getAppointmentId());
        Employee employee = coordinationService.getOneEmployeeByUserDetails(userDetails);

        if (!appointmentFound.getDoctor().getId().equals(employee.getId())) {
            throw new ForbiddenAccessException(
                    String.format(apiMessages.getMessage("error.no.action.prescription.doctor"), employee.getId())
            );
        }
        if (!appointmentFound.getStatus().equals(AppointmentStatus.IN_PROGRESS)) {
            throw new UnsuitableRequestException(
                    String.format(apiMessages.getMessage("error.no.action.prescription.app"), appointmentFound.getStatus())
            );
        }

        Doctor doctorFound = doctorService.getOneDoctorById(employee.getId());

        String onePieceMedicines = String.join("¨!¨", request.getMedicines());
        var savePrescription = Prescription.builder()
                .medicines(onePieceMedicines)
                .doctor(doctorFound)
                .appointment(appointmentFound)
                .build();
        Prescription saved = prescriptionRepository.save(savePrescription);

        if (appointmentFound.getPatient().getEmail() != null) {
            try {
                sendPrescriptionToPatientEmailById(saved);
            } catch (EmailSendingException e) {
                return new ResponseEntity<>(
                        ApiResponse.builder()
                                .success(false)
                                .message(
                                        String.format(
                                                apiMessages.getMessage("error.send.mail.px.save"),
                                                saved.getId(),
                                                e.getMessage()
                                        )
                                )
                                .build(), HttpStatus.INTERNAL_SERVER_ERROR
                );
            }
        }

        return new ResponseEntity<>(prescriptionMapper.buildDetailedPrescriptionResponse(saved), HttpStatus.CREATED);
    }

    public ResponseEntity<DetailedPrescriptionResponse> updatePrescription(PrescriptionUpdateRequest request, Long id, UserDetails userDetails) {
        Prescription foundPrescription = getOnePrescriptionById(id);
        Employee employee = coordinationService.getOneEmployeeByUserDetails(userDetails);

        if (!employee.getId().equals(foundPrescription.getDoctor().getId())) {
            throw new ForbiddenAccessException(
                    String.format(apiMessages.getMessage("error.no.action.prescription.doctor"), employee.getId())
            );
        }
        if (!foundPrescription.getAppointment().getStatus().equals(AppointmentStatus.IN_PROGRESS)) {
            throw new UnsuitableRequestException(
                    String.format(apiMessages.getMessage("error.no.action.prescription.app"), foundPrescription.getAppointment().getStatus())
            );
        }

        Doctor doctorFound = doctorService.getOneDoctorById(employee.getId());

        if (!request.getMedicines().isEmpty()) {
            String onePieceMedicines = String.join("¨!¨", request.getMedicines());
            foundPrescription.setMedicines(onePieceMedicines);
        }

        Prescription updated = prescriptionRepository.save(foundPrescription);
        return new ResponseEntity<>(prescriptionMapper.buildDetailedPrescriptionResponse(updated), HttpStatus.ACCEPTED);
    }

    public Prescription getOnePrescriptionById(Long prescriptionId) {
        return prescriptionRepository
                .findById(prescriptionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(apiMessages.getMessage("error.not.found.prescription.id"), prescriptionId))
                );
    }

    public ResponseEntity<ApiResponse> deletePrescription(Long id, UserDetails userDetails) {
        Prescription deletePrescription = getOnePrescriptionById(id);
        Employee employee = coordinationService.getOneEmployeeByUserDetails(userDetails);

        if (!deletePrescription.getDoctor().getId().equals(employee.getId())) {
            throw new ForbiddenAccessException(
                    String.format(apiMessages.getMessage("error.no.action.prescription.doctor"), employee.getId())
            );
        }

        if (!deletePrescription.getAppointment().getStatus().equals(AppointmentStatus.IN_PROGRESS)) {
            throw new UnsuitableRequestException(
                    String.format(apiMessages.getMessage("error.no.action.prescription.app"), deletePrescription.getAppointment().getStatus())
            );
        }

        prescriptionRepository.delete(deletePrescription);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message(apiMessages.getMessage("success.prescription.delete"))
                        .build()
        );
    }

    public ResponseEntity<ApiResponse> exportPrescriptionToPdfById(Long prescriptionId) {
        Prescription prescription = getOnePrescriptionById(prescriptionId);
        PrescriptionExportResource exportData = prescriptionMapper.buildPrescriptionExportResource(prescription);
        prescriptionPdfService.createPdf(exportData);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message(apiMessages.getMessage("success.prescription.export.pdf"))
                        .build()
        );
    }

    public ResponseEntity<ApiResponse> sendPrescriptionToPatientEmailById(Long prescriptionId) {
        Prescription prescription = getOnePrescriptionById(prescriptionId);
        PrescriptionExportResource exportData = prescriptionMapper.buildPrescriptionExportResource(prescription);

        if (exportData.getPatientEmail() == null) {
            return new ResponseEntity<>(
                    ApiResponse.builder()
                            .success(false)
                            .message(apiMessages.getMessage("error.not.exists.email"))
                            .build(), HttpStatus.NOT_FOUND
            );
        }
        emailService.sendPrescriptionEmail(exportData, exportData.getPatientEmail());
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message(apiMessages.getMessage("success.prescription.mail.send"))
                        .build()
        );
    }

    private void sendPrescriptionToPatientEmailById(Prescription prescription) {
        PrescriptionExportResource exportData = prescriptionMapper.buildPrescriptionExportResource(prescription);
        emailService.sendPrescriptionEmail(exportData, exportData.getPatientEmail());
    }

    public ResponseEntity<DetailedPrescriptionResponse> getOneDetailedPrescriptionById(Long id) {
        return ResponseEntity.ok(prescriptionMapper.buildDetailedPrescriptionResponse(getOnePrescriptionById(id)));
    }

    public Page<SimplePrescriptionResponse> getAllPrescription(Pageable pageable) {
        return prescriptionRepository.findAll(pageable).map(prescriptionMapper::buildSimplePrescriptionResponse);
    }


    public Page<SimplePrescriptionResponse> getAllPrescriptionByDoctorId(Long doctorId, Pageable pageable, UserDetails userDetails) {

        Employee employee = coordinationService.getOneEmployeeByUserDetails(userDetails);
        if (employee.getRole().equals(Role.DOCTOR) && !employee.getId().equals(doctorId)) {
            throw new ForbiddenAccessException(apiMessages.getMessage("error.forbidden.doctor.info"));
        }

        return prescriptionRepository.findByDoctor_Id(doctorId, pageable).map(prescriptionMapper::buildSimplePrescriptionResponse);
    }

    public ResponseEntity<SimplePrescriptionResponse> getPrescriptionByAppointmentId(Long appointmentId) {
        Prescription prescription = prescriptionRepository
                .findByAppointment_Id(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException(""));
        return ResponseEntity.ok(prescriptionMapper.buildSimplePrescriptionResponse(prescription));
    }

    public Page<SimplePrescriptionResponse> getAllPrescriptionByPatientId(Long patientId, Pageable pageable) {
        return prescriptionRepository
                .findByAppointment_Patient_Id(patientId, pageable)
                .map(prescriptionMapper::buildSimplePrescriptionResponse);
    }

    public Page<SimplePrescriptionResponse> getAllPrescriptionByPatientSsn(String ssn, Pageable pageable) {
        return prescriptionRepository.findByAppointment_Patient_Ssn(ssn, pageable).map(prescriptionMapper::buildSimplePrescriptionResponse);
    }

}
