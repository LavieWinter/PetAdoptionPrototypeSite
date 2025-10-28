package com.example.PetAdoption.interfAdaptadora.dtos;
import com.example.PetAdoption.dominio.entidades.AdoptionApplicationModel;

import java.util.List;
import java.util.UUID;

public class AdoptionDtos {

    public static class CreateRequest {
        public UUID petId;
        public Boolean useDefaultPreferences = Boolean.TRUE;
    }

    public static class ApplicationResponse {
        public UUID id;
        public UUID adopterId;
        public UUID petId;
        public String status;
        public Boolean useDefaultPreferences;
        public String createdAt;
        public String updatedAt;
        public String submittedAt;

        public static ApplicationResponse of(AdoptionApplicationModel m) {
            var r = new ApplicationResponse();
            r.id = m.getId();
            r.adopterId = m.getAdopterId();
            r.petId = m.getPetId();
            r.status = m.getStatus().name();
            r.useDefaultPreferences = m.getUseDefaultPreferences();
            r.createdAt = m.getCreatedAt() != null ? m.getCreatedAt().toString() : null;
            r.updatedAt = m.getUpdatedAt() != null ? m.getUpdatedAt().toString() : null;
            r.submittedAt = m.getSubmittedAt() != null ? m.getSubmittedAt().toString() : null;
            return r;
        }
    }

    public static class PageResponse {
        public int page;
        public int size;
        public long totalElements;
        public int totalPages;
        public List<ApplicationResponse> content;
    }
}