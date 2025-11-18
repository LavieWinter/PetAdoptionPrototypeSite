package com.example.PetAdoption.dominio.entidades;

import java.time.Instant;
import java.util.UUID;

public class AdopterPreferencesModel {

    private UUID preferenceKey;
    private UUID adopterId; // = user_admin.id
    private String desiredSpecies;   // manter String p/ não acoplar enum do Pet
    private String desiredSize;      // idem
    private Boolean acceptsSpecialNeeds;
    private Boolean acceptsOngoingTreatment;
    private Boolean acceptsChronicDisease;
    private Boolean hasOtherPets;
    private Boolean hasTimeForConstantCare;
    private Instant updatedAt;

    // === Construtores ===
    public AdopterPreferencesModel() {
    }

    public AdopterPreferencesModel(UUID preferenceKey, UUID adopterId, String desiredSpecies, String desiredSize,
                                   Boolean acceptsSpecialNeeds, Boolean acceptsOngoingTreatment,
                                   Boolean acceptsChronicDisease, Boolean hasOtherPets,
                                   Boolean hasTimeForConstantCare, Instant updatedAt) {
        this.preferenceKey = preferenceKey;
        this.adopterId = adopterId;
        this.desiredSpecies = desiredSpecies;
        this.desiredSize = desiredSize;
        this.acceptsSpecialNeeds = acceptsSpecialNeeds;
        this.acceptsOngoingTreatment = acceptsOngoingTreatment;
        this.acceptsChronicDisease = acceptsChronicDisease;
        this.hasOtherPets = hasOtherPets;
        this.hasTimeForConstantCare = hasTimeForConstantCare;
        this.updatedAt = updatedAt;
    }

    // === Getters e Setters ===

    public UUID getPreferenceKey() {
        return preferenceKey;
    }

    public void setPreferenceKey(UUID preferenceKey) {
        this.preferenceKey = preferenceKey;
    }

    public UUID getAdopterId() {
        return adopterId;
    }

    public void setAdopterId(UUID adopterId) {
        this.adopterId = adopterId;
    }

    public String getDesiredSpecies() {
        return desiredSpecies;
    }

    public void setDesiredSpecies(String desiredSpecies) {
        this.desiredSpecies = desiredSpecies;
    }

    public String getDesiredSize() {
        return desiredSize;
    }

    public void setDesiredSize(String desiredSize) {
        this.desiredSize = desiredSize;
    }

    public Boolean getAcceptsSpecialNeeds() {
        return acceptsSpecialNeeds;
    }

    public void setAcceptsSpecialNeeds(Boolean acceptsSpecialNeeds) {
        this.acceptsSpecialNeeds = acceptsSpecialNeeds;
    }

    public Boolean getAcceptsOngoingTreatment() {
        return acceptsOngoingTreatment;
    }

    public void setAcceptsOngoingTreatment(Boolean acceptsOngoingTreatment) {
        this.acceptsOngoingTreatment = acceptsOngoingTreatment;
    }

    public Boolean getAcceptsChronicDisease() {
        return acceptsChronicDisease;
    }

    public void setAcceptsChronicDisease(Boolean acceptsChronicDisease) {
        this.acceptsChronicDisease = acceptsChronicDisease;
    }

    public Boolean getHasOtherPets() {
        return hasOtherPets;
    }

    public void setHasOtherPets(Boolean hasOtherPets) {
        this.hasOtherPets = hasOtherPets;
    }

    public Boolean getHasTimeForConstantCare() {
        return hasTimeForConstantCare;
    }

    public void setHasTimeForConstantCare(Boolean hasTimeForConstantCare) {
        this.hasTimeForConstantCare = hasTimeForConstantCare;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    // === toString (útil para debug/logs) ===
    @Override
    public String toString() {
        return "AdopterPreferencesModel{" +
                "preferenceKey=" + preferenceKey +
                ", adopterId=" + adopterId +
                ", desiredSpecies='" + desiredSpecies + '\'' +
                ", desiredSize='" + desiredSize + '\'' +
                ", acceptsSpecialNeeds=" + acceptsSpecialNeeds +
                ", acceptsOngoingTreatment=" + acceptsOngoingTreatment +
                ", acceptsChronicDisease=" + acceptsChronicDisease +
                ", hasOtherPets=" + hasOtherPets +
                ", hasTimeForConstantCare=" + hasTimeForConstantCare +
                ", updatedAt=" + updatedAt +
                '}';
    }
}