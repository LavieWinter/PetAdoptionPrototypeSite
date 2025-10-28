package com.example.PetAdoption.interfAdaptadora.entidades;

import com.example.PetAdoption.dominio.enums.AdoptionStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "adoptions")
public class AdoptionEntity {

    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "application_id", nullable = false)
    private UUID applicationId;

    @Column(name = "pet_id", nullable = false)
    private UUID petId;

    @Column(name = "tutor_id", nullable = false)
    private UUID tutorId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AdoptionStatus status = AdoptionStatus.ACTIVE;

    @Column(name = "adoption_date")
    private LocalDate adoptionDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "end_reason")
    private String endReason;

    @Column(name = "created_at")
    private Instant createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = Instant.now();
        if (adoptionDate == null) adoptionDate = LocalDate.now();
    }

    // Getters/Setters
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
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}