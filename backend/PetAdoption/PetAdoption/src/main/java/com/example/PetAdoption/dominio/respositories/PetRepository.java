package com.example.PetAdoption.dominio.respositories;

import com.example.PetAdoption.dominio.entidades.Pet;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import java.util.*;
//ler e gravar os pets
public interface PetRepository extends JpaRepository<Pet, UUID> {
  @Query("""
    select p from Pet p
    where p.status = 'AVAILABLE'
    order by p.createdAt desc  
  """) //assim vai aparecer na ordem de criacao -- alterar depois
  List<Pet> listAvailable(Pageable pageable);
}
