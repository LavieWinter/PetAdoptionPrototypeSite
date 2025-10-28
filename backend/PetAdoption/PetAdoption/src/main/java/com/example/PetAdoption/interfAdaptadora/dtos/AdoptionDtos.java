package com.example.PetAdoption.interfAdaptadora.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class AdoptionDtos {

    public static class RecommendationItem {
        public String petId;
        public String name;
        public String species;
        public String size;
        public Integer score;
    }

    public static class RecommendationResponse {
        public List<RecommendationItem> items;
        public int limit;
        public int offset;
    }

    public static class CreateApplicationRequest {
        @NotNull public String petId;
        public Boolean useDefaultPreferences = Boolean.TRUE; // default = true
    }

    public static class CreateApplicationResponse {
        public String applicationId;
    }

    public static class ConfirmResponse {
        public String status; // "OK"
    }
}