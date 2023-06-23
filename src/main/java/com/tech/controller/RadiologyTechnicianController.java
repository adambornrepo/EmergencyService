package com.tech.controller;

import com.tech.entites.enums.UniqueField;
import com.tech.payload.request.register.RadiologyTechRegistrationRequest;
import com.tech.payload.request.update.RadiologyTechUpdateRequest;
import com.tech.payload.response.ApiResponse;
import com.tech.payload.response.detailed.DetailedRadiologyTechResponse;
import com.tech.payload.response.simple.SimpleRadiologyTechResponse;
import com.tech.service.RadiologyTechnicianService;
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
        name = "RadiologyTechnicianController",
        description = "Radiology Technician Controller is responsible for performing operations related to Radiology Technicians."
)
@RestController
@RequestMapping("api/v1/radioTech")
@RequiredArgsConstructor
public class RadiologyTechnicianController {

    private final RadiologyTechnicianService radiologyTechService;

    @Operation(
            summary = "Get Radiology Technician",
            description = "Retrieves detailed information about a specific Radiology Technician based on unique field and value. " +
                    "This method can be executed by Admin, Chief, or Radiology Technician with read authority. " +
                    "This method can be executed by providing one of the following values: ID, SSN or PHONE_NUMBER"
    )
    @GetMapping("/get")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','radiology_tech:read')")
    public ResponseEntity<DetailedRadiologyTechResponse> getOneRadioTech(@RequestParam("in") UniqueField searchIn, @RequestParam("val") String value) {
        return radiologyTechService.getOneRadiologyTechByUniqueField(searchIn, value);
    }

    @Operation(
            summary = "Get All Radiology Technicians",
            description = "Retrieves a paginated list of all Radiology Technicians. " +
                    "This method can be executed by Admin or Chief with read authority."
    )
    @GetMapping("/getAll")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read')")
    public Page<SimpleRadiologyTechResponse> getAllRadiologyTech(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "ssn") String sort,
            @RequestParam(value = "type", defaultValue = "ASC") String type
    ) {
        return radiologyTechService.getAllRadiologyTech(page, size, sort, type);
    }

    @Operation(
            summary = "Get All Active Radiology Technicians",
            description = "Retrieves a list of all active Radiology Technicians. " +
                    "This method can be executed by Admin, Chief, Doctor, Radiology Technician, or Psr with read authority."
    )
    @GetMapping("/getAll/active")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','doctor:read','radiology_tech:read','psr:read')")
    public List<SimpleRadiologyTechResponse> getAllActiveRadiologyTech() {
        return radiologyTechService.getAllActiveRadiologyTech();
    }

    @Operation(
            summary = "Register Radiology Technician",
            description = "Registers a new Radiology Technician. " +
                    "This method can be executed by Admin or Chief with create authority."
    )
    @PostMapping("/register")
    @PreAuthorize("hasAnyAuthority('admin:create','chief:create')")
    public ResponseEntity<DetailedRadiologyTechResponse> saveRadiologyTech(@Valid @RequestBody RadiologyTechRegistrationRequest request) {
        return radiologyTechService.saveRadiologyTech(request);
    }

    @Operation(
            summary = "Update Radiology Technician",
            description = "Updates the information of an existing Radiology Technician based on the specified ID. " +
                    "This method can be executed by Admin or Radiology Technician with update authority. " +
                    "Admin has authority over all Radiology Technician accounts, while Radiology Technician can only update their own account."
    )
    @PatchMapping("/update")
    @PreAuthorize("hasAnyAuthority('admin:update','radiology_tech:update')")
    public ResponseEntity<DetailedRadiologyTechResponse> updateRadiologyTech(
            @Valid @RequestBody RadiologyTechUpdateRequest request,
            @RequestParam("id") Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return radiologyTechService.updateRadiologyTech(request, id, userDetails);
    }

    @Operation(
            summary = "Delete Radiology Technician",
            description = "Deletes a Radiology Technician based on the specified ID. " +
                    "This method can be executed by Admin, Chief, or Radiology Technician with delete authority. " +
                    "Admin has authority over all Radiology Technician accounts, while Radiology Technician can only delete their own account."
    )
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('admin:delete','chief:delete','radiology_tech:delete')")
    public ResponseEntity<ApiResponse> deleteRadiologyTech(@RequestParam("id") Long id, @AuthenticationPrincipal UserDetails userDetails) {
        return radiologyTechService.deleteRadiologyTech(id, userDetails);
    }


}
