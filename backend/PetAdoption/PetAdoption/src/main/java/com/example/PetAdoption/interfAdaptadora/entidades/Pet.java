package com.example.PetAdoption.interfAdaptadora.entidades;

import com.example.PetAdoption.dominio.entidades.PetModel;
import com.example.PetAdoption.dominio.enums.PetStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "pets")
public class Pet {

    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "name")   private String name;
    @Column(name = "species")private String species;
    @Column(name = "breed")  private String breed;
    @Column(name = "size")   private String size;
    @Column(name = "sex")    private String sex;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PetStatus status = PetStatus.AVAILABLE;

    @Column(name = "rescued_by_id") private UUID rescuedById;

    @Column(name = "has_special_needs")     private String  hasSpecialNeeds;
    @Column(name = "has_ongoing_treatment") private Boolean hasOngoingTreatment;
    @Column(name = "has_chronic_disease")   private String  hasChronicDisease;
    @Column(name = "good_with_other_animals") private Boolean goodWithOtherAnimals;
    @Column(name = "requires_constant_care")  private Boolean requiresConstantCare;

    @Column(name = "registered_date") private LocalDate registeredDate;
    @Column(name = "rescued_at")      private LocalDate rescuedAt;

    @Column(name = "created_at", columnDefinition = "timestamptz")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "timestamptz")
    private OffsetDateTime updatedAt;

    protected Pet() {}

    @PrePersist
    void onCreate() { createdAt = OffsetDateTime.now(); updatedAt = createdAt; }

    @PreUpdate
    void onUpdate() { updatedAt = OffsetDateTime.now(); }

    // getters públicos
    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getSpecies() { return species; }
    public String getBreed() { return breed; }
    public String getSize() { return size; }
    public String getSex() { return sex; }
    public PetStatus getStatus() { return status; }
    public UUID getRescuedById() { return rescuedById; }
    public String getHasSpecialNeeds() { return hasSpecialNeeds; }
    public Boolean getHasOngoingTreatment() { return hasOngoingTreatment; }
    public String getHasChronicDisease() { return hasChronicDisease; }
    public Boolean getGoodWithOtherAnimals() { return goodWithOtherAnimals; }
    public Boolean getRequiresConstantCare() { return requiresConstantCare; }
    public LocalDate getRegisteredDate() { return registeredDate; }
    public LocalDate getRescuedAt() { return rescuedAt; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }

    // conversores
    public static Pet fromModel(PetModel m) {
        if (m == null) return null;
        Pet e = new Pet();
        e.id = m.getId();
        e.name = m.getName();
        e.species = m.getSpecies();
        e.breed = m.getBreed();
        e.size = m.getSize();
        e.sex = m.getSex();
        e.status = m.getStatus();
        e.rescuedById = m.getRescuedById();
        e.hasSpecialNeeds = m.getHasSpecialNeeds();
        e.hasOngoingTreatment = m.getHasOngoingTreatment();
        e.hasChronicDisease = m.getHasChronicDisease();
        e.goodWithOtherAnimals = m.getGoodWithOtherAnimals();
        e.requiresConstantCare = m.getRequiresConstantCare();
        e.registeredDate = m.getRegisteredDate();
        e.rescuedAt = m.getRescuedAt();
        e.createdAt = m.getCreatedAt();
        e.updatedAt = m.getUpdatedAt();
        return e;
    }

    public static PetModel toModel(Pet e) {
        if (e == null) return null;
        PetModel m = new PetModel();
        m.setId(e.id);
        m.setName(e.name);
        m.setSpecies(e.species);
        m.setBreed(e.breed);
        m.setSize(e.size);
        m.setSex(e.sex);
        m.setStatus(e.status);
        m.setRescuedById(e.rescuedById);
        m.setHasSpecialNeeds(e.hasSpecialNeeds);
        m.setHasOngoingTreatment(e.hasOngoingTreatment);
        m.setHasChronicDisease(e.hasChronicDisease);
        m.setGoodWithOtherAnimals(e.goodWithOtherAnimals);
        m.setRequiresConstantCare(e.requiresConstantCare);
        m.setRegisteredDate(e.registeredDate);
        m.setRescuedAt(e.rescuedAt);
        m.setCreatedAt(e.createdAt);
        m.setUpdatedAt(e.updatedAt);
        return m;
    }
}