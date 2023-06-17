package com.tech.service;

import com.tech.configuration.ApiMessages;
import com.tech.entites.abstracts.MedicalEmployee;
import com.tech.entites.concretes.Appointment;
import com.tech.entites.concretes.Procedure;
import com.tech.entites.enums.AppointmentStatus;
import com.tech.entites.enums.ProcedureStatus;
import com.tech.exception.custom.ResourceNotFoundException;
import com.tech.exception.custom.UnsuitableRequestException;
import com.tech.payload.request.ProcedureCreationRequest;
import com.tech.payload.request.ProcedureCreationRequestForDoctor;
import com.tech.payload.request.update.ProcedureUpdateRequest;
import com.tech.payload.response.ApiResponse;
import com.tech.payload.response.ProcedureResponse;
import com.tech.repository.ProcedureRepository;
import com.tech.utils.GeneralUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProcedureService {
    private final ProcedureRepository procedureRepository;
    private final AppointmentService appointmentService;
    private final CheckAndCoordinationService coordinationService;
    private final ApiMessages apiMessages;

    public ResponseEntity<List<ProcedureResponse>> getAllProcedureByAppointmentId(Long appointmentId) {
        return ResponseEntity.ok(
                procedureRepository.findByAppointment_Id(appointmentId)
                        .stream()
                        .map(this::buildProcedureResponse)
                        .collect(Collectors.toList())
        );
    }

    public Page<ProcedureResponse> getAllProcedureByEmployeeId(Long employeeId, int page, int size, String sort, String type) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("isDisabled").ascending().and(Sort.by(sort)).ascending());
        if (Objects.equals(type, "DESC")) {
            pageable = PageRequest.of(page, size, Sort.by("isDisabled").ascending().and(Sort.by(sort)).descending());
        }
        return procedureRepository.findByEmployee_Id(employeeId, pageable).map(this::buildProcedureResponse);
    }

    public ResponseEntity<ProcedureResponse> saveProcedureForDoctor(ProcedureCreationRequestForDoctor request) {

        Appointment appointment = appointmentService.getOneAppointmentById(request.getAppointmentId());
        if (!appointment.getStatus().equals(AppointmentStatus.IN_PROGRESS)) {
            throw new UnsuitableRequestException(
                    String.format(apiMessages.getMessage("error.no.action.procedure.app"), appointment.getStatus())
            );
        }

        var saveProcedure = Procedure.builder()
                .applied(request.getApplied())
                .appointment(appointment)
                .employee(appointment.getDoctor())
                .status(ProcedureStatus.APPLIED)
                .isDisabled(false)
                .build();
        Procedure saved = procedureRepository.save(saveProcedure);
        return new ResponseEntity<>(buildProcedureResponse(saved), HttpStatus.CREATED);
    }

    public ResponseEntity<ProcedureResponse> saveProcedure(ProcedureCreationRequest request) {

        Appointment appointment = appointmentService.getOneAppointmentById(request.getAppointmentId());
        if (!appointment.getStatus().equals(AppointmentStatus.IN_PROGRESS)) {
            throw new UnsuitableRequestException(
                    String.format(apiMessages.getMessage("error.no.action.procedure.app"), appointment.getStatus())
            );
        }

        MedicalEmployee employee = coordinationService.getOneMedicalEmployeeById(request.getEmployeeId());
        if (employee == null) {
            throw new ResourceNotFoundException(
                    String.format(apiMessages.getMessage("error.not.found.medical.employee.id"), request.getEmployeeId())
            );
        }

        var saveProcedure = Procedure.builder()
                .doctorNote(request.getDoctorNote())
                .applied(null)
                .appointment(appointment)
                .employee(employee)
                .status(ProcedureStatus.NOT_APPLIED)
                .isDisabled(false)
                .build();
        Procedure saved = procedureRepository.save(saveProcedure);
        return new ResponseEntity<>(buildProcedureResponse(saved), HttpStatus.CREATED);
    }

    private Procedure getOneProcedureById(Long procedureId) {
        return procedureRepository
                .findById(procedureId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(apiMessages.getMessage("error.not.found.procedure.id"), procedureId)
                ));
    }


    public ResponseEntity<ProcedureResponse> updateProcedure(ProcedureUpdateRequest request, Long procedureId) {
        Procedure foundProcedure = getOneProcedureById(procedureId);
        if (foundProcedure.isDisabled()) {
            throw new ResourceNotFoundException(String.format(apiMessages.getMessage("error.not.exists.id"), procedureId));
        }
        if (!foundProcedure.getAppointment().getStatus().equals(AppointmentStatus.IN_PROGRESS)) {
            throw new UnsuitableRequestException(
                    String.format(apiMessages.getMessage("error.no.action.procedure.app"), foundProcedure.getAppointment().getStatus())
            );
        }
        if (!foundProcedure.getEmployee().getId().equals(request.getEmployeeId())) {
            throw new UnsuitableRequestException(
                    String.format(apiMessages.getMessage("error.no.action.procedure.employee"), request.getEmployeeId())
            );
        }
        foundProcedure.setApplied(request.getApplied());
        Procedure updated = procedureRepository.save(foundProcedure);
        return new ResponseEntity<>(buildProcedureResponse(updated), HttpStatus.ACCEPTED);
    }

    public ResponseEntity<ApiResponse> deleteProcedure(Long procedureId) {
        Procedure foundProcedure = getOneProcedureById(procedureId);
        if (foundProcedure.isDisabled()) {
            throw new ResourceNotFoundException(String.format(apiMessages.getMessage("error.not.exists.id"), procedureId));
        }
        if (!foundProcedure.getAppointment().getStatus().equals(AppointmentStatus.IN_PROGRESS)) {
            throw new UnsuitableRequestException(
                    String.format(apiMessages.getMessage("error.no.action.procedure.app"), foundProcedure.getAppointment().getStatus())
            );
        }

        /**
         * TODO Anlık login olan ile procedure ü gerçekleştiren aynı kişi mi
         */
        foundProcedure.setStatus(ProcedureStatus.DELETED);
        foundProcedure.setDisabled(true);

        Procedure deleted = procedureRepository.save(foundProcedure);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message(apiMessages.getMessage("success.procedure.delete"))
                        .build()
        );
    }

    private ProcedureResponse buildProcedureResponse(Procedure procedure) {
        return ProcedureResponse.builder()
                .id(procedure.getId())
                .applied(procedure.getApplied())
                .appointmentId(procedure.getAppointment().getId())
                .employeeId(procedure.getEmployee().getId())
                .employeeFirstName(procedure.getEmployee().getFirstName())
                .employeeLastName(procedure.getEmployee().getLastName())
                .createdAt(GeneralUtils.convertMillisToLocalDateTime(procedure.getCreatedAt()))
                .updatedAt(GeneralUtils.convertMillisToLocalDateTime(procedure.getUpdatedAt()))
                .isDisabled(procedure.isDisabled())
                .build();
    }

    public ResponseEntity<ProcedureResponse> completeProcedure(Long id) {

        /**
         * TODO Anlık login olan ile procedure ü gerçekleştiren aynı kişi mi
         */

        Procedure foundProcedure = getOneProcedureById(id);
        foundProcedure.setStatus(ProcedureStatus.APPLIED);
        Procedure updated = procedureRepository.save(foundProcedure);
        return null;
    }


    public ResponseEntity<List<ProcedureResponse>> getAllActiveProcedureByEmployeeId(Long id) {
        return ResponseEntity.ok(
                procedureRepository.
                        findByEmployee_IdAndStatus(id, ProcedureStatus.NOT_APPLIED)
                        .stream()
                        .map(this::buildProcedureResponse)
                        .collect(Collectors.toList())
        );
    }

}
