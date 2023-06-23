package com.tech.controller;

import com.tech.entites.enums.UniqueField;
import com.tech.payload.request.register.RepresentativeRegistrationRequest;
import com.tech.payload.request.update.RepresentativeUpdateRequest;
import com.tech.payload.response.*;
import com.tech.payload.response.detailed.DetailedRepresentativeResponse;
import com.tech.payload.response.simple.SimpleRepresentativeResponse;
import com.tech.service.RepresentativeService;
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
        name = "RepresentativeController",
        description = "Representative(Patient Service Representative [PSR]) " +
                "Controller is responsible for performing operations related to Representatives."
)
@RestController
@RequestMapping("api/v1/psr")
@RequiredArgsConstructor
public class RepresentativeController {

    private final RepresentativeService representativeService;

    @Operation(
            summary = "Get Representative",
            description = "Retrieves detailed information about a specific Representative based on unique field and value. " +
                    "This method can be executed by Admin, Chief, or Psr with read authority. " +
                    "This method can be executed by providing one of the following values: ID, SSN or PHONE_NUMBER"
    )
    @GetMapping("/get")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','psr:read')")
    public ResponseEntity<DetailedRepresentativeResponse> getOneRepresentative(
            @RequestParam("in") UniqueField searchIn, @RequestParam("val") String value
    ) {
        return representativeService.getOneRepresentativeByUniqueField(searchIn, value);
    }

    @Operation(
            summary = "Get All Representatives",
            description = "Retrieves a paginated list of all Representatives. " +
                    "This method can be executed by Admin or Chief with read authority."
    )
    @GetMapping("/getAll")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read')")
    public Page<SimpleRepresentativeResponse> getAllRepresentative(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "ssn") String sort,
            @RequestParam(value = "type", defaultValue = "ASC") String type
    ) {
        return representativeService.getAllRepresentative(page, size, sort, type);
    }

    @Operation(
            summary = "Get All Active Representatives",
            description = "Retrieves a list of all active Representatives. " +
                    "This method can be executed by Admin, Chief, or Psr with read authority."
    )
    @GetMapping("/getAll/active")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','psr:read')")
    public List<SimpleRepresentativeResponse> getAllActiveRepresentative() {
        return representativeService.getAllActiveRepresentative();
    }

    @Operation(
            summary = "Register Representative",
            description = "Registers a new Representative. " +
                    "This method can be executed by Admin or Chief with create authority."
    )
    @PostMapping("/register")
    @PreAuthorize("hasAnyAuthority('admin:create','chief:create')")
    public ResponseEntity<DetailedRepresentativeResponse> saveRepresentative(@Valid @RequestBody RepresentativeRegistrationRequest request) {
        return representativeService.saveRepresentative(request);
    }

    @Operation(
            summary = "Update Representative",
            description = "Updates the information of an existing Representative based on the specified ID. " +
                    "This method can be executed by Admin or Psr with update authority. " +
                    "Admin has authority over all Representative accounts, while Psr can only update their own account."
    )
    @PatchMapping("/update")
    @PreAuthorize("hasAnyAuthority('admin:update','psr:update')")
    public ResponseEntity<DetailedRepresentativeResponse> updateRepresentative(
            @Valid @RequestBody RepresentativeUpdateRequest request,
            @RequestParam("id") Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return representativeService.updateRepresentative(request, id, userDetails);
    }

    @Operation(
            summary = "Delete Representative",
            description = "Deletes a Representative based on the specified ID. " +
                    "This method can be executed by Admin, Chief, or Psr with delete authority. " +
                    "Admin has authority over all Representative accounts, while Psr can only delete their own account."
    )
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('admin:delete','chief:delete','psr:delete')")
    public ResponseEntity<ApiResponse> deleteRepresentative(@RequestParam("id") Long id, @AuthenticationPrincipal UserDetails userDetails) {
        return representativeService.deleteRepresentative(id, userDetails);
    }


}
