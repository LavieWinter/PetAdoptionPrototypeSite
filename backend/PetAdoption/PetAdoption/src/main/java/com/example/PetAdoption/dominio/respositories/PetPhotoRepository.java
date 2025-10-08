package com.example.PetAdoption.dominio.respositories;
import com.example.PetAdoption.dominio.entidades.PetPhoto;
import org.springframework.data.jpa.repository.*;
import java.util.*;
//buscar fotos do pet na ordem certa - principal primeiro
public interface PetPhotoRepository extends JpaRepository<PetPhoto, UUID> {
  @Query("""
    select ph from PetPhoto ph
    where ph.pet.id = :petId
    order by ph.isPrimary desc, ph.sortOrder asc, ph.createdAt asc
  """)
  List<PetPhoto> findOrderedByPet(UUID petId);
}