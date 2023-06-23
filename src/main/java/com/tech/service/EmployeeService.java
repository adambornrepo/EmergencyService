package com.tech.service;

import com.tech.configuration.ApiMessages;
import com.tech.entites.abstracts.Employee;
import com.tech.mapper.EmployeeMapper;
import com.tech.payload.resource.EmployeeExcelResource;
import com.tech.payload.response.ApiResponse;
import com.tech.payload.response.EmployeeResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final CheckAndCoordinationService coordinationService;
    private final ExcelWriteService excelWriteService;
    private final ApiMessages apiMessages;
    private final EmployeeMapper employeeMapper;

    public List<EmployeeResponse> getAllActiveEmployeeList() {
        return coordinationService.getAllActiveEmployeeList()
                .stream()
                .map(employeeMapper::buildEmployeeResponse)
                .collect(Collectors.toList());
    }

    private List<EmployeeExcelResource> getAllActiveEmployeeListForExport() {
        return coordinationService.getAllActiveEmployeeList()
                .stream()
                .map(employeeMapper::buildEmployeeExcelResource)
                .collect(Collectors.toList());
    }

    public ResponseEntity<ApiResponse> getAllActiveEmployeeForExport() {
        var exportData = getAllActiveEmployeeList();

        String date = LocalDate.now().toString();
        excelWriteService.writeToExcel(
                exportData,
                String.join("_", "Active", "Employee", date),
                "DATE = " + date
        );
        log.warn("All active Employees' info exported to Excel on date: {}", date);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message(apiMessages.getMessage("success.export.completed"))
                        .build()
        );
    }

}
