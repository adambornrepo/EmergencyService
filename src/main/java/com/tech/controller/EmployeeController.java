package com.tech.controller;

import com.tech.payload.response.ApiResponse;
import com.tech.payload.response.EmployeeResponse;
import com.tech.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/getAll/active")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read')")
    public List<EmployeeResponse> getAllActiveEmployeeList() {
        return employeeService.getAllActiveEmployeeList();
    }

    @GetMapping("/getAll/active/export")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read')")
    public ResponseEntity<ApiResponse> getAllActiveEmployeeForExport() {
        return employeeService.getAllActiveEmployeeForExport();
    }

}
