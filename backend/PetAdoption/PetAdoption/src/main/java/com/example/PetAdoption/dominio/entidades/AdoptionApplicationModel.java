package com.example.PetAdoption.dominio.entidades;


import com.example.PetAdoption.dominio.enums.ApplicationStatus;

import java.time.Instant;
import java.util.UUID;

public class AdoptionApplicationModel {
    private UUID id;
    private UUID adopterId;
    private UUID petId;

    private ApplicationStatus status;
    private Boolean useDefaultPreferences;

    private Instant submittedAt;
    private UUID reviewedBy;
    private String reviewNotes;

    private Instant createdAt;
    private Instant updatedAt;

    // Getters/Setters
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
    public Instant getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(Instant submittedAt) { this.submittedAt = submittedAt; }
    public UUID getReviewedBy() { return reviewedBy; }
    public void setReviewedBy(UUID reviewedBy) { this.reviewedBy = reviewedBy; }
    public String getReviewNotes() { return reviewNotes; }
    public void setReviewNotes(String reviewNotes) { this.reviewNotes = reviewNotes; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}