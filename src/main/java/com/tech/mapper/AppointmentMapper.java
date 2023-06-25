package com.tech.mapper;

import com.tech.entites.concretes.Appointment;
import com.tech.entites.concretes.Procedure;
import com.tech.payload.resource.AppointmentExcelResource;
import com.tech.payload.response.detailed.DetailedAppointmentResponse;
import com.tech.payload.response.simple.SimpleAppointmentResponse;
import com.tech.utils.GeneralUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AppointmentMapper {

    public DetailedAppointmentResponse buildDetailedAppointmentResponse(Appointment appointment) {
        var builder = DetailedAppointmentResponse.builder()
                .appointment_id(appointment.getId())
                .symptoms(appointment.getSymptoms())
                .appointmentDate(appointment.getAppointmentDate())
                .status(appointment.getStatus())
                .createdAt(GeneralUtils.convertMillisToLocalDateTime(appointment.getCreatedAt()));

        if (appointment.getRepresentative() != null) {
            builder.representativeId(appointment.getRepresentative().getId());
        }
        if (appointment.getDoctor() != null) {
            builder.doctorId(appointment.getDoctor().getId());
            builder.doctorFirstName(appointment.getDoctor().getFirstName());
            builder.doctorLastName(appointment.getDoctor().getLastName());
        }
        if (appointment.getPatient() != null) {
            builder.patientSsn(appointment.getPatient().getSsn());
            builder.patientFirstName(appointment.getPatient().getFirstName());
            builder.patientLastName(appointment.getPatient().getLastName());
        }
        if (appointment.getProcedures() != null) {
            builder.procedures(buildProcedureSummary(appointment.getProcedures()));
        }
        if (appointment.getPrescription() != null) {
            builder.prescriptionId(appointment.getPrescription().getId());
        }
        if (appointment.getPrescription() != null) {
            builder.medicines(GeneralUtils.buildMedicineList(appointment.getPrescription().getMedicines()));
        }
        return builder.build();
    }

    public SimpleAppointmentResponse buildSimpleAppointmentResponse(Appointment appointment) {
        var builder = SimpleAppointmentResponse.builder()
                .appointment_id(appointment.getId())
                .symptoms(appointment.getSymptoms())
                .appointmentDate(appointment.getAppointmentDate())
                .status(appointment.getStatus());

        if (appointment.getDoctor() != null) {
            builder.doctorId(appointment.getDoctor().getId());
            builder.doctorFirstName(appointment.getDoctor().getFirstName());
            builder.doctorLastName(appointment.getDoctor().getLastName());
        }
        if (appointment.getPatient() != null) {
            builder.patientSsn(appointment.getPatient().getSsn());
            builder.patientFirstName(appointment.getPatient().getFirstName());
            builder.patientLastName(appointment.getPatient().getLastName());
        }
        return builder.build();
    }

    public AppointmentExcelResource buildAppointmentExcelResource(Appointment appointment) {
        var builder = AppointmentExcelResource.builder()
                .appointment_id(appointment.getId())
                .symptoms(appointment.getSymptoms())
                .appointmentDate(appointment.getAppointmentDate())
                .status(appointment.getStatus());

        if (appointment.getDoctor() != null) {
            builder.doctorId(appointment.getDoctor().getId());
            builder.doctorFirstName(appointment.getDoctor().getFirstName());
            builder.doctorLastName(appointment.getDoctor().getLastName());
        }
        if (appointment.getPatient() != null) {
            builder.patientSsn(appointment.getPatient().getSsn());
            builder.patientFirstName(appointment.getPatient().getFirstName());
            builder.patientLastName(appointment.getPatient().getLastName());
        }
        return builder.build();
    }

    private Map<Long, String> buildProcedureSummary(List<Procedure> procedures) {
        return procedures.stream().collect(
                Collectors.toMap(
                        Procedure::getId,
                        procedure -> procedure.getApplied() != null ? procedure.getApplied() : procedure.getStatus().name()
                )
        );
    }


}
