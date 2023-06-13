package com.tech.controller;

import com.tech.payload.request.ProcedureCreationRequest;
import com.tech.payload.request.update.ProcedureUpdateRequest;
import com.tech.payload.response.ApiResponse;
import com.tech.payload.response.ProcedureResponse;
import com.tech.service.ProcedureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/procedure")
@RequiredArgsConstructor
public class ProcedureController {

    private final ProcedureService procedureService;

    @GetMapping("/getAll/appointment")
    public ResponseEntity<List<ProcedureResponse>> getAllProcedureByAppointmentId(@RequestParam("id") Long id) {
        return procedureService.getAllProcedureByAppointmentId(id);
    }

    @GetMapping("/getAll/employee")
    public Page<ProcedureResponse> getAllProcedureByEmployeeId(
            @RequestParam("id") Long id,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "type", defaultValue = "DESC") String type
    ) {
        return procedureService.getAllProcedureByEmployeeId(id, page, size, sort, type);
    }

    @PostMapping("/create")
    public ResponseEntity<ProcedureResponse> saveProcedure(@Valid @RequestBody ProcedureCreationRequest request) {
        return procedureService.saveProcedure(request);
    }

    @PatchMapping("/update")
    public ResponseEntity<ProcedureResponse> updateProcedure(@Valid @RequestBody ProcedureUpdateRequest request, @RequestParam("id") Long id) {
        return procedureService.updateProcedure(request, id);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteProcedure(@RequestParam("id") Long id) {
        return procedureService.deleteProcedure(id);
    }

}
