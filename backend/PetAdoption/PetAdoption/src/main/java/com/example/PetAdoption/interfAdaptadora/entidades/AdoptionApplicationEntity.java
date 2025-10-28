package com.example.PetAdoption.interfAdaptadora.entidades;
import com.example.PetAdoption.dominio.enums.ApplicationStatus;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "adoption_applications")
public class AdoptionApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // deixa o Hibernate gerar o UUID
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(name = "adopter_id", nullable = false, columnDefinition = "uuid")
    private UUID adopterId;

    @Column(name = "pet_id", nullable = false, columnDefinition = "uuid")
    private UUID petId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 40)
    private ApplicationStatus status = ApplicationStatus.DRAFT;

    @Column(name = "use_default_preferences", nullable = false)
    private boolean useDefaultPreferences = false;

    @Column(name = "submitted_at")
    private Instant submittedAt;

    @Column(name = "reviewed_by", columnDefinition = "uuid")
    private UUID reviewedBy;

    @Column(name = "review_notes")
    private String reviewNotes;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    // Se você NÃO tem coluna 'version' no banco, deixe comentado
    // @Version
    // @Column(name = "version")
    // private Long version;

    @PrePersist
    public void prePersist() {
        Instant now = Instant.now();
        if (createdAt == null) createdAt = now;
        if (updatedAt == null) updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }

    // Getters/Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getAdopterId() { return adopterId; }
    public void setAdopterId(UUID adopterId) { this.adopterId = adopterId; }
    public UUID getPetId() { return petId; }
    public void setPetId(UUID petId) { this.petId = petId; }
    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; }
    public boolean isUseDefaultPreferences() { return useDefaultPreferences; }
    public void setUseDefaultPreferences(boolean useDefaultPreferences) { this.useDefaultPreferences = useDefaultPreferences; }
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