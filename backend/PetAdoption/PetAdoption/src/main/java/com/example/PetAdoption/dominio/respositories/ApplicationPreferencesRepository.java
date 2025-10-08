package com.example.PetAdoption.dominio.respositories;

import com.example.PetAdoption.dominio.entidades.ApplicationPreferences;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
//persistir e ler o snapchot de preferencias de uma application
public interface ApplicationPreferencesRepository extends JpaRepository<ApplicationPreferences, UUID> {
  Optional<ApplicationPreferences> findByApplication_Id(UUID applicationId);
}
