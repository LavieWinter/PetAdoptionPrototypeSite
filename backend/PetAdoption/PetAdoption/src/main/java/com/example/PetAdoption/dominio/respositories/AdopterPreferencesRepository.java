package com.example.PetAdoption.dominio.respositories;
import com.example.PetAdoption.dominio.entidades.AdopterPreferences;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
//ler a preferencias default(0.. 1) do usuario adotante
public interface AdopterPreferencesRepository extends JpaRepository<AdopterPreferences, UUID> {
  Optional<AdopterPreferences> findByAdopter_Id(UUID adopterId);
}
