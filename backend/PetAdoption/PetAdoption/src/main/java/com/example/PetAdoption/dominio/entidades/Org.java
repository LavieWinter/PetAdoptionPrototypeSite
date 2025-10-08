package com.example.PetAdoption.dominio.entidades;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;
import java.util.UUID;

@Entity
@Table(
    name = "org",
    uniqueConstraints = @UniqueConstraint(name = "uk_org_email", columnNames = "email")
)
public class Org {

    @Id
    @UuidGenerator
    @Column(name = "org_id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    // case-insensitive no Postgres
    @Column(name = "email", columnDefinition = "citext")
    private String email;

    @Column(name = "phone")
    private String phone;

    // FK opcional herdada do ERD (→ pets.id). Mantida como UUID para evitar ciclo aqui.
    @Column(name = "pet_owned_id")
    private UUID petOwnedId;

    // getters/setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public UUID getPetOwnedId() { return petOwnedId; }
    public void setPetOwnedId(UUID petOwnedId) { this.petOwnedId = petOwnedId; }
}