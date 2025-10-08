package com.example.PetAdoption.dominio.entidades;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "application_preferences")
public class ApplicationPreferences {

    @Id
    @Column(name = "application_id")
    private UUID applicationId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id")
    private AdoptionApplication application;

    @Column(name = "desired_species")
    private String desiredSpecies;

    @Column(name = "desired_size")
    private String desiredSize;

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

    @Column(name = "created_at", columnDefinition = "timestamptz")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "timestamptz")
    private OffsetDateTime updatedAt;

    @PrePersist
    void onCreate(){ createdAt = OffsetDateTime.now(); updatedAt = createdAt; }

    @PreUpdate
    void onUpdate(){ updatedAt = OffsetDateTime.now(); }

    // getters/setters
    public UUID getApplicationId() { return applicationId; }
    public void setApplicationId(UUID applicationId) { this.applicationId = applicationId; }

    public AdoptionApplication getApplication() { return application; }
    public void setApplication(AdoptionApplication application) { this.application = application; }

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

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}