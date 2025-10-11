package com.example.PetAdoption.interfAdaptadora.entidades;
import com.example.PetAdoption.dominio.entidades.ApplicationPreferencesModel;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "application_preferences")
public class ApplicationPreferences {

    @Id
    @Column(name = "application_id", nullable = false, updatable = false)
    private UUID applicationId; // PK = FK para adoption_applications(id)

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

    protected ApplicationPreferences() {}

    @PrePersist
    void onCreate(){ createdAt = OffsetDateTime.now(); updatedAt = createdAt; }

    @PreUpdate
    void onUpdate(){ updatedAt = OffsetDateTime.now(); }

    // ---- getters públicos ----
    public UUID getApplicationId() { return applicationId; }
    public String getDesiredSpecies() { return desiredSpecies; }
    public String getDesiredSize() { return desiredSize; }
    public Boolean getAcceptsSpecialNeeds() { return acceptsSpecialNeeds; }
    public Boolean getAcceptsOngoingTreatment() { return acceptsOngoingTreatment; }
    public Boolean getAcceptsChronicDisease() { return acceptsChronicDisease; }
    public Boolean getHasOtherPets() { return hasOtherPets; }
    public Boolean getHasTimeForConstantCare() { return hasTimeForConstantCare; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }

    // ---- conversores  ----
    public static ApplicationPreferences fromModel(ApplicationPreferencesModel m){
        if (m == null) return null;
        var e = new ApplicationPreferences();
        e.applicationId = m.getApplicationId();
        e.desiredSpecies = m.getDesiredSpecies();
        e.desiredSize = m.getDesiredSize();
        e.acceptsSpecialNeeds = m.getAcceptsSpecialNeeds();
        e.acceptsOngoingTreatment = m.getAcceptsOngoingTreatment();
        e.acceptsChronicDisease = m.getAcceptsChronicDisease();
        e.hasOtherPets = m.getHasOtherPets();
        e.hasTimeForConstantCare = m.getHasTimeForConstantCare();
        e.createdAt = m.getCreatedAt();
        e.updatedAt = m.getUpdatedAt();
        return e;
    }

    public static ApplicationPreferencesModel toModel(ApplicationPreferences e){
        if (e == null) return null;
        var m = new ApplicationPreferencesModel();
        m.setApplicationId(e.applicationId);
        m.setDesiredSpecies(e.desiredSpecies);
        m.setDesiredSize(e.desiredSize);
        m.setAcceptsSpecialNeeds(e.acceptsSpecialNeeds);
        m.setAcceptsOngoingTreatment(e.acceptsOngoingTreatment);
        m.setAcceptsChronicDisease(e.acceptsChronicDisease);
        m.setHasOtherPets(e.hasOtherPets);
        m.setHasTimeForConstantCare(e.hasTimeForConstantCare);
        m.setCreatedAt(e.createdAt);
        m.setUpdatedAt(e.updatedAt);
        return m;
    }
}