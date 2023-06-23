package com.tech.controller;

import com.tech.entites.enums.UniqueField;
import com.tech.payload.request.register.LabTechRegistrationRequest;
import com.tech.payload.request.update.LabTechUpdateRequest;
import com.tech.payload.response.*;
import com.tech.payload.response.detailed.DetailedLabTechResponse;
import com.tech.payload.response.simple.SimpleLabTechResponse;
import com.tech.service.LabTechnicianService;
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

import java.util.List;

@Tag(
        name = "LaboratoryTechnicianController",
        description = "LabTechnician Controller is responsible for performing operations related to Lab Technicians."
)
@RestController
@RequestMapping("api/v1/labTech")
@RequiredArgsConstructor
public class LabTechnicianController {

    private final LabTechnicianService labTechService;

    @Operation(
            summary = "Get Lab Technician",
            description = "Retrieves detailed information about a specific Lab Technician based on unique field and value. " +
                    "This method can be executed by Admin, Chief, or Lab Technician with read authority. " +
                    "This method can be executed by providing one of the following values: ID, SSN or PHONE_NUMBER"
    )
    @GetMapping("/get")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','lab_tech:read')")
    public ResponseEntity<DetailedLabTechResponse> getOneLabTech(@RequestParam("in") UniqueField searchIn, @RequestParam("val") String value) {
        return labTechService.getOneLabTechByUniqueField(searchIn, value);
    }

    @Operation(
            summary = "Get All Lab Technicians",
            description = "Retrieves a paginated list of all Lab Technicians. " +
                    "This method can be executed by Admin or Chief with read authority."
    )
    @GetMapping("/getAll")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read')")
    public Page<SimpleLabTechResponse> getAllLabTech(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "ssn") String sort,
            @RequestParam(value = "type", defaultValue = "ASC") String type
    ) {
        return labTechService.getAllLabTech(page, size, sort, type);
    }

    @Operation(
            summary = "Get All Active Lab Technicians",
            description = "Retrieves a list of all active Lab Technicians. " +
                    "This method can be executed by Admin, Chief, Doctor, Lab Technician, or Psr with read authority."
    )
    @GetMapping("/getAll/active")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','doctor:read','lab_tech:read','psr:read')")
    public List<SimpleLabTechResponse> getAllActiveLabTech() {
        return labTechService.getAllActiveLabTech();
    }

    @Operation(
            summary = "Register Lab Technician",
            description = "Registers a new Lab Technician. " +
                    "This method can be executed by Admin or Chief with create authority."
    )
    @PostMapping("/register")
    @PreAuthorize("hasAnyAuthority('admin:create','chief:create')")
    public ResponseEntity<DetailedLabTechResponse> saveLabTech(@Valid @RequestBody LabTechRegistrationRequest request) {
        return labTechService.saveLabTech(request);
    }

    @Operation(
            summary = "Update Lab Technician",
            description = "Updates the information of an existing Lab Technician based on the specified ID. " +
                    "This method can be executed by Admin or Lab Technician with update authority. " +
                    "Admin has authority over all Lab Technician accounts, while Lab Technician can only update their own account."
    )
    @PatchMapping("/update")
    @PreAuthorize("hasAnyAuthority('admin:update','lab_tech:update')")
    public ResponseEntity<DetailedLabTechResponse> updateLabTech(
            @Valid @RequestBody LabTechUpdateRequest request,
            @RequestParam("id") Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return labTechService.updateLabTech(request, id, userDetails);
    }

    @Operation(
            summary = "Delete Lab Technician",
            description = "Deletes a Lab Technician based on the specified ID. " +
                    "This method can be executed by Admin, Chief, or Lab Technician with delete authority. " +
                    "Admin has authority over all Lab Technician accounts, while Lab Technician can only delete their own account."
    )
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('admin:delete','chief:delete','lab_tech:delete')")
    public ResponseEntity<ApiResponse> deleteLabTech(@RequestParam("id") Long id, @AuthenticationPrincipal UserDetails userDetails) {
        return labTechService.deleteLabTech(id, userDetails);
    }


}
