package com.example.PetAdoption.dominio.entidades;

import com.example.PetAdoption.dominio.enums.AdoptionStatus;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

/** Domain Model: sem JPA/Spring. */
public class AdoptionModel {

    private UUID id;
    private UUID applicationId;   // FK -> adoption_applications.id
    private UUID petId;           // FK -> pets.id
    private UUID tutorId;         // FK -> user_admin.id

    private AdoptionStatus status = AdoptionStatus.ACTIVE;

    private LocalDate adoptionDate;
    private LocalDate endDate;
    private String endReason;

    private OffsetDateTime createdAt;

    // ---- getters/setters ----
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getApplicationId() { return applicationId; }
    public void setApplicationId(UUID applicationId) { this.applicationId = applicationId; }

    public UUID getPetId() { return petId; }
    public void setPetId(UUID petId) { this.petId = petId; }

    public UUID getTutorId() { return tutorId; }
    public void setTutorId(UUID tutorId) { this.tutorId = tutorId; }

    public AdoptionStatus getStatus() { return status; }
    public void setStatus(AdoptionStatus status) { this.status = status; }

    public LocalDate getAdoptionDate() { return adoptionDate; }
    public void setAdoptionDate(LocalDate adoptionDate) { this.adoptionDate = adoptionDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getEndReason() { return endReason; }
    public void setEndReason(String endReason) { this.endReason = endReason; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}