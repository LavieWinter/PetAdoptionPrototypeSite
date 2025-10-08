package com.example.PetAdoption.dominio.respositories;
import com.example.PetAdoption.dominio.entidades.Org;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface OrgRepository extends JpaRepository<Org, UUID> {
  Optional<Org> findByEmail(String email);
}