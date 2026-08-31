package com.example.symptomchecker.config;

import com.example.symptomchecker.model.Hospital;
import com.example.symptomchecker.repository.HospitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private HospitalRepository hospitalRepository;

    @Override
    public void run(String... args) throws Exception {
        // Initialize sample hospitals
        hospitalRepository.save(new Hospital(
                null,
                "Government General Hospital",
                "Karur",
                "Government Hospital",
                "Main Street, Karur",
                "+91-4324-XXXXXX",
                "info@ggh.karur.gov.in",
                10.9157,
                78.1239,
                true
        ));

        hospitalRepository.save(new Hospital(
                null,
                "Primary Health Centre",
                "Punnam Chathiram",
                "PHC",
                "PHC Road, Punnam Chathiram",
                "+91-4325-XXXXXX",
                "phc@punnam.gov.in",
                10.9300,
                78.1100,
                false
        ));

        hospitalRepository.save(new Hospital(
                null,
                "Government Hospital",
                "Erode",
                "Government Hospital",
                "Hospital Street, Erode",
                "+91-4294-XXXXXX",
                "info@gh.erode.gov.in",
                11.3411,
                79.1104,
                true
        ));

        hospitalRepository.save(new Hospital(
                null,
                "Apollo Hospital",
                "Karur",
                "Private Hospital",
                "Apollo Complex, Karur",
                "+91-4324-999999",
                "apollo@karur.com",
                10.9200,
                78.1250,
                true
        ));

        hospitalRepository.save(new Hospital(
                null,
                "Fortis Healthcare",
                "Erode",
                "Private Hospital",
                "Fortis Plaza, Erode",
                "+91-4294-888888",
                "fortis@erode.com",
                11.3450,
                79.1150,
                true
        ));

        System.out.println("Sample hospitals initialized successfully!");
    }

}