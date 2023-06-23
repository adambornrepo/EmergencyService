package com.tech.mapper;

import com.tech.payload.resource.PrescriptionExportResource;
import com.tech.entites.concretes.Doctor;
import com.tech.entites.concretes.Patient;
import com.tech.entites.concretes.Prescription;
import com.tech.payload.response.detailed.DetailedPrescriptionResponse;
import com.tech.payload.response.simple.SimplePrescriptionResponse;
import com.tech.utils.GeneralUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;


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

    public PrescriptionExportResource buildPrescriptionExportResource(Prescription prescription) {
        Doctor doctor = prescription.getDoctor();
        Patient patient = prescription.getAppointment().getPatient();
        LocalDate appointmentDate = prescription.getAppointment().getAppointmentDate();
        String fileName = String.join(
                "_",
                patient.getFirstName(), patient.getLastName(), appointmentDate.toString()
        );
        List<String> medicines = GeneralUtils.buildMedicineList(prescription.getMedicines());

        return PrescriptionExportResource.builder()
                .doctorFirstName(doctor.getFirstName())
                .doctorLastName(doctor.getLastName())
                .patientSSN(patient.getSsn())
                .patientFirstName(patient.getFirstName())
                .patientLastName(patient.getLastName())
                .patientAge(GeneralUtils.calculateAge(patient.getBirthDate()).toString())
                .patientGender(patient.getGender().name())
                .date(appointmentDate.toString())
                .patientPhoneNum(patient.getPhoneNumber())
                .patientAddress(GeneralUtils.mergeAddressFields(patient.getAddress()))
                .medicines(medicines)
                .fileName(fileName)
                .patientEmail(patient.getEmail())
                .build();
    }


}
