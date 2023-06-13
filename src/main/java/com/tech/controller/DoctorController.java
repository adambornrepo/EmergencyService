package com.tech.controller;

import com.tech.entites.enums.UniqueField;
import com.tech.payload.request.register.DoctorRegistrationRequest;
import com.tech.payload.request.update.DoctorUpdateRequest;
import com.tech.payload.response.ApiResponse;
import com.tech.payload.response.detailed.DetailedDoctorResponse;
import com.tech.payload.response.simple.SimpleDoctorResponse;
import com.tech.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dr")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping("/get")
    public ResponseEntity<DetailedDoctorResponse> getOneDoctor(@RequestParam("in") UniqueField searchIn, @RequestParam("val")String value) {
        return doctorService.getOneDoctorByUniqueField(searchIn, value);
    }

    @GetMapping("/getAll")
    public Page<SimpleDoctorResponse> getAllDoctor(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "ssn") String sort,
            @RequestParam(value = "type", defaultValue = "ASC") String type
    ) {
        return doctorService.getAllDoctor(page, size, sort, type);
    }

    @PostMapping("/register")
    public ResponseEntity<DetailedDoctorResponse> saveDoctor(@Valid @RequestBody DoctorRegistrationRequest request) {
        return doctorService.saveDoctor(request);
    }

    @PatchMapping("/update")
    public ResponseEntity<DetailedDoctorResponse> updateDoctor(@Valid @RequestBody DoctorUpdateRequest request, @RequestParam("id") Long id) {
        return doctorService.updateDoctor(request, id);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteDoctor(@RequestParam("id") Long id) {
        return doctorService.deleteDoctor(id);
    }


}
