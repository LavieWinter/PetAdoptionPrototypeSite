package com.example.PetAdoption.dominio.respositories;
import com.example.PetAdoption.dominio.entidades.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface EventRepository extends JpaRepository<Event, UUID> { }
