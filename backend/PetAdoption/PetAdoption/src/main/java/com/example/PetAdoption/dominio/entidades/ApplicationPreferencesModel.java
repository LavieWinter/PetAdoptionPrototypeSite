package com.example.PetAdoption.dominio.entidades;
import java.time.OffsetDateTime;
import java.util.UUID;

/** Domain Model: snapshot 1:1 por aplicacao */
public class ApplicationPreferencesModel {

    private UUID applicationId;              // PK & FK -> adoption_applications.id

    private String desiredSpecies;
    private String desiredSize;

    private Boolean acceptsSpecialNeeds;
    private Boolean acceptsOngoingTreatment;
    private Boolean acceptsChronicDisease;
    private Boolean hasOtherPets;
    private Boolean hasTimeForConstantCare;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    // ---- getters/setters ----
    public UUID getApplicationId() { return applicationId; }
    public void setApplicationId(UUID applicationId) { this.applicationId = applicationId; }

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