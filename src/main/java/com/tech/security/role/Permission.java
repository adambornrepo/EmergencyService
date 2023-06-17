package com.tech.security.role;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {
    SUPER_ADMIN_READ("super_admin:read"),
    SUPER_ADMIN_UPDATE("super_admin:update"),
    SUPER_ADMIN_CREATE("super_admin:create"),
    SUPER_ADMIN_DELETE("super_admin:delete"),
    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),
    CHIEF_READ("chief:read"),
    CHIEF_UPDATE("chief:update"),
    CHIEF_CREATE("chief:create"),
    CHIEF_DELETE("chief:delete"),
    DOCTOR_READ("doctor:read"),
    DOCTOR_UPDATE("doctor:update"),
    DOCTOR_CREATE("doctor:create"),
    DOCTOR_DELETE("doctor:delete"),
    NURSE_READ("nurse:read"),
    NURSE_UPDATE("nurse:update"),
    NURSE_CREATE("nurse:create"),
    NURSE_DELETE("nurse:delete"),
    LAB_TECHNICIAN_READ("lab_tech:read"),
    LAB_TECHNICIAN_UPDATE("lab_tech:update"),
    LAB_TECHNICIAN_CREATE("lab_tech:create"),
    LAB_TECHNICIAN_DELETE("lab_tech:delete"),
    RADIOLOGY_TECHNICIAN_READ("radiology_tech:read"),
    RADIOLOGY_TECHNICIAN_UPDATE("radiology_tech:update"),
    RADIOLOGY_TECHNICIAN_CREATE("radiology_tech:create"),
    RADIOLOGY_TECHNICIAN_DELETE("radiology_tech:delete"),
    PSR_READ("psr:read"),
    PSR_UPDATE("psr:update"),
    PSR_CREATE("psr:create"),
    PSR_DELETE("psr:delete");

    @Getter
    private final String permission;
}
