package com.tech.service;

import com.tech.configuration.ApiMessages;
import com.tech.entites.concretes.Appointment;
import com.tech.entites.concretes.Doctor;
import com.tech.entites.concretes.Prescription;
import com.tech.entites.enums.AppointmentStatus;
import com.tech.exception.custom.ResourceNotFoundException;
import com.tech.exception.custom.UnsuitableRequestException;
import com.tech.mapper.PrescriptionMapper;
import com.tech.payload.request.PrescriptionCreationRequest;
import com.tech.payload.request.update.PrescriptionUpdateRequest;
import com.tech.payload.response.ApiResponse;
import com.tech.payload.response.detailed.DetailedPrescriptionResponse;
import com.tech.payload.response.simple.SimplePrescriptionResponse;
import com.tech.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;
    private final PrescriptionMapper prescriptionMapper;
    private final ApiMessages apiMessages;

    public ResponseEntity<DetailedPrescriptionResponse> savePrescription(PrescriptionCreationRequest request) {
        Appointment appointmentFound = appointmentService.getOneAppointmentById(request.getAppointmentId());
        /**
         * TODO Bu kısım login olan doctordan da alınabilirdi
         */
        if (!appointmentFound.getDoctor().getId().equals(request.getDoctorId())) {
            throw new UnsuitableRequestException(
                    String.format(apiMessages.getMessage("error.no.action.prescription.doctor"), request.getDoctorId())
            );
        }
        if (!appointmentFound.getStatus().equals(AppointmentStatus.IN_PROGRESS)) {
            throw new UnsuitableRequestException(
                    String.format(apiMessages.getMessage("error.no.action.prescription.app"), appointmentFound.getStatus())
            );
        }

        Doctor doctorFound = doctorService.getOneDoctorById(request.getDoctorId());

        String onePieceMedicines = String.join("¨!¨", request.getMedicines());
        var savePrescription = Prescription.builder()
                .medicines(onePieceMedicines)
                .doctor(doctorFound)
                .appointment(appointmentFound)
                .build();
        Prescription saved = prescriptionRepository.save(savePrescription);

        return new ResponseEntity<>(prescriptionMapper.buildDetailedPrescriptionResponse(saved), HttpStatus.CREATED);
    }

    public ResponseEntity<DetailedPrescriptionResponse> updatePrescription(PrescriptionUpdateRequest request, Long id) {
        Prescription foundPrescription = getOnePrescriptionById(id).toBuilder().build();
        if (!request.getDoctorId().equals(foundPrescription.getDoctor().getId())) {
            throw new UnsuitableRequestException(
                    String.format(apiMessages.getMessage("error.no.action.prescription.doctor"), request.getDoctorId())
            );
        }
        if (!foundPrescription.getAppointment().getStatus().equals(AppointmentStatus.IN_PROGRESS)) {
            throw new UnsuitableRequestException(
                    String.format(apiMessages.getMessage("error.no.action.prescription.app"), foundPrescription.getAppointment().getStatus())
            );
        }

        Doctor doctorFound = doctorService.getOneDoctorById(request.getDoctorId());

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

    public ResponseEntity<ApiResponse> deletePrescription(Long id) {
        Prescription deletePrescription = getOnePrescriptionById(id);

        // TODO: 13.06.2023 Bu methoda ulaşan docktor id si anlık login den bun ve eşle, eşleşmiyorsa hata at

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


    public ResponseEntity<DetailedPrescriptionResponse> getOneDetailedPrescriptionById(Long id) {
        return ResponseEntity.ok(prescriptionMapper.buildDetailedPrescriptionResponse(getOnePrescriptionById(id)));
    }

    public Page<SimplePrescriptionResponse> getAllPrescription(Pageable pageable) {
        return prescriptionRepository.findAll(pageable).map(prescriptionMapper::buildSimplePrescriptionResponse);
    }


    public Page<SimplePrescriptionResponse> getAllPrescriptionByDoctorId(Long doctorId, Pageable pageable) {
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
