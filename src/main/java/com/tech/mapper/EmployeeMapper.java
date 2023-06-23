package com.tech.mapper;

import com.tech.entites.abstracts.Employee;
import com.tech.payload.resource.EmployeeExcelResource;
import com.tech.payload.response.EmployeeResponse;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {
    public EmployeeResponse buildEmployeeResponse(Employee employee) {
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

    public EmployeeExcelResource buildEmployeeExcelResource(Employee employee) {
        return EmployeeExcelResource.builder()
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
