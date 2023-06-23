package com.tech.controller;

import com.tech.payload.request.ProcedureCreationRequest;
import com.tech.payload.request.ProcedureCreationRequestForDoctor;
import com.tech.payload.request.update.ProcedureUpdateRequest;
import com.tech.payload.response.ApiResponse;
import com.tech.payload.response.ProcedureResponse;
import com.tech.service.ProcedureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(
        name = "ProcedureController",
        description = "Procedure Controller is responsible for performing operations related to Procedures"
)
@RestController
@RequestMapping("api/v1/procedure")
@RequiredArgsConstructor
public class ProcedureController {

    private final ProcedureService procedureService;

    @Operation(
            summary = "Get all procedures by appointment ID",
            description = "Retrieves a list of procedures based on the appointment ID."
    )
    @GetMapping("/getAll/appointment")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','doctor:read','nurse:read','lab_tech:read','radiology_tech:read')")
    public ResponseEntity<List<ProcedureResponse>> getAllProcedureByAppointmentId(@RequestParam("id") Long id) {
        return procedureService.getAllProcedureByAppointmentId(id);
    }

    @Operation(
            summary = "Get all procedures by employee ID",
            description = "Retrieves a page of procedures based on the employee ID."
    )
    @GetMapping("/getAll/employee")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read')")
    public Page<ProcedureResponse> getAllProcedureByEmployeeId(
            @RequestParam("id") Long id,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "type", defaultValue = "DESC") String type
    ) {
        return procedureService.getAllProcedureByEmployeeId(id, page, size, sort, type);
    }

    @Operation(
            summary = "Get all procedures for export to Excel by employee ID and date",
            description = "Retrieves a response for exporting procedures based on the employee ID and date."
    )
    @GetMapping("/getAll/export/employee")
    @PreAuthorize("hasAnyAuthority('admin:create','chief:create')")
    public ResponseEntity<ApiResponse> getAllProcedureByEmployeeIdForExport(
            @RequestParam("id") Long id, @RequestParam("on") LocalDate date
    ) {
        return procedureService.getAllProcedureOnDateByEmployeeIdForExport(id, date);
    }

    @Operation(
            summary = "Get all active procedures by employee ID",
            description = "Retrieves a list of active procedures based on the medical employee ID. " +
                    "Admin and Chief have authority over all employees' procedures, while others can only view their procedure own list."
    )
    @GetMapping("/getAll/active/employee")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','doctor:read','nurse:read','lab_tech:read','radiology_tech:read')")
    public ResponseEntity<List<ProcedureResponse>> getAllProcedureActiveByEmployeeId(
            @RequestParam("id") Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return procedureService.getAllActiveProcedureByEmployeeId(id, userDetails);
    }

    @Operation(
            summary = "Save procedure for a doctor",
            description = "Creates and saves a new procedure for a doctor if the doctor has executed the procedure themselves."
    )
    @PostMapping("/create/dr")
    @PreAuthorize("hasAnyAuthority('chief:create','doctor:create')")
    public ResponseEntity<ProcedureResponse> saveProcedureForDoctor(@Valid @RequestBody ProcedureCreationRequestForDoctor request) {
        return procedureService.saveProcedureForDoctor(request);
    }

    @Operation(
            summary = "Save procedure",
            description = "Creates and saves a new procedure for other medical employees to perform, " +
                    "such as tests, radiographs etc., requested by the doctor."
    )
    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('chief:create','doctor:create')")
    public ResponseEntity<ProcedureResponse> saveProcedure(@Valid @RequestBody ProcedureCreationRequest request) {
        return procedureService.saveProcedure(request);
    }

    @Operation(
            summary = "Update procedure",
            description = "Updates an existing procedure owned by the medical employee."
    )
    @PatchMapping("/update")
    @PreAuthorize("hasAnyAuthority('chief:update','doctor:update','nurse:update','lab_tech:update','radiology_tech:update')")
    public ResponseEntity<ProcedureResponse> updateProcedure(
            @Valid @RequestBody ProcedureUpdateRequest request,
            @RequestParam("id") Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return procedureService.updateProcedure(request, id, userDetails);
    }

    @Operation(
            summary = "Complete procedure",
            description = "Marks as complete an existing procedure owned by the medical employee."
    )
    @PatchMapping("/complete")
    @PreAuthorize("hasAnyAuthority('nurse:update','lab_tech:update','radiology_tech:update')")
    public ResponseEntity<ApiResponse> completeProcedure(
            @RequestParam("id") Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return procedureService.completeProcedure(id, userDetails);
    }

    @Operation(
            summary = "Delete procedure",
            description = "Deletes an existing procedure owned by the medical employee."
    )
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority(,'chief:delete','doctor:delete','nurse:delete','lab_tech:delete','radiology_tech:delete')")
    public ResponseEntity<ApiResponse> deleteProcedure(@RequestParam("id") Long id, @AuthenticationPrincipal UserDetails userDetails) {
        return procedureService.deleteProcedure(id, userDetails);
    }

}
