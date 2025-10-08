package com.example.PetAdoption.dominio.respositories;

import com.example.PetAdoption.dominio.entidades.AdoptionApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
//criar e ler adoption_application - as inscricoes do adotante
public interface ApplicationRepository extends JpaRepository<AdoptionApplication, UUID> { }