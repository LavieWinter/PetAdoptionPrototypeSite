package com.example.PetAdoption.dominio.entidades;

import java.time.OffsetDateTime;
import java.util.UUID;

/** Model de domínio: sem anotações de framework, não usa as próximas camadas  */
public class AdopterPreferencesModel {

    private UUID id;                 // preference_key
    private UUID adopterId;          // FK -> user_admin.id

    private String desiredSpecies;   // text
    private String desiredSize;      // text

    private Boolean acceptsSpecialNeeds;
    private Boolean acceptsOngoingTreatment;
    private Boolean acceptsChronicDisease;
    private Boolean hasOtherPets;
    private Boolean hasTimeForConstantCare;

    private OffsetDateTime updatedAt; // timestamptz

    // Getters/Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getAdopterId() { return adopterId; }
    public void setAdopterId(UUID adopterId) { this.adopterId = adopterId; }

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
