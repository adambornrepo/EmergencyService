package com.tech.mapper;

import com.tech.entites.abstracts.Employee;
import com.tech.payload.resource.EmployeeExcelResource;
import com.tech.payload.response.EmployeeResponse;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {
    public EmployeeResponse buildEmployeeResponse(Employee employee) {
        var builder = EmployeeResponse.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .ssn(employee.getSsn())
                .gender(employee.getGender())
                .phoneNumber(employee.getPhoneNumber())
                .birthDate(employee.getBirthDate())
                .role(employee.getRole());
        if (employee.getAddress() != null) {
            builder.street(employee.getAddress().getStreet())
                    .city(employee.getAddress().getCity())
                    .state(employee.getAddress().getState());
        }
        return builder.build();
    }

    public EmployeeExcelResource buildEmployeeExcelResource(Employee employee) {
        var builder = EmployeeExcelResource.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .ssn(employee.getSsn())
                .gender(employee.getGender())
                .phoneNumber(employee.getPhoneNumber())
                .birthDate(employee.getBirthDate())
                .role(employee.getRole());
        if (employee.getAddress() != null) {
            builder.street(employee.getAddress().getStreet())
                    .city(employee.getAddress().getCity())
                    .state(employee.getAddress().getState());
        }
        return builder.build();
    }
}
