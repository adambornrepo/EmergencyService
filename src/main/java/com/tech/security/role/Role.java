package com.tech.security.role;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.tech.security.role.Permission.*;

@RequiredArgsConstructor
public enum Role {
    SUPER_ADMIN(
            Set.of(
                    SUPER_ADMIN_READ,
                    SUPER_ADMIN_UPDATE,
                    SUPER_ADMIN_DELETE,
                    SUPER_ADMIN_CREATE

            )
    ),
    ADMIN(
            Set.of(
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_DELETE,
                    ADMIN_CREATE,
                    CHIEF_READ,
                    CHIEF_UPDATE,
                    CHIEF_DELETE,
                    CHIEF_CREATE,
                    DOCTOR_READ,
                    DOCTOR_UPDATE,
                    DOCTOR_DELETE,
                    DOCTOR_CREATE,
                    NURSE_READ,
                    NURSE_UPDATE,
                    NURSE_DELETE,
                    NURSE_CREATE,
                    LAB_TECHNICIAN_READ,
                    LAB_TECHNICIAN_UPDATE,
                    LAB_TECHNICIAN_DELETE,
                    LAB_TECHNICIAN_CREATE,
                    RADIOLOGY_TECHNICIAN_READ,
                    RADIOLOGY_TECHNICIAN_UPDATE,
                    RADIOLOGY_TECHNICIAN_DELETE,
                    RADIOLOGY_TECHNICIAN_CREATE,
                    PSR_READ,
                    PSR_UPDATE,
                    PSR_DELETE,
                    PSR_CREATE
            )
    ),
    CHIEF(
            Set.of(
                    CHIEF_READ,
                    CHIEF_UPDATE,
                    CHIEF_DELETE,
                    CHIEF_CREATE,
                    DOCTOR_READ,
                    DOCTOR_UPDATE,
                    DOCTOR_DELETE,
                    DOCTOR_CREATE
            )
    ),
    DOCTOR(
            Set.of(
                    DOCTOR_READ,
                    DOCTOR_UPDATE,
                    DOCTOR_DELETE,
                    DOCTOR_CREATE
            )
    ),
    NURSE(
            Set.of(
                    NURSE_READ,
                    NURSE_UPDATE,
                    NURSE_DELETE,
                    NURSE_CREATE
            )
    ),
    LAB_TECHNICIAN(
            Set.of(
                    LAB_TECHNICIAN_READ,
                    LAB_TECHNICIAN_UPDATE,
                    LAB_TECHNICIAN_DELETE,
                    LAB_TECHNICIAN_CREATE
            )
    ),
    RADIOLOGY_TECHNICIAN(
            Set.of(
                    RADIOLOGY_TECHNICIAN_READ,
                    RADIOLOGY_TECHNICIAN_UPDATE,
                    RADIOLOGY_TECHNICIAN_DELETE,
                    RADIOLOGY_TECHNICIAN_CREATE
            )
    ),
    PSR(
            Set.of(
                    PSR_READ,
                    PSR_UPDATE,
                    PSR_DELETE,
                    PSR_CREATE
            )
    ),
    PATIENT(Collections.emptySet());

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
