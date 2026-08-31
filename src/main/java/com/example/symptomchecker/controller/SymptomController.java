package com.example.symptomchecker.controller;

import com.example.symptomchecker.dto.ErrorResponse;
import com.example.symptomchecker.dto.SymptomCheckRequest;
import com.example.symptomchecker.dto.SymptomCheckResponse;
import com.example.symptomchecker.model.Hospital;
import com.example.symptomchecker.model.SymptomAnalysis;
import com.example.symptomchecker.service.HospitalService;
import com.example.symptomchecker.service.SymptomService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/symptoms")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SymptomController {

    @Autowired
    private SymptomService symptomService;

    @Autowired
    private HospitalService hospitalService;

    @PostMapping("/check")
    public ResponseEntity<?> checkSymptoms(@Valid @RequestBody SymptomCheckRequest request) {
        try {
            // Normalize symptoms input
            String normalizedSymptoms = request.getSymptoms().toLowerCase().trim();

            // Analyze symptoms
            SymptomAnalysis analysis = symptomService.analyzeSymptoms(normalizedSymptoms);

            // Find nearby hospitals based on urgency
            List<Hospital> hospitals = hospitalService.findByLocationAndUrgency(
                    request.getLocation(),
                    analysis.getUrgency()
            );

            // If no hospitals found in location, get all hospitals
            if (hospitals.isEmpty()) {
                hospitals = hospitalService.findByUrgency(analysis.getUrgency());
            }

            // Build response
            SymptomCheckResponse response = new SymptomCheckResponse(
                    analysis.getResult(),
                    analysis.getUrgency(),
                    analysis.getAdvice(),
                    hospitals,
                    System.currentTimeMillis()
            );

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(e.getMessage(), "INVALID_INPUT", 400, System.currentTimeMillis()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred while processing your request",
                            "INTERNAL_ERROR", 500, System.currentTimeMillis()));
        }
    }

    @GetMapping("/hospitals")
    public ResponseEntity<?> getAllHospitals() {
        try {
            List<Hospital> hospitals = hospitalService.getAllHospitals();
            return ResponseEntity.ok(hospitals);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Failed to fetch hospitals",
                            "FETCH_ERROR", 500, System.currentTimeMillis()));
        }
    }

    @GetMapping("/hospitals/location/{location}")
    public ResponseEntity<?> getHospitalsByLocation(@PathVariable String location) {
        try {
            List<Hospital> hospitals = hospitalService.findNearby(location);
            return ResponseEntity.ok(hospitals);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Failed to fetch hospitals for location: " + location,
                            "FETCH_ERROR", 500, System.currentTimeMillis()));
        }
    }

    @PostMapping("/hospitals/add")
    public ResponseEntity<?> addHospital(@Valid @RequestBody Hospital hospital) {
        try {
            Hospital savedHospital = hospitalService.addHospital(hospital);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedHospital);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Failed to add hospital",
                            "ADD_ERROR", 500, System.currentTimeMillis()));
        }
    }

}