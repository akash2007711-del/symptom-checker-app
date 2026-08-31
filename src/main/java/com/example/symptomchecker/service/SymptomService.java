package com.example.symptomchecker.service;

import com.example.symptomchecker.model.SymptomAnalysis;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SymptomService {

    private static final Map<String, List<String>> SYMPTOM_KEYWORDS = new HashMap<>();

    static {
        // Emergency symptoms (HIGH urgency)
        SYMPTOM_KEYWORDS.put("HIGH", Arrays.asList(
                "chest pain", "chest discomfort",
                "breathing difficulty", "difficulty breathing", "shortness of breath",
                "unconscious", "unconsciousness", "fainting",
                "severe bleeding", "bleeding heavily",
                "heart attack", "stroke", "cardiac",
                "severe headache", "sudden severe headache",
                "poisoning", "overdose"
        ));

        // Moderate symptoms (MEDIUM urgency)
        SYMPTOM_KEYWORDS.put("MEDIUM", Arrays.asList(
                "fever", "high temperature",
                "cough", "persistent cough",
                "cold", "common cold",
                "flu", "influenza",
                "sore throat", "throat pain",
                "nausea", "vomiting", "vomit",
                "diarrhea", "diarrhoea",
                "rash", "skin rash",
                "ear pain", "earache",
                "eye pain", "eye discomfort",
                "moderate pain"
        ));

        // Minor symptoms (LOW urgency)
        SYMPTOM_KEYWORDS.put("LOW", Arrays.asList(
                "headache", "mild headache",
                "body pain", "muscle pain",
                "fatigue", "tiredness",
                "weakness", "mild weakness",
                "sneezing", "runny nose",
                "stuffy nose", "congestion",
                "mild cough", "slight cough",
                "minor wound", "minor cut"
        ));
    }

    public SymptomAnalysis analyzeSymptoms(String symptoms) {
        if (symptoms == null || symptoms.trim().isEmpty()) {
            throw new IllegalArgumentException("Symptoms cannot be empty");
        }

        String normalizedSymptoms = symptoms.toLowerCase().trim();

        // Check HIGH urgency first
        if (matchesKeywords(normalizedSymptoms, "HIGH")) {
            return new SymptomAnalysis(
                    "⚠️ URGENT: Your symptoms require immediate medical attention!",
                    "HIGH",
                    "Please visit the nearest emergency room or call emergency services immediately.",
                    "EMERGENCY"
            );
        }

        // Check MEDIUM urgency
        if (matchesKeywords(normalizedSymptoms, "MEDIUM")) {
            return new SymptomAnalysis(
                    "Your symptoms may be related to a common illness that requires medical attention.",
                    "MEDIUM",
                    "Please consult a healthcare professional as soon as possible. Visit a nearby clinic or hospital.",
                    "INFECTIOUS"
            );
        }

        // Check LOW urgency
        if (matchesKeywords(normalizedSymptoms, "LOW")) {
            return new SymptomAnalysis(
                    "Your symptoms appear to be minor and may resolve with rest and self-care.",
                    "LOW",
                    "Get adequate rest, stay hydrated, and monitor your symptoms. Visit a doctor if symptoms persist.",
                    "MINOR"
            );
        }

        // Unknown symptoms
        return new SymptomAnalysis(
                "Unable to categorize your symptoms with certainty.",
                "MEDIUM",
                "Please consult a healthcare professional to get a proper diagnosis.",
                "UNKNOWN"
        );
    }

    private boolean matchesKeywords(String symptoms, String urgencyLevel) {
        List<String> keywords = SYMPTOM_KEYWORDS.get(urgencyLevel);
        if (keywords == null) {
            return false;
        }

        for (String keyword : keywords) {
            if (symptoms.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

}