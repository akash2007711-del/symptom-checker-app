package com.example.symptomchecker.service;

import com.example.symptomchecker.model.Hospital;
import com.example.symptomchecker.repository.HospitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;

    public List<Hospital> findNearby(String location) {
        if (location == null || location.trim().isEmpty()) {
            return hospitalRepository.findAll();
        }

        List<Hospital> hospitals = hospitalRepository.findByLocation(location);
        return hospitals.isEmpty() ? hospitalRepository.findAll() : hospitals;
    }

    public List<Hospital> findByUrgency(String urgency) {
        if ("HIGH".equals(urgency)) {
            return hospitalRepository.findByEmergency24(true);
        }
        return hospitalRepository.findAll();
    }

    public List<Hospital> findByLocationAndUrgency(String location, String urgency) {
        if ("HIGH".equals(urgency)) {
            return hospitalRepository.findByLocationAndEmergency24(location, true);
        }
        return findNearby(location);
    }

    public Hospital addHospital(Hospital hospital) {
        return hospitalRepository.save(hospital);
    }

    public List<Hospital> getAllHospitals() {
        return hospitalRepository.findAll();
    }

}