package com.tech.controller;

import com.tech.entites.enums.UniqueField;
import com.tech.entites.enums.Zone;
import com.tech.payload.request.register.DoctorRegistrationRequest;
import com.tech.payload.request.update.DoctorUpdateRequest;
import com.tech.payload.response.ApiResponse;
import com.tech.payload.response.detailed.DetailedDoctorResponse;
import com.tech.payload.response.simple.SimpleDoctorResponse;
import com.tech.service.DoctorService;
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
        name = "DoctorController",
        description = "Doctor Controller is responsible for performing operations related to Doctors."
)
@RestController
@RequestMapping("api/v1/dr")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @Operation(
            summary = "Get One Doctor",
            description = "This method can be executed by Admin, Chief, or Doctor with read authority. " +
                    "Retrieves detailed information about a specific Doctor based on unique field and value. " +
                    "This method can be executed by providing one of the following values: ID, SSN or PHONE_NUMBER"
    )
    @GetMapping("/get")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','doctor:read')")
    public ResponseEntity<DetailedDoctorResponse> getOneDoctor(
            @RequestParam("in") UniqueField searchIn, @RequestParam("val") String value
    ) {
        return doctorService.getOneDoctorByUniqueField(searchIn, value);
    }

    @Operation(
            summary = "Get All Doctors",
            description = "Retrieves a paginated list of all Doctors. " +
                    "This method can be executed by Admin or Chief with read authority."
    )
    @GetMapping("/getAll")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read')")
    public Page<SimpleDoctorResponse> getAllDoctor(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "ssn") String sort,
            @RequestParam(value = "type", defaultValue = "ASC") String type
    ) {
        return doctorService.getAllDoctor(page, size, sort, type);
    }

    @Operation(
            summary = "Get All Active Doctors",
            description = "Retrieves a list of all active Doctors. " +
                    "This method can be executed by Admin, Chief, Doctor, or Psr with read authority. " +
                    "The 'zone' parameter is optional for this method. " +
                    "If the 'zone' parameter is not provided, all active doctors are retrieved. " +
                    "If the 'zone' parameter is provided, only the active doctors in the specified zone are retrieved."
    )
    @GetMapping("/getAll/active")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','doctor:read','psr:read')")
    public List<SimpleDoctorResponse> getAllActiveDoctor(@RequestParam(required = false, value = "zone") Zone zone) {
        return doctorService.getAllActiveDoctor(zone);
    }

    @Operation(
            summary = "Register Doctor",
            description = "Registers a new Doctor. This method can be executed by Admin or Chief with create authority."
    )
    @PostMapping("/register")
    @PreAuthorize("hasAnyAuthority('admin:create','chief:create')")
    public ResponseEntity<DetailedDoctorResponse> saveDoctor(@Valid @RequestBody DoctorRegistrationRequest request) {
        return doctorService.saveDoctor(request);
    }

    @Operation(
            summary = "Update Doctor",
            description = "Updates the information of an existing Doctor based on the specified ID. " +
                    "This method can be executed by Admin, Chief, or Doctor with update authority. " +
                    "Admin has authority over all Chief/Doctor accounts, while Chief/Doctor can only update their own account."
    )
    @PatchMapping("/update")
    @PreAuthorize("hasAnyAuthority('admin:update','chief:update','doctor:update')")
    public ResponseEntity<DetailedDoctorResponse> updateDoctor(
            @Valid @RequestBody DoctorUpdateRequest request,
            @RequestParam("id") Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return doctorService.updateDoctor(request, id, userDetails);
    }


    @Operation(
            summary = "Update Doctor Zone",
            description = "Updates the zone of an existing Doctor based on the specified ID. " +
                    "This method can be executed by Admin or Chief with update authority."
    )
    @PatchMapping("/update/zone")
    @PreAuthorize("hasAnyAuthority('admin:update','chief:update')")
    public ResponseEntity<DetailedDoctorResponse> updateDoctorZone(
            @RequestParam("to") Zone zone,
            @RequestParam("id") Long id) {
        return doctorService.updateDoctorZone(zone, id);
    }

    @Operation(
            summary = "Delete Doctor",
            description = "Deletes a Doctor based on the specified ID. " +
                    "This method can be executed by Admin, Chief, or Doctor with delete authority. " +
                    "Admin has authority over all Chief/Doctor accounts, while Chief/Doctor can only delete their own account."
    )
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('admin:delete','chief:delete','doctor:delete')")
    public ResponseEntity<ApiResponse> deleteDoctor(@RequestParam("id") Long id, @AuthenticationPrincipal UserDetails userDetails) {
        return doctorService.deleteDoctor(id, userDetails);
    }

    @Operation(
            summary = "Assign Chief Physician",
            description = "Assigns the role of Chief Physician to a Doctor based on the specified ID. " +
                    "This method can be executed by Admin or Chief with update authority. " +
                    "In this application, there can only be one chief physician. " +
                    "If an assignment is made and there is already a chief physician, that doctor will be updated as a regular doctor."
    )
    @PatchMapping("/assign/chief")
    @PreAuthorize("hasAnyAuthority('admin:update','chief:update')")
    public ResponseEntity<DetailedDoctorResponse> toAssignChiefPhysician(@RequestParam("id") Long id) {
        return doctorService.assignChiefPhysician(id);
    }


}
