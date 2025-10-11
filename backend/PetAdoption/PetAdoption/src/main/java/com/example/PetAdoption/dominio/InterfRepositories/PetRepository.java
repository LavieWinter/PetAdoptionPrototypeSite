/* package com.example.PetAdoption.dominio.InterfRepositories;
//por em interfaceAdaptadora.repositorios.jpa;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import com.example.PetAdoption.dominio.entidades.PetModel;
import java.util.*;
//ler e gravar os pets
public interface PetRepository extends JpaRepository<PetModel, UUID> {
  @Query("""
    select p from Pet p
    where p.status = 'AVAILABLE'
    order by p.createdAt desc  
  """) //assim vai aparecer na ordem de criacao -- alterar depois
  List<PetModel> listAvailable(Pageable pageable);
} */
