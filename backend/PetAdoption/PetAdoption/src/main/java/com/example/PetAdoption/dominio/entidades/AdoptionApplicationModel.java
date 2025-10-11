package com.example.PetAdoption.dominio.entidades;
import com.example.PetAdoption.dominio.enums.ApplicationStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

/** Domain Model: sem anotações de framework. */
public class AdoptionApplicationModel {

    private UUID id;
    private UUID adopterId;                 // user_admin.id
    private UUID petId;                     // pets.id

    private ApplicationStatus status = ApplicationStatus.DRAFT;
    private Boolean useDefaultPreferences = Boolean.TRUE;
    private OffsetDateTime submittedAt;

    private UUID reviewedById;              // user_admin.id (opcional)
    private String reviewNotes;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    // ---- getters/setters ----
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getAdopterId() { return adopterId; }
    public void setAdopterId(UUID adopterId) { this.adopterId = adopterId; }

    public UUID getPetId() { return petId; }
    public void setPetId(UUID petId) { this.petId = petId; }

    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; }

    public Boolean getUseDefaultPreferences() { return useDefaultPreferences; }
    public void setUseDefaultPreferences(Boolean useDefaultPreferences) { this.useDefaultPreferences = useDefaultPreferences; }

    public OffsetDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(OffsetDateTime submittedAt) { this.submittedAt = submittedAt; }

    public UUID getReviewedById() { return reviewedById; }
    public void setReviewedById(UUID reviewedById) { this.reviewedById = reviewedById; }

    public String getReviewNotes() { return reviewNotes; }
    public void setReviewNotes(String reviewNotes) { this.reviewNotes = reviewNotes; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}