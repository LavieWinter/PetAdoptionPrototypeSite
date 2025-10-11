package com.example.PetAdoption.interfAdaptadora.entidades;
import com.example.PetAdoption.dominio.entidades.EventModel;
import com.example.PetAdoption.dominio.enums.EventPhase;
import com.example.PetAdoption.dominio.enums.EventType;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "pet_id", nullable = false)
    private UUID petId;

    @Column(name = "adoption_id")
    private UUID adoptionId;  // opcional

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private EventType eventType;

    @Enumerated(EnumType.STRING)
    @Column(name = "phase", nullable = false)
    private EventPhase phase;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "incident_type")
    private String incidentType;

    @Column(name = "description")
    private String description;

    @Column(name = "location")
    private String location;

    protected Event() {}

    // getters públicos
    public UUID getId() { return id; }
    public UUID getPetId() { return petId; }
    public UUID getAdoptionId() { return adoptionId; }
    public EventType getEventType() { return eventType; }
    public EventPhase getPhase() { return phase; }
    public LocalDate getDate() { return date; }
    public String getIncidentType() { return incidentType; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }

    // conversores
    public static Event fromModel(EventModel m) {
        if (m == null) return null;
        Event e = new Event();
        e.id = m.getId();
        e.petId = m.getPetId();
        e.adoptionId = m.getAdoptionId();
        e.eventType = m.getEventType();
        e.phase = m.getPhase();
        e.date = m.getDate();
        e.incidentType = m.getIncidentType();
        e.description = m.getDescription();
        e.location = m.getLocation();
        return e;
    }

    public static EventModel toModel(Event e) {
        if (e == null) return null;
        EventModel m = new EventModel();
        m.setId(e.id);
        m.setPetId(e.petId);
        m.setAdoptionId(e.adoptionId);
        m.setEventType(e.eventType);
        m.setPhase(e.phase);
        m.setDate(e.date);
        m.setIncidentType(e.incidentType);
        m.setDescription(e.description);
        m.setLocation(e.location);
        return m;
    }
}