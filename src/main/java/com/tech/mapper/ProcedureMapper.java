package com.tech.mapper;

import com.tech.entites.concretes.Procedure;
import com.tech.payload.resource.ProcedureExcelResource;
import com.tech.payload.response.ProcedureResponse;
import com.tech.utils.GeneralUtils;
import org.springframework.stereotype.Component;

@Component
public class ProcedureMapper {

    public ProcedureResponse buildProcedureResponse(Procedure procedure) {
        return ProcedureResponse.builder()
                .id(procedure.getId())
                .doctorNote(procedure.getDoctorNote())
                .applied(procedure.getApplied())
                .appointmentId(procedure.getAppointment().getId())
                .employeeId(procedure.getEmployee().getId())
                .employeeFirstName(procedure.getEmployee().getFirstName())
                .employeeLastName(procedure.getEmployee().getLastName())
                .createdAt(GeneralUtils.convertMillisToLocalDateTime(procedure.getCreatedAt()))
                .updatedAt(GeneralUtils.convertMillisToLocalDateTime(procedure.getUpdatedAt()))
                .isDisabled(procedure.isDisabled())
                .build();
    }

    public ProcedureExcelResource buildProcedureExcelResource(Procedure procedure) {
        return ProcedureExcelResource.builder()
                .id(procedure.getId())
                .doctorNote(procedure.getDoctorNote())
                .applied(procedure.getApplied())
                .appointmentId(procedure.getAppointment().getId())
                .employeeId(procedure.getEmployee().getId())
                .employeeFirstName(procedure.getEmployee().getFirstName())
                .employeeLastName(procedure.getEmployee().getLastName())
                .createdAt(GeneralUtils.convertMillisToLocalDateTime(procedure.getCreatedAt()))
                .updatedAt(GeneralUtils.convertMillisToLocalDateTime(procedure.getUpdatedAt()))
                .isDisabled(procedure.isDisabled())
                .build();
    }
}
