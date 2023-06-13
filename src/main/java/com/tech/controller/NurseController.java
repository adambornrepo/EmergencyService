package com.tech.controller;

import com.tech.entites.enums.UniqueField;
import com.tech.payload.request.register.NurseRegistrationRequest;
import com.tech.payload.request.update.NurseUpdateRequest;
import com.tech.payload.response.*;
import com.tech.payload.response.detailed.DetailedNurseResponse;
import com.tech.payload.response.simple.SimpleNurseResponse;
import com.tech.service.NurseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/nurse")
@RequiredArgsConstructor
public class NurseController {

    private final NurseService nurseService;

    @GetMapping("/get")
    public ResponseEntity<DetailedNurseResponse> getOneNurse(@RequestParam("in") UniqueField searchIn, @RequestParam("val")String value) {
        return nurseService.getOneNurseByUniqueField(searchIn, value);
    }

    @GetMapping("/getAll")
    public Page<SimpleNurseResponse> getAllNurse(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "ssn") String sort,
            @RequestParam(value = "type", defaultValue = "ASC") String type
    ) {
        return nurseService.getAllNurse(page, size, sort, type);
    }

    @PostMapping("/register")
    public ResponseEntity<DetailedNurseResponse> saveNurse(@Valid @RequestBody NurseRegistrationRequest request) {
        return nurseService.saveNurse(request);
    }

    @PatchMapping("/update")
    public ResponseEntity<DetailedNurseResponse> updateNurse(@Valid @RequestBody NurseUpdateRequest request, @RequestParam("id") Long id) {
        return nurseService.updateNurse(request, id);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteNurse(@RequestParam("id") Long id) {
        return nurseService.deleteNurse(id);
    }


}
