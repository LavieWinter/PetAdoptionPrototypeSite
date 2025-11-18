package com.example.PetAdoption.dominio.InterfRepositories;

import com.example.PetAdoption.dominio.entidades.EventModel;
import com.example.PetAdoption.dominio.enums.EventPhase;
import com.example.PetAdoption.dominio.enums.EventType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRepositoryPort {

    EventModel save(EventModel event);

    Optional<EventModel> findById(UUID petId, UUID eventId);

    List<EventModel> findAll(UUID petId,
                             EventType type,
                             EventPhase phase,
                             int page, int size,
                             String sortBy, boolean asc);

    boolean delete(UUID petId, UUID eventId);
}