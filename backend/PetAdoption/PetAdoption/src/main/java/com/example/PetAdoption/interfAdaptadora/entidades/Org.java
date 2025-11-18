package com.example.PetAdoption.interfAdaptadora.entidades;

import com.example.PetAdoption.dominio.entidades.OrgModel;
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

    // Postgres CITEXT (case-insensitive)
    @Column(name = "email", columnDefinition = "citext")
    private String email;

    @Column(name = "phone")
    private String phone;

    // FK opcional herdada do ERD (-> pets.id)
    @Column(name = "pet_owned_id")
    private UUID petOwnedId;

    protected Org() {}

    // getters públicos
    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public UUID getPetOwnedId() { return petOwnedId; }

    // conversores
    public static Org fromModel(OrgModel m) {
        if (m == null) return null;
        Org e = new Org();
        e.id = m.getId();
        e.name = m.getName();
        e.email = m.getEmail();
        e.phone = m.getPhone();
        e.petOwnedId = m.getPetOwnedId();
        return e;
    }

    public static OrgModel toModel(Org e) {
        if (e == null) return null;
        OrgModel m = new OrgModel();
        m.setId(e.id);
        m.setName(e.name);
        m.setEmail(e.email);
        m.setPhone(e.phone);
        m.setPetOwnedId(e.petOwnedId);
        return m;
    }
}