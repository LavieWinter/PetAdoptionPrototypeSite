package com.example.PetAdoption.dominio.respositories;

import com.example.PetAdoption.dominio.entidades.Adoption;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface AdoptionRepository extends JpaRepository<Adoption, UUID> { }
