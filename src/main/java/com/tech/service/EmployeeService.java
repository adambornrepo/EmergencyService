package com.tech.service;

import com.tech.configuration.ApiMessages;
import com.tech.entites.abstracts.Employee;
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
    public List<EmployeeResponse> getAllActiveEmployeeList() {
        return coordinationService.getAllActiveEmployeeList()
                .stream()
                .map(this::buildEmployeeResponse)
                .collect(Collectors.toList());
    }
    public ResponseEntity<ApiResponse> getAllActiveEmployeeForExport() {
        var exportData = getAllActiveEmployeeList();

        String date = LocalDate.now().toString();
        excelWriteService.writeAppointmentsToExcel(
                exportData,
                String.join("_", "Active", "Employee", date),
                "DATE = " + date
        );
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message(apiMessages.getMessage("success.export.completed"))
                        .build()
        );
    }
    private EmployeeResponse buildEmployeeResponse(Employee employee) {
        return EmployeeResponse.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .ssn(employee.getSsn())
                .gender(employee.getGender())
                .phoneNumber(employee.getPhoneNumber())
                .birthDate(employee.getBirthDate())
                .street(employee.getAddress().getStreet())
                .city(employee.getAddress().getCity())
                .state(employee.getAddress().getState())
                .role(employee.getRole())
                .build();
    }


}
