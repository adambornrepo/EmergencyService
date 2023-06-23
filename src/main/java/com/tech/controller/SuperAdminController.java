package com.tech.controller;

import com.tech.entites.enums.UniqueField;
import com.tech.payload.request.register.SuperAdminRegistrationRequest;
import com.tech.payload.request.update.SuperAdminUpdateRequest;
import com.tech.payload.response.ApiResponse;
import com.tech.payload.response.detailed.DetailedSuperAdminResponse;
import com.tech.service.SuperAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "SuperAdminController",
        description = "Super admin is responsible for performing operations related to SuperAdmin and Admin management. " +
                "(sole authority for creating Admins)"
)
@RestController
@RequestMapping("api/v1/super")
@RequiredArgsConstructor
public class SuperAdminController {


    private final SuperAdminService superAdminService;

    @Operation(
            summary = "Get One Super Admin By : ID, SSN or PHONE_NUMBER",
            description = "Retrieves detailed information about a specific Super Admin based on unique field and value. " +
                    "This method can be executed by providing one of the following values: ID, SSN or PHONE_NUMBER."
    )
    @GetMapping("/get")
    @PreAuthorize("hasAuthority('super_admin:read')")
    public ResponseEntity<DetailedSuperAdminResponse> getOneSuperAdmin(
            @RequestParam("in") UniqueField searchIn, @RequestParam("val") String value
    ) {
        return superAdminService.getOneSuperAdminByUniqueField(searchIn, value);
    }

    @Operation(
            summary = "Register Super Admin",
            description = "Registers a new Super Admin."
    )
    @PostMapping("/register")
    @PreAuthorize("hasAuthority('super_admin:create')")
    public ResponseEntity<DetailedSuperAdminResponse> saveSuperAdmin(@Valid @RequestBody SuperAdminRegistrationRequest request) {
        return superAdminService.saveSuperAdmin(request);
    }

    @Operation(
            summary = "Update Super Admin",
            description = "Updates the information of an existing Super Admin based on the specified ID."
    )
    @PatchMapping("/update")
    @PreAuthorize("hasAuthority('super_admin:update')")
    public ResponseEntity<DetailedSuperAdminResponse> updateSuperAdmin(
            @Valid @RequestBody SuperAdminUpdateRequest request, @RequestParam("id") Long id
    ) {
        return superAdminService.updateSuperAdmin(request, id);
    }

    @Operation(
            summary = "Delete Super Admin",
            description = "Deletes a Super Admin based on the specified ID. " +
                    " There must be at least one Super Admin present in the application. " +
                    "If you are the last remaining Super Admin, you cannot delete yourself."
    )
    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('super_admin:delete')")
    public ResponseEntity<ApiResponse> deleteSuperAdmin(@RequestParam("id") Long id) {
        return superAdminService.deleteSuperAdmin(id);
    }


}
