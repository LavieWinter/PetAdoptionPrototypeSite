package com.example.PetAdoption.interfAdaptadora.entidades;

import com.example.PetAdoption.dominio.entidades.AdoptionApplicationModel;
import com.example.PetAdoption.dominio.enums.ApplicationStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "adoption_applications")
public class AdoptionApplication {

    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "adopter_id", nullable = false)
    private UUID adopterId;

    @Column(name = "pet_id", nullable = false)
    private UUID petId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ApplicationStatus status = ApplicationStatus.DRAFT;

    @Column(name = "use_default_preferences", nullable = false)
    private Boolean useDefaultPreferences = Boolean.TRUE;

    @Column(name = "submitted_at", columnDefinition = "timestamptz")
    private OffsetDateTime submittedAt;

    @Column(name = "reviewed_by")
    private UUID reviewedById;

    @Column(name = "review_notes")
    private String reviewNotes;

    @Column(name = "created_at", columnDefinition = "timestamptz")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "timestamptz")
    private OffsetDateTime updatedAt;

    protected AdoptionApplication() {}

    @PrePersist
    void onCreate(){ createdAt = OffsetDateTime.now(); updatedAt = createdAt; }

    @PreUpdate
    void onUpdate(){ updatedAt = OffsetDateTime.now(); }

    // ---- getters públicos ----
    public UUID getId() { return id; }
    public UUID getAdopterId() { return adopterId; }
    public UUID getPetId() { return petId; }
    public ApplicationStatus getStatus() { return status; }
    public Boolean getUseDefaultPreferences() { return useDefaultPreferences; }
    public OffsetDateTime getSubmittedAt() { return submittedAt; }
    public UUID getReviewedById() { return reviewedById; }
    public String getReviewNotes() { return reviewNotes; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }

    // ---- conversores  ----
    public static AdoptionApplication fromModel(AdoptionApplicationModel m){
        if (m == null) return null;
        var e = new AdoptionApplication();
        e.id = m.getId();
        e.adopterId = m.getAdopterId();
        e.petId = m.getPetId();
        e.status = m.getStatus();
        e.useDefaultPreferences = m.getUseDefaultPreferences();
        e.submittedAt = m.getSubmittedAt();
        e.reviewedById = m.getReviewedById();
        e.reviewNotes = m.getReviewNotes();
        e.createdAt = m.getCreatedAt();
        e.updatedAt = m.getUpdatedAt();
        return e;
    }

    public static AdoptionApplicationModel toModel(AdoptionApplication e){
        if (e == null) return null;
        var m = new AdoptionApplicationModel();
        m.setId(e.id);
        m.setAdopterId(e.adopterId);
        m.setPetId(e.petId);
        m.setStatus(e.status);
        m.setUseDefaultPreferences(e.useDefaultPreferences);
        m.setSubmittedAt(e.submittedAt);
        m.setReviewedById(e.reviewedById);
        m.setReviewNotes(e.reviewNotes);
        m.setCreatedAt(e.createdAt);
        m.setUpdatedAt(e.updatedAt);
        return m;
    }
}