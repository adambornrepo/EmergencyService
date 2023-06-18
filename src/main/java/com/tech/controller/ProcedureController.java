package com.tech.controller;

import com.tech.payload.request.ProcedureCreationRequest;
import com.tech.payload.request.ProcedureCreationRequestForDoctor;
import com.tech.payload.request.update.ProcedureUpdateRequest;
import com.tech.payload.response.ApiResponse;
import com.tech.payload.response.ProcedureResponse;
import com.tech.service.ProcedureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/procedure")
@RequiredArgsConstructor
public class ProcedureController {

    private final ProcedureService procedureService;

    @GetMapping("/getAll/appointment")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','doctor:read','nurse:read','lab_tech:read','radiology_tech:read')")
    public ResponseEntity<List<ProcedureResponse>> getAllProcedureByAppointmentId(@RequestParam("id") Long id) {
        return procedureService.getAllProcedureByAppointmentId(id);
    }

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

    @GetMapping("/getAll/active/employee")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','doctor:read','nurse:read','lab_tech:read','radiology_tech:read')")
    public ResponseEntity<List<ProcedureResponse>> getAllProcedureActiveByEmployeeId(
            @RequestParam("id") Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return procedureService.getAllActiveProcedureByEmployeeId(id, userDetails);
    }


    @PostMapping("/create/dr")
    @PreAuthorize("hasAnyAuthority('chief:create','doctor:create')")
    public ResponseEntity<ProcedureResponse> saveProcedureForDoctor(@Valid @RequestBody ProcedureCreationRequestForDoctor request) {
        return procedureService.saveProcedureForDoctor(request);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('chief:create','doctor:create')")
    public ResponseEntity<ProcedureResponse> saveProcedure(@Valid @RequestBody ProcedureCreationRequest request) {
        return procedureService.saveProcedure(request);
    }

    @PatchMapping("/update")
    @PreAuthorize("hasAnyAuthority('chief:update','doctor:update','nurse:update','lab_tech:update','radiology_tech:update')")
    public ResponseEntity<ProcedureResponse> updateProcedure(
            @Valid @RequestBody ProcedureUpdateRequest request,
            @RequestParam("id") Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return procedureService.updateProcedure(request, id, userDetails);
    }

    @PatchMapping("/complete")
    @PreAuthorize("hasAnyAuthority('nurse:update','lab_tech:update','radiology_tech:update')")
    public ResponseEntity<ProcedureResponse> completeProcedure(
            @RequestParam("id") Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return procedureService.completeProcedure(id, userDetails);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority(,'chief:delete','doctor:delete','nurse:delete','lab_tech:delete','radiology_tech:delete')")
    public ResponseEntity<ApiResponse> deleteProcedure(@RequestParam("id") Long id, @AuthenticationPrincipal UserDetails userDetails) {
        return procedureService.deleteProcedure(id, userDetails);
    }

}
