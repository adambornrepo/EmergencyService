package com.tech.mapper;

import com.tech.entites.concretes.Prescription;
import com.tech.payload.response.detailed.DetailedPrescriptionResponse;
import com.tech.payload.response.simple.SimplePrescriptionResponse;
import com.tech.utils.GeneralUtils;
import org.springframework.stereotype.Component;


@Component
public class PrescriptionMapper {

    public DetailedPrescriptionResponse buildDetailedPrescriptionResponse(Prescription prescription) {
        var builder = DetailedPrescriptionResponse.builder()
                .id(prescription.getId())
                .createdAt(GeneralUtils.convertMillisToLocalDateTime(prescription.getCreatedAt()))
                .medicines(GeneralUtils.buildMedicineList(prescription.getMedicines()));

        if (prescription.getAppointment() != null) {
            builder.appointmentId(prescription.getAppointment().getId());
            builder.patientSsn(prescription.getAppointment().getPatient().getSsn());
            builder.patientFirstName(prescription.getAppointment().getPatient().getFirstName());
            builder.patientLastName(prescription.getAppointment().getPatient().getLastName());
        }
        if (prescription.getDoctor() != null) {
            builder.doctorId(prescription.getDoctor().getId());
            builder.doctorFirstName(prescription.getDoctor().getFirstName());
            builder.doctorLastName(prescription.getDoctor().getLastName());
        }
        return builder.build();
    }

    public SimplePrescriptionResponse buildSimplePrescriptionResponse(Prescription prescription) {
        var builder = SimplePrescriptionResponse.builder()
                .id(prescription.getId());

        if (prescription.getAppointment() != null) {
            builder.appointmentId(prescription.getAppointment().getId());
            builder.patientSsn(prescription.getAppointment().getPatient().getSsn());
        }
        if (prescription.getDoctor() != null) {
            builder.doctorId(prescription.getDoctor().getId());
            builder.doctorFirstName(prescription.getDoctor().getFirstName());
            builder.doctorLastName(prescription.getDoctor().getLastName());
        }
        return builder.build();
    }


}
