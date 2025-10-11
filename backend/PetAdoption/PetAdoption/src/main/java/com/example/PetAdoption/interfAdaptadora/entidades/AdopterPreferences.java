package com.example.PetAdoption.interfAdaptadora.entidades;
import com.example.PetAdoption.dominio.entidades.AdopterPreferencesModel;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
    name = "adopter_preferences",
    uniqueConstraints = @UniqueConstraint(name = "uq_adopter_pref_by_user", columnNames = "adopter_id")
)
public class AdopterPreferences {

    @Id
    @UuidGenerator
    @Column(name = "preference_key", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "adopter_id", nullable = false)
    private UUID adopterId;

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

    @Column(name = "updated_at", columnDefinition = "timestamptz")
    private OffsetDateTime updatedAt;

    @PrePersist @PreUpdate
    void touch(){ this.updatedAt = OffsetDateTime.now(); }

    protected AdopterPreferences() {}

    // ---------- getters públicos ----------
    public UUID getId() { return id; }
    public UUID getAdopterId() { return adopterId; }
    public String getDesiredSpecies() { return desiredSpecies; }
    public String getDesiredSize() { return desiredSize; }
    public Boolean getAcceptsSpecialNeeds() { return acceptsSpecialNeeds; }
    public Boolean getAcceptsOngoingTreatment() { return acceptsOngoingTreatment; }
    public Boolean getAcceptsChronicDisease() { return acceptsChronicDisease; }
    public Boolean getHasOtherPets() { return hasOtherPets; }
    public Boolean getHasTimeForConstantCare() { return hasTimeForConstantCare; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }

    // ---------- conversores ----------
    public static AdopterPreferences fromModel(AdopterPreferencesModel m) {
        if (m == null) return null;
        AdopterPreferences e = new AdopterPreferences();
        e.id = m.getId();
        e.adopterId = m.getAdopterId();
        e.desiredSpecies = m.getDesiredSpecies();
        e.desiredSize = m.getDesiredSize();
        e.acceptsSpecialNeeds = m.getAcceptsSpecialNeeds();
        e.acceptsOngoingTreatment = m.getAcceptsOngoingTreatment();
        e.acceptsChronicDisease = m.getAcceptsChronicDisease();
        e.hasOtherPets = m.getHasOtherPets();
        e.hasTimeForConstantCare = m.getHasTimeForConstantCare();
        e.updatedAt = m.getUpdatedAt();
        return e;
    }

    public static AdopterPreferencesModel toModel(AdopterPreferences e) {
        if (e == null) return null;
        AdopterPreferencesModel m = new AdopterPreferencesModel();
        m.setId(e.id);
        m.setAdopterId(e.adopterId);
        m.setDesiredSpecies(e.desiredSpecies);
        m.setDesiredSize(e.desiredSize);
        m.setAcceptsSpecialNeeds(e.acceptsSpecialNeeds);
        m.setAcceptsOngoingTreatment(e.acceptsOngoingTreatment);
        m.setAcceptsChronicDisease(e.acceptsChronicDisease);
        m.setHasOtherPets(e.hasOtherPets);
        m.setHasTimeForConstantCare(e.hasTimeForConstantCare);
        m.setUpdatedAt(e.updatedAt);
        return m;
    }
}