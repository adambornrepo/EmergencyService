package com.tech.controller;

import com.tech.payload.response.ApiResponse;
import com.tech.payload.response.EmployeeResponse;
import com.tech.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(
        name = "EmployeeController",
        description = "Employee Controller is responsible for performing operations related to Employees List"
)
@RestController
@RequestMapping("api/v1/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @Operation(
            summary = "Get All Active Employees",
            description = "Retrieves a list of all active Employees. " +
                    "This method can be executed by Admin or Chief with read authority."
    )
    @GetMapping("/getAll/active")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read')")
    public List<EmployeeResponse> getAllActiveEmployeeList() {
        return employeeService.getAllActiveEmployeeList();
    }

    @Operation(
            summary = "Export All Active Employees",
            description = "Exports the list of all active Employees to Excel. " +
                    "This method can be executed by Admin or Chief with create authority."
    )
    @PostMapping("/getAll/active/export")
    @PreAuthorize("hasAnyAuthority('admin:create','chief:create')")
    public ResponseEntity<ApiResponse> getAllActiveEmployeeForExport() {
        return employeeService.getAllActiveEmployeeForExport();
    }

}
