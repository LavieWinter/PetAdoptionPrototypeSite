package com.example.PetAdoption.interfAdaptadora.entidades;

import com.example.PetAdoption.dominio.entidades.AdopterPreferencesModel;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "adopter_preferences")
public class AdopterPreferenceEntity {

    @Id
    @Column(name = "preference_key")
    private UUID preferenceKey;

    @Column(name = "adopter_id", nullable = false)
    private UUID adopterId; // FK para user_admin(id)

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

    @Column(name = "updated_at")
    private Instant updatedAt;

    // =====================
    // Construtores
    // =====================
    public AdopterPreferenceEntity() {
    }

    public AdopterPreferenceEntity(UUID preferenceKey,
                                   UUID adopterId,
                                   String desiredSpecies,
                                   String desiredSize,
                                   Boolean acceptsSpecialNeeds,
                                   Boolean acceptsOngoingTreatment,
                                   Boolean acceptsChronicDisease,
                                   Boolean hasOtherPets,
                                   Boolean hasTimeForConstantCare,
                                   Instant updatedAt) {
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

    // =====================
    // Callbacks JPA
    // =====================
    @PrePersist
    public void prePersist() {
        if (preferenceKey == null) preferenceKey = UUID.randomUUID();
        if (updatedAt == null) updatedAt = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }

    // =====================
    // Getters e Setters
    // =====================
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

    // =====================
    // Conversores Domain <-> Entity
    // =====================

    /** Converte Domain -> Entity */
    public static AdopterPreferenceEntity fromModel(AdopterPreferencesModel m) {
        if (m == null) return null;
        return new AdopterPreferenceEntity(
                m.getPreferenceKey(),
                m.getAdopterId(),
                m.getDesiredSpecies(),
                m.getDesiredSize(),
                m.getAcceptsSpecialNeeds(),
                m.getAcceptsOngoingTreatment(),
                m.getAcceptsChronicDisease(),
                m.getHasOtherPets(),
                m.getHasTimeForConstantCare(),
                m.getUpdatedAt()
        );
    }

    /** Converte esta Entity -> Domain */
    public AdopterPreferencesModel toModel() {
        AdopterPreferencesModel m = new AdopterPreferencesModel();
        m.setPreferenceKey(this.preferenceKey);
        m.setAdopterId(this.adopterId);
        m.setDesiredSpecies(this.desiredSpecies);
        m.setDesiredSize(this.desiredSize);
        m.setAcceptsSpecialNeeds(this.acceptsSpecialNeeds);
        m.setAcceptsOngoingTreatment(this.acceptsOngoingTreatment);
        m.setAcceptsChronicDisease(this.acceptsChronicDisease);
        m.setHasOtherPets(this.hasOtherPets);
        m.setHasTimeForConstantCare(this.hasTimeForConstantCare);
        m.setUpdatedAt(this.updatedAt);
        return m;
    }

    @Override
    public String toString() {
        return "AdopterPreferenceEntity{" +
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