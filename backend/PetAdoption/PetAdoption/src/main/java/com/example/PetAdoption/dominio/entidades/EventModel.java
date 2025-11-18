package com.example.PetAdoption.dominio.entidades;
import com.example.PetAdoption.dominio.enums.EventPhase;
import com.example.PetAdoption.dominio.enums.EventType;

import java.time.LocalDate;
import java.util.UUID;

/** Domain Model: sem anotações JPA/Spring. */
public class EventModel {

    private UUID id;
    private UUID petId;        // FK -> pets.id
    private UUID adoptionId;   // FK opcional -> adoptions.id

    private EventType eventType; // HEALTH | RELOCATION | VISIT
    private EventPhase phase;    // RESCUE | SHELTER | POST_ADOPTION

    private LocalDate date;
    private String incidentType;  // VACCINATION|NEUTERING|DISEASE|OTHER 
    private String description;
    private String location;

    // getters/setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getPetId() { return petId; }
    public void setPetId(UUID petId) { this.petId = petId; }

    public UUID getAdoptionId() { return adoptionId; }
    public void setAdoptionId(UUID adoptionId) { this.adoptionId = adoptionId; }

    public EventType getEventType() { return eventType; }
    public void setEventType(EventType eventType) { this.eventType = eventType; }

    public EventPhase getPhase() { return phase; }
    public void setPhase(EventPhase phase) { this.phase = phase; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getIncidentType() { return incidentType; }
    public void setIncidentType(String incidentType) { this.incidentType = incidentType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}