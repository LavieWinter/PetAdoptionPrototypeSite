package com.example.PetAdoption.dominio.entidades;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
    name = "adopter_preferences",
    uniqueConstraints = @UniqueConstraint(
        name = "uq_adopter_pref_by_user",
        columnNames = "adopter_id"          // 0..1 por usuário
    )
)
public class AdopterPreferences {

    @Id
    @UuidGenerator
    @Column(name = "preference_key", nullable = false, updatable = false)
    private UUID id;

    // FK → user_admin(id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adopter_id", nullable = false)
    private UserModel adopter;

    // preferências “categóricas” (podem ser null = aceita qualquer)
    @Column(name = "desired_species")
    private String desiredSpecies;

    @Column(name = "desired_size")
    private String desiredSize;

    // flags (booleans) — hard/soft rules
    @Column(name = "accepts_special_needs")
    private Boolean acceptsSpecialNeeds;

    @Column(name = "accepts_ongoing_treatment")
    private Boolean acceptsOngoingTreatment;

    @Column(name = "accepts_chronic_disease")
    private Boolean acceptsChronicDisease;

    @Column(name = "has_other_pets")
    private Boolean hasOtherPets;

    @Column(name = "has_time_for_constant_care")
    private Boolean hasTimeForConstantCare;

    @Column(name = "updated_at", columnDefinition = "timestamptz")
    private OffsetDateTime updatedAt;

    @PrePersist @PreUpdate
    void touch() { this.updatedAt = OffsetDateTime.now(); }

    // getters/setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UserModel getAdopter() { return adopter; }
    public void setAdopter(UserModel adopter) { this.adopter = adopter; }

    public String getDesiredSpecies() { return desiredSpecies; }
    public void setDesiredSpecies(String desiredSpecies) { this.desiredSpecies = desiredSpecies; }

    public String getDesiredSize() { return desiredSize; }
    public void setDesiredSize(String desiredSize) { this.desiredSize = desiredSize; }

    public Boolean getAcceptsSpecialNeeds() { return acceptsSpecialNeeds; }
    public void setAcceptsSpecialNeeds(Boolean acceptsSpecialNeeds) { this.acceptsSpecialNeeds = acceptsSpecialNeeds; }

    public Boolean getAcceptsOngoingTreatment() { return acceptsOngoingTreatment; }
    public void setAcceptsOngoingTreatment(Boolean acceptsOngoingTreatment) { this.acceptsOngoingTreatment = acceptsOngoingTreatment; }

    public Boolean getAcceptsChronicDisease() { return acceptsChronicDisease; }
    public void setAcceptsChronicDisease(Boolean acceptsChronicDisease) { this.acceptsChronicDisease = acceptsChronicDisease; }

    public Boolean getHasOtherPets() { return hasOtherPets; }
    public void setHasOtherPets(Boolean hasOtherPets) { this.hasOtherPets = hasOtherPets; }

    public Boolean getHasTimeForConstantCare() { return hasTimeForConstantCare; }
    public void setHasTimeForConstantCare(Boolean hasTimeForConstantCare) { this.hasTimeForConstantCare = hasTimeForConstantCare; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}