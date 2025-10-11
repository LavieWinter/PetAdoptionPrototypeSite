package com.example.PetAdoption.interfAdaptadora.entidades;

import com.example.PetAdoption.dominio.entidades.AdoptionModel;
import com.example.PetAdoption.dominio.enums.AdoptionStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "adoptions")
public class Adoption {

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

    @Column(name = "created_at", columnDefinition = "timestamptz")
    private OffsetDateTime createdAt;

    protected Adoption() {} // exigido pelo Hibernate

    @PrePersist
    void onCreate(){ createdAt = OffsetDateTime.now(); }

    // -------- getters públicos (úteis p/ Jackson/MapStruct/uso externo) --------
    public UUID getId() { return id; }
    public UUID getApplicationId() { return applicationId; }
    public UUID getPetId() { return petId; }
    public UUID getTutorId() { return tutorId; }
    public AdoptionStatus getStatus() { return status; }
    public LocalDate getAdoptionDate() { return adoptionDate; }
    public LocalDate getEndDate() { return endDate; }
    public String getEndReason() { return endReason; }
    public OffsetDateTime getCreatedAt() { return createdAt; }

    // -------- conversores no seu padrão --------
    public static Adoption fromModel(AdoptionModel m) {
        if (m == null) return null;
        Adoption e = new Adoption();
        e.id = m.getId();
        e.applicationId = m.getApplicationId();
        e.petId = m.getPetId();
        e.tutorId = m.getTutorId();
        e.status = m.getStatus();
        e.adoptionDate = m.getAdoptionDate();
        e.endDate = m.getEndDate();
        e.endReason = m.getEndReason();
        e.createdAt = m.getCreatedAt();
        return e;
    }

    public static AdoptionModel toModel(Adoption e) {
        if (e == null) return null;
        AdoptionModel m = new AdoptionModel();
        m.setId(e.id);
        m.setApplicationId(e.applicationId);
        m.setPetId(e.petId);
        m.setTutorId(e.tutorId);
        m.setStatus(e.status);
        m.setAdoptionDate(e.adoptionDate);
        m.setEndDate(e.endDate);
        m.setEndReason(e.endReason);
        m.setCreatedAt(e.createdAt);
        return m;
    }
}