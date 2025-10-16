package com.example.PetAdoption.interfAdaptadora.interfJPA;

import com.example.PetAdoption.dominio.enums.EventPhase;
import com.example.PetAdoption.dominio.enums.EventType;
import com.example.PetAdoption.interfAdaptadora.entidades.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EventJpaRepository extends JpaRepository<Event, UUID> {

    Optional<Event> findByIdAndPetId(UUID id, UUID petId);

    Page<Event> findAllByPetId(UUID petId, Pageable pageable);

    Page<Event> findAllByPetIdAndEventType(UUID petId, EventType type, Pageable pageable);

    Page<Event> findAllByPetIdAndPhase(UUID petId, EventPhase phase, Pageable pageable);

    Page<Event> findAllByPetIdAndEventTypeAndPhase(UUID petId, EventType type, EventPhase phase, Pageable pageable);

    long deleteByIdAndPetId(UUID id, UUID petId);
}