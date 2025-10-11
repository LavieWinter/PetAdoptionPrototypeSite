/* package com.example.PetAdoption.dominio.InterfRepositories;

import org.springframework.data.jpa.repository.*;
import com.example.PetAdoption.dominio.entidades.PetPhotoModel;
import java.util.*;
//buscar fotos do pet na ordem certa - principal primeiro
public interface PetPhotoRepository extends JpaRepository<PetPhotoModel, UUID> {
  @Query("""
    select ph from PetPhoto ph
    where ph.pet.id = :petId
    order by ph.isPrimary desc, ph.sortOrder asc, ph.createdAt asc
  """)
  List<PetPhotoModel> findOrderedByPet(UUID petId);
} */