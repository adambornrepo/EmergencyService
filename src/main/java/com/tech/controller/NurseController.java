package com.tech.controller;

import com.tech.entites.enums.UniqueField;
import com.tech.entites.enums.Zone;
import com.tech.payload.request.register.NurseRegistrationRequest;
import com.tech.payload.request.update.NurseUpdateRequest;
import com.tech.payload.response.*;
import com.tech.payload.response.detailed.DetailedDoctorResponse;
import com.tech.payload.response.detailed.DetailedNurseResponse;
import com.tech.payload.response.simple.SimpleNurseResponse;
import com.tech.service.NurseService;
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
        name = "NurseController",
        description = "Nurse Controller is responsible for performing operations related to Nurses."
)
@RestController
@RequestMapping("api/v1/nurse")
@RequiredArgsConstructor
public class NurseController {

    private final NurseService nurseService;

    @Operation(
            summary = "Get Nurse",
            description = "Retrieves detailed information about a specific Nurse based on unique field and value. " +
                    "This method can be executed by Admin, Chief, or Nurse with read authority. " +
                    "This method can be executed by providing one of the following values: ID, SSN or PHONE_NUMBER"
    )
    @GetMapping("/get")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','nurse:read')")
    public ResponseEntity<DetailedNurseResponse> getOneNurse(@RequestParam("in") UniqueField searchIn, @RequestParam("val") String value) {
        return nurseService.getOneNurseByUniqueField(searchIn, value);
    }

    @Operation(
            summary = "Get All Nurses",
            description = "Retrieves a paginated list of all Nurses. " +
                    "This method can be executed by Admin or Chief with read authority."
    )
    @GetMapping("/getAll")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read')")
    public Page<SimpleNurseResponse> getAllNurse(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "ssn") String sort,
            @RequestParam(value = "type", defaultValue = "ASC") String type
    ) {
        return nurseService.getAllNurse(page, size, sort, type);
    }

    @Operation(
            summary = "Get All Active Nurses",
            description = "Retrieves a list of all active Nurses. " +
                    "This method can be executed by Admin, Chief, Doctor, Nurse, or Psr with read authority. " +
                    "The 'zone' parameter is optional for this method. " +
                    "If the 'zone' parameter is not provided, all active nurses are retrieved. " +
                    "If the 'zone' parameter is provided, only the active nurses in the specified zone are retrieved."
    )
    @GetMapping("/getAll/active")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','doctor:read','nurse:read','psr:read')")
    public List<SimpleNurseResponse> getAllActiveNurse(@RequestParam(required = false, value = "zone") Zone zone) {
        return nurseService.getAllActiveNurse(zone);
    }

    @Operation(
            summary = "Register Nurse",
            description = "Registers a new Nurse. " +
                    "This method can be executed by Admin or Chief with create authority."
    )
    @PostMapping("/register")
    @PreAuthorize("hasAnyAuthority('admin:create','chief:create')")
    public ResponseEntity<DetailedNurseResponse> saveNurse(@Valid @RequestBody NurseRegistrationRequest request) {
        return nurseService.saveNurse(request);
    }

    @Operation(
            summary = "Update Nurse",
            description = "Updates the information of an existing Nurse based on the specified ID. " +
                    "This method can be executed by Admin or Nurse with update authority. " +
                    "Admin has authority over all Nurse accounts, while Nurse can only update their own account."
    )
    @PatchMapping("/update")
    @PreAuthorize("hasAnyAuthority('admin:update','nurse:update')")
    public ResponseEntity<DetailedNurseResponse> updateNurse(
            @Valid @RequestBody NurseUpdateRequest request,
            @RequestParam("id") Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return nurseService.updateNurse(request, id, userDetails);
    }

    @Operation(
            summary = "Update Nurse Zone",
            description = "Updates the zone of an existing Nurse based on the specified ID. " +
                    "This method can be executed by Admin or Chief with update authority."
    )
    @PatchMapping("/update/zone")
    @PreAuthorize("hasAnyAuthority('admin:update','chief:update')")
    public ResponseEntity<DetailedNurseResponse> updateNurseZone(
            @RequestParam("to") Zone zone,
            @RequestParam("id") Long id) {
        return nurseService.updateNurseZone(zone, id);
    }

    @Operation(
            summary = "Delete Nurse",
            description = "Deletes a Nurse based on the specified ID. " +
                    "This method can be executed by Admin, Chief, or Nurse with delete authority. " +
                    "Admin has authority over all Nurse accounts, while Nurse can only delete their own account."
    )
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('admin:delete','chief:delete','nurse:delete')")
    public ResponseEntity<ApiResponse> deleteNurse(@RequestParam("id") Long id, @AuthenticationPrincipal UserDetails userDetails) {
        return nurseService.deleteNurse(id, userDetails);
    }


}
