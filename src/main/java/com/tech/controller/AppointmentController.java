package com.tech.controller;

import com.tech.payload.request.AppointmentCreationRequest;
import com.tech.payload.request.update.AppointmentUpdateRequest;
import com.tech.payload.response.ApiResponse;
import com.tech.payload.response.detailed.DetailedAppointmentResponse;
import com.tech.payload.response.simple.SimpleAppointmentResponse;
import com.tech.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(
        name = "AppointmentController",
        description = "Appointment Controller is responsible for performing operations related to Appointments"
)
@RestController
@RequestMapping("api/v1/appointment")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Operation(
            summary = "Get Detailed Appointment",
            description = "This method retrieves detailed information about a specific Appointment based on its ID. " +
                    "It can be executed by Admin, Chief, Doctor, Nurse, or Psr with read authority."
    )
    @GetMapping("/get")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','doctor:read','nurse:read','psr:read')")
    public ResponseEntity<DetailedAppointmentResponse> getOneDetailedAppointmentById(@RequestParam("id") Long id) {
        return appointmentService.getOneDetailedAppointmentById(id);
    }

    @Operation(
            summary = "Get All Appointments",
            description = "Retrieves a paginated list of all Appointments. " +
                    "This method can be executed by Admin or Chief with read authority."
    )
    @GetMapping("/getAll")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read')")
    public Page<SimpleAppointmentResponse> getAllAppointment(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return appointmentService.getAllAppointment(pageable);
    }

    @Operation(
            summary = "Get All Appointments by Doctor ID",
            description = "Retrieves a paginated list of all Appointments associated with a specific Doctor based on their ID. " +
                    "This method can be executed by Admin, Chief, Doctor, or Psr with read authority."
    )
    @GetMapping("/getAll/doctor")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','doctor:read','psr:read')")
    public Page<SimpleAppointmentResponse> getAllAppointmentByDoctorId(
            @RequestParam("id") Long id,
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return appointmentService.getAllAppointmentByDoctorId(id, pageable);
    }

    @Operation(
            summary = "Get All In-Progress Appointments by Doctor ID",
            description = "Retrieves a paginated list of all in-progress Appointments associated with a specific Doctor based on their ID. " +
                    "This method can be executed by Admin, Chief, Doctor, or Psr with read authority."
    )
    @GetMapping("/getAll/inProgress/doctor")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','doctor:read','psr:read')")
    public Page<SimpleAppointmentResponse> getAllInProgressAppointmentByDoctorId(
            @RequestParam("id") Long id,
            @PageableDefault(page = 0, size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return appointmentService.getAllInProgressAppointmentByDoctorId(id, pageable);
    }

    @Operation(
            summary = "Get All In-Progress Appointments List by Doctor ID",
            description = "Retrieves a list of all in-progress Appointments associated with a specific Doctor based on their ID. " +
                    "This method can be executed by Admin, Chief, Doctor, or Psr with read authority."
    )
    @GetMapping("/getAll/list/inProgress/doctor")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','doctor:read','psr:read')")
    public List<SimpleAppointmentResponse> getAllInProgressAppointmentListByDoctorId(@RequestParam("id") Long id) {
        return appointmentService.getAllInProgressAppointmentListByDoctorId(id);
    }


    @Operation(
            summary = "Get All Appointments by Patient SSN",
            description = "Retrieves a paginated list of all Appointments associated with a specific Patient based on their SSN. " +
                    "This method can be executed by Admin, Chief, Doctor, or Psr with read authority."
    )
    @GetMapping("/getAll/patient")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','doctor:read','psr:read')")
    public Page<SimpleAppointmentResponse> getAllAppointmentByPatientSsn(
            @RequestParam("ssn") String ssn,
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return appointmentService.getAllAppointmentByPatientSsn(ssn, pageable);
    }

    @Operation(
            summary = "Get All Appointments List by Patient SSN",
            description = "Retrieves a list of all Appointments associated with a specific Patient based on their SSN. " +
                    "This method can be executed by Admin, Chief, Doctor, or Psr with read authority."
    )
    @GetMapping("/getAll/list/patient")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','doctor:read','psr:read')")
    public List<SimpleAppointmentResponse> getAllAppointmentListByPatientSsn(@RequestParam("ssn") String ssn) {
        return appointmentService.getAllAppointmentListByPatientSsn(ssn);
    }

    @Operation(
            summary = "Get All Appointments by Date",
            description = "Retrieves a paginated list of all Appointments scheduled on a specific date. " +
                    "This method can be executed by Admin, Chief, or Psr with read authority."
    )
    @GetMapping("/getAll/date")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','psr:read')")
    public Page<SimpleAppointmentResponse> getAllAppointmentByDate(
            @RequestParam("on") LocalDate date,
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return appointmentService.getAllAppointmentByDate(date, pageable);
    }

    @Operation(
            summary = "Get All Appointments List by Date",
            description = "Retrieves a list of all Appointments scheduled on a specific date. " +
                    "This method can be executed by Admin, Chief, or Psr with read authority."
    )
    @GetMapping("/getAll/list/date")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','psr:read')")
    public List<SimpleAppointmentResponse> getAllAppointmentListByDate(@RequestParam("on") LocalDate date) {
        return appointmentService.getAllAppointmentListByDate(date);
    }

    @Operation(
            summary = "Create Appointment",
            description = "This method creates a new Appointment. " +
                    "It can be executed by Admin or Psr with create authority."
    )
    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('admin:create','psr:create')")
    public ResponseEntity<SimpleAppointmentResponse> saveAppointment(@Valid @RequestBody AppointmentCreationRequest request) {
        return appointmentService.saveAppointment(request);
    }

    @Operation(
            summary = "Update Appointment",
            description = "This method updates an existing Appointment based on its ID. " +
                    "It can be executed by Admin or Psr with update authority."
    )
    @PatchMapping("/update")
    @PreAuthorize("hasAnyAuthority('admin:update','psr:update')")
    public ResponseEntity<DetailedAppointmentResponse> updateAppointment(
            @Valid @RequestBody AppointmentUpdateRequest request, @RequestParam("id") Long id
    ) {
        return appointmentService.updateAppointment(request, id);
    }

    @Operation(
            summary = "Cancel Appointment",
            description = "This method cancels an existing Appointment based on its ID. " +
                    "It can be executed by Admin or Psr with update authority."
    )
    @PatchMapping("/cancel")
    @PreAuthorize("hasAnyAuthority('admin:update','psr:update')")
    public ResponseEntity<ApiResponse> cancelAppointment(@RequestParam("id") Long id) {
        return appointmentService.cancelAppointment(id);
    }

    @Operation(
            summary = "Complete Appointment",
            description = "This method marks an Appointment as complete based on its ID. " +
                    "It can be executed by Admin, Chief, or Doctor with update authority."
    )
    @PatchMapping("/complete")
    @PreAuthorize("hasAnyAuthority('admin:update','chief:update','doctor:update')")
    public ResponseEntity<ApiResponse> completeAppointment(
            @RequestParam("id") Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return appointmentService.completeAppointment(id, userDetails);
    }

    @Operation(
            summary = "Delete Appointment",
            description = "This method deletes an existing Appointment based on its ID. " +
                    "It can be executed by Admin or Psr with delete authority."
    )
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('admin:delete','psr:delete')")
    public ResponseEntity<ApiResponse> deleteAppointment(@RequestParam("id") Long id) {
        return appointmentService.deleteAppointment(id);
    }

    @Operation(
            summary = "Export to Excel all In-Progress Appointments by Doctor ID",
            description = "This method exports to Excel all in-progress Appointments associated with a specific Doctor based on their ID. " +
                    "It can be executed by Admin, Chief, Doctor, or Psr with create authority."
    )
    @PostMapping("/all/export/inProgress/doctor")
    @PreAuthorize("hasAnyAuthority('admin:create','chief:create','doctor:create','psr:create')")
    public ResponseEntity<ApiResponse> getAllInProgressAppointmentByDoctorIdForExport(@RequestParam("id") Long doctorId) {
        return appointmentService.getAllInProgressAppointmentByDoctorIdForExport(doctorId);
    }

    @Operation(
            summary = "Export to Excel all Appointments by Patient SSN",
            description = "This method exports to Excel all Appointments associated with a specific Patient based on their SSN. " +
                    "It can be executed by Admin, Chief, Doctor, or Psr with create authority."
    )
    @PostMapping("/all/export/patient")
    @PreAuthorize("hasAnyAuthority('admin:create','chief:create','doctor:create','psr:create')")
    public ResponseEntity<ApiResponse> getAllAppointmentByPatientSsnForExport(@RequestParam("ssn") String ssn) {
        return appointmentService.getAllAppointmentByPatientSsnForExport(ssn);
    }
    @Operation(
            summary = "Export to Excel all Appointments by Date",
            description = "This method exports to Excel all Appointments based on a specific date. " +
                    "It can be executed by Admin, Chief, or Psr with create authority."
    )
    @PostMapping("/all/export/date")
    @PreAuthorize("hasAnyAuthority('admin:create','chief:create','psr:create')")
    public ResponseEntity<ApiResponse> getAllAppointmentByDateForExport(@RequestParam("on") LocalDate date) {
        return appointmentService.getAllAppointmentByDateForExport(date);
    }

}
