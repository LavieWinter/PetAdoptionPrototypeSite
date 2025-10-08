package com.example.PetAdoption.dominio.respositories;
import com.example.PetAdoption.dominio.entidades.PetPhotoInline;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface PetPhotoInlineRepository extends JpaRepository<PetPhotoInline, UUID> { }
